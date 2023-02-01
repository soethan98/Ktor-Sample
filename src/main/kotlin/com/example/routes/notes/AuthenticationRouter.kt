package com.example.routes.notes

import com.example.Database
import com.example.entities.UserEntity
import com.example.models.NotesResponse
import com.example.models.UserCredentials
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.authenticationRoutes() {
    routing {
        post("/register") {
            val userCredentials = call.receive<UserCredentials>()

            if (!userCredentials.isValidCredentials()) {
                call.respond(HttpStatusCode.BadRequest,
                    NotesResponse(success = false,
                        data = "Username should be greater than or equal to 3 and password should be greater than or equal to 8"))
                return@post
            }
            val username = userCredentials.username.lowercase()
            val password = userCredentials.hashPassword()
            // Check if username already exists
           val user =  Database.database.from(UserEntity).select()
                .where{
                    UserEntity.username eq username
                }
                .map { it[UserEntity.username] }
                .firstOrNull()
            if (user!=null){
                call.respond(HttpStatusCode.BadRequest,
                    NotesResponse(success = false, data = "User already exist,please try a different username"))
                return@post
            }
            Database.database.insert(UserEntity) {
                set(it.username,username)
                set(it.password, password)
            }

            call.respond(HttpStatusCode.Created,
                NotesResponse(success = true, data = "User has been successfully created"))

        }
    }
}