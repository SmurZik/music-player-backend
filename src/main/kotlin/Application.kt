package com.smurzik

import com.smurzik.features.login.configureLoginRouting
import com.smurzik.features.register.configureRegisterRouting
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureLoginRouting()
    configureRegisterRouting()
    configureRouting()
}
