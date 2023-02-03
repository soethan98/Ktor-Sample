package com.example

import com.example.entities.NotesEntity
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.select


fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    install(Authentication){
        jwt {

        }
    }

    install(ContentNegotiation){
        json()
    }
    configureRouting()

    val name =System.getenv("MYSQL_PASSWORD")
    println("${name}")


}


object  Database{
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/notes",
        driver = "com.mysql.cj.jdbc.Driver",
        user = System.getenv("MYSQL_USERNAME"),
        password = System.getenv("MYSQL_PASSWORD")
    )
}
