package com.smurzik.features.favorites

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureFavoriteRouting() {
    routing {
        post("/favorite/change") {
            val favoriteController = FavoriteController(call)
            favoriteController.changeFavorite()
        }
        get("/favorite/fetch") {
            val favoriteController = FavoriteController(call)
            favoriteController.fetchFavorites()
        }
    }
}