package com.jin

import com.jin.models.BaseResponse
import com.jin.repository.UserRepo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import models.User

fun Application.configureRouting() {
}

fun Route.userRoutes(repo: UserRepo) {
    route("/users") {
        get {
            val users = repo.all()
            call.respondBase(HttpStatusCode.OK, "Lấy danh sách thành công", users)
        }
        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respondBase(HttpStatusCode.BadRequest, "ID không hợp lệ", Unit)
                return@get
            }

            val user = repo.findById(id)
            if (user != null) {
                call.respondBase(
                    HttpStatusCode.OK,
                    "Lấy user thành công",
                    user
                )
            } else {
                call.respondBase(
                    HttpStatusCode.OK,
                    "Không tìm thấy user với id = $id",
                    Unit
                )
            }
        }

        post {
            val userReq = call.receive<User>()
            val created = repo.create(userReq)
            call.respondBase(
                HttpStatusCode.Created,
                "Tạo user thành công",
                created
            )
        }

        put("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respondBase(HttpStatusCode.BadRequest, "ID không hợp lệ", Unit)
                return@put
            }
            val userReq = call.receive<User>()
            if (repo.update(id, userReq)) {
                call.respondBase(
                    HttpStatusCode.OK,
                    "Cập nhật thành công",
                    Unit
                )
            } else {
                call.respondBase(
                    HttpStatusCode.NotFound,
                    "Không tìm thấy user với id = $id",
                    Unit
                )
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respondBase(
                    HttpStatusCode.BadRequest,
                    "ID không hợp lệ",
                    Unit
                )
                return@delete
            }
            if (repo.delete(id)) {
                call.respondBase(
                    HttpStatusCode.NoContent,
                    "Xóa thành công",
                    Unit
                )
            } else {
                call.respondBase(
                    HttpStatusCode.NotFound,
                    "Không tìm thấy user với id = $id",
                    Unit
                )
            }
        }
    }
}


suspend inline fun <reified T> ApplicationCall.respondBase(
    status: HttpStatusCode,
    message: String,
    data: T? = null
) {
    respond(
        status,
        BaseResponse(
            code = status.value,
            message = message,
            data = data
        )
    )
}