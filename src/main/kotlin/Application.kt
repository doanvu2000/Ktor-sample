package com.jin

import com.jin.repository.UserRepo
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
//    EngineMain.main(args)
    // 1. Kết nối database H2 in-memory
    val db = Database.connect(
        url = "jdbc:h2:mem:users;DB_CLOSE_DELAY=-1",
        driver = "org.h2.Driver",
        user = "sa",
        password = ""
    )
    // 2. Tạo repo (init table ngay trong init block)
    val userRepo = UserRepo(db)

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) { json() }
        routing {
            // ... nếu bạn có các route khác thì giữ nguyên
            userRoutes(userRepo)
        }
    }.start(wait = true)
}

fun Application.module() {
//    configureHTTP()
    configureSerialization()
    configureRouting()
}