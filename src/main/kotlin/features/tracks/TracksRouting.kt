package com.smurzik.features.tracks

import com.smurzik.cache.InMemoryCache
import com.smurzik.cache.TokenCache
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureGamesRouting() {
    routing {
        post("/tracks/fetch") {
            val tracksController = TracksController(call)
            tracksController.fetchAllTracks()
        }

        post("/tracks/add") {
            val tracksController = TracksController(call)
            tracksController.addTrack()
        }
    }
}