package com.smurzik.features.user

import com.smurzik.database.tokens.Tokens
import com.smurzik.database.users.UserDTO
import com.smurzik.database.users.Users
import com.smurzik.features.register.RegisterReceiveRemote
import com.smurzik.utils.TokenCheck
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class UserController(private val call: ApplicationCall) {

    suspend fun fetchUser() {
        val token = call.request.headers["Bearer-Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val login = token?.let { Tokens.fetchLogin(it) }
            val user = login?.let { Users.fetchUser(login) }

            if (user != null) {
                val userRemote = UserResponseRemote(
                    login = user.login,
                    password = user.password,
                    username = user.username
                )
                call.respond(userRemote)
            } else {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun updateUser() {
        val token = call.request.headers["Bearer-Authorization"]
        val userReceiveRemote = call.receive<UserReceiveRemote>()

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            // валидация пароля
            val userDTO = UserDTO(
                login = userReceiveRemote.login,
                password = userReceiveRemote.password,
                username = userReceiveRemote.username
            )
            Users.update(userDTO)
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }
}