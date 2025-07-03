package com.jin

import com.jin.repository.UserRepo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.User

fun Application.configureRouting() {
}

fun Route.userRoutes(repo: UserRepo) {
    route("/users") {
        get { call.respond(repo.all()) }
        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) return@get call.respond(HttpStatusCode.BadRequest, "ID không hợp lệ")
            repo.findById(id)?.let { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound, "Không tìm thấy user")
        }

        post {
            val user = call.receive<User>()
            val created = repo.create(user)
            call.respond(HttpStatusCode.Created, created)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) return@put call.respond(HttpStatusCode.BadRequest, "ID không hợp lệ")
            val user = call.receive<User>()
            if (repo.update(id, user))
                call.respond(HttpStatusCode.OK, "Cập nhật thành công")
            else
                call.respond(HttpStatusCode.NotFound, "Không tìm thấy user")
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) return@delete call.respond(HttpStatusCode.BadRequest, "ID không hợp lệ")
            if (repo.delete(id))
                call.respond(HttpStatusCode.NoContent)
            else
                call.respond(HttpStatusCode.NotFound, "Không tìm thấy user")
        }
    }
}