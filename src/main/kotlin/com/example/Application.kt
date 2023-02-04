package com.example

import com.example.entities.NotesEntity
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.select


fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val tokenManager = TokenManager(config)

    install(Authentication){
        jwt {
            verifyJWT(tokenManager,config)
        }
    }

    install(ContentNegotiation){
        json()
    }
    configureRouting()

}

private fun JWTAuthenticationProvider.Config.verifyJWT(
    tokenManager: TokenManager,
    config:HoconApplicationConfig
){
verifier(tokenManager.verifyJWTToken())
    realm = config.property("ktor.jwt.realm").getString()
    validate { jwtCredential ->
        if(jwtCredential.payload.getClaim("username").asString().isNotEmpty()){
            JWTPrincipal(jwtCredential.payload)
        }else{
            null
        }
    }
}




object  Database{
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/notes",
        driver = "com.mysql.cj.jdbc.Driver",
        user = System.getenv("MYSQL_USERNAME"),
        password = System.getenv("MYSQL_PASSWORD")
    )
}
