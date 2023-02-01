package com.example.plugins

import com.example.routes.notes.authenticationRoutes
import com.example.routes.notes.notesRoute
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }

    notesRoute()
    authenticationRoutes()
}
