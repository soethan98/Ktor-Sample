package com.example.routes.notes

import com.example.Database
import com.example.entities.NotesEntity
import com.example.models.CreateNoteRequest
import com.example.models.Note
import com.example.models.NotesResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import javax.xml.crypto.Data


fun Application.notesRoute() {
    routing {
        get("/notes") {
            val notes = Database.database.from(NotesEntity).select().map {
                val id = it[NotesEntity.id]
                val note = it[NotesEntity.note]
                Note(id ?: -1, note ?: "")
            }

            call.respond(HttpStatusCode.OK, notes)
        }

        post("/notes") {
            val request = call.receive<CreateNoteRequest>()
            val result = Database.database.insert(NotesEntity) {
                set(it.note, request.note)
            }

            if (result == 1) {
                call.respond(
                    HttpStatusCode.OK,
                    NotesResponse(success = true, data = "Value successfully inserted")
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NotesResponse(success = false, data = "Failed to insert.")
                )
            }
        }

        get("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val note = Database.database.from(NotesEntity)
                .select()
                .where {
                    NotesEntity.id eq id
                }
                .mapNotNull {
                    val id = it[NotesEntity.id]
                    val note = it[NotesEntity.note]
                    Note(
                        id = id!!,
                        note = note!!
                    )

                }.firstOrNull()

            if (note != null) {
                call.respond(
                    HttpStatusCode.OK, NotesResponse(
                        success = true,
                        data = note
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK, NotesResponse(
                        success = false,
                        data = "Note not found"
                    )
                )
            }

        }

        delete("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val result = Database.database.delete(NotesEntity) {
                it.id eq id
            }
            if (result == 1) {
                call.respond(
                    HttpStatusCode.NoContent, NotesResponse<String>(
                        success = true,
                        data = "Note successfully deleted"
                    )
                )
                return@delete
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NotesResponse<String>(success = false, data = "Failed to delete note.")
                )
            }

        }

        put("notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val updateNote = call.receive<CreateNoteRequest>()
            val rowsEffected = Database.database.update(NotesEntity) {
                set(it.note, updateNote.note)
                where {
                    it.id eq id
                }
            }
            if (rowsEffected == 1) {
                call.respond(
                    HttpStatusCode.NoContent,
                    NotesResponse<String>(
                        success = true,
                        data = "Note successfully updated"
                    )
                )
                return@put
            }
            call.respond(
                HttpStatusCode.BadRequest,
                NotesResponse<String>(
                    success = false,
                    data = "Failed to update note."
                )
            )
        }
    }
}