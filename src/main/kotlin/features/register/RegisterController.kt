package com.smurzik.features.register

import com.smurzik.database.tokens.TokenDTO
import com.smurzik.database.tokens.Tokens
import com.smurzik.database.users.UserDTO
import com.smurzik.database.users.Users
import com.smurzik.utils.hashPassword
import com.smurzik.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        val hashedPassword = hashPassword(registerReceiveRemote.password)
        if (!registerReceiveRemote.login.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email in not valid")
        }
        val userDTO = Users.fetchUser(registerReceiveRemote.login)

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString()

            try {
                Users.insert(
                    UserDTO(
                        login = registerReceiveRemote.login,
                        password = hashedPassword,
                        username = registerReceiveRemote.username
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }

            Tokens.insert(
                TokenDTO(
                    rowId = UUID.randomUUID().toString(),
                    login = registerReceiveRemote.login,
                    token = token
                )
            )
            call.respond(RegisterResponseRemote(token = token))
        }
    }
}