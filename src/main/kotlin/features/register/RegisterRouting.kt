package com.smurzik.features.register

import com.smurzik.cache.InMemoryCache
import com.smurzik.cache.TokenCache
import com.smurzik.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val receive = call.receive<RegisterReceiveRemote>()
            if (!receive.email.isValidEmail()) {
                call.respond(HttpStatusCode.BadRequest, "Email in not valid")
            }

            if (InMemoryCache.userList.map { it.email }.contains(receive.email)) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }

            val token = UUID.randomUUID().toString()
            InMemoryCache.userList.add(receive)
            InMemoryCache.token.add(TokenCache(login = receive.email, token = token))

            call.respond(RegisterResponseRemote(token = token))
        }
    }
}