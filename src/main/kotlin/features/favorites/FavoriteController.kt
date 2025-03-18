package com.smurzik.features.favorites

import com.smurzik.database.favorites.Favorites
import com.smurzik.database.tokens.Tokens
import com.smurzik.database.tracks.TrackDTO
import com.smurzik.utils.TokenCheck
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class FavoriteController(private val call: ApplicationCall) {
    suspend fun changeFavorite() {
        val token = call.request.headers["Bearer-Authorization"]
        val favoriteReceiveRemote = call.receive<FavoriteReceiveRemote>()
        val trackId = favoriteReceiveRemote.id

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val loginUser = token?.let { Tokens.fetchLogin(it) }

            if (loginUser != null) {
                if (Favorites.isFavorite(trackId, loginUser)) {
                    Favorites.removeTrackFromFavorites(loginUser, trackId)
                } else {
                    Favorites.addTrackToFavorites(
                        loginUser, TrackDTO(
                            favoriteReceiveRemote.id,
                            favoriteReceiveRemote.title,
                            favoriteReceiveRemote.artist,
                            favoriteReceiveRemote.album,
                            favoriteReceiveRemote.duration,
                            favoriteReceiveRemote.uri,
                            favoriteReceiveRemote.albumUri,
                            favoriteReceiveRemote.index
                        )
                    )
                }
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun fetchFavorites() {
        val token = call.request.headers["Bearer-Authorization"]
        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val loginUser = token?.let { Tokens.fetchLogin(it) }

            if (loginUser != null) {
                val favoritesRemote = Favorites.getFavoriteTracks(loginUser)
                call.respond(favoritesRemote.map {
                    FavoriteResponseRemote(
                        it.id,
                        it.title,
                        it.artist,
                        it.album,
                        it.duration,
                        it.uri,
                        it.albumUri,
                        it.index
                    )
                })
            } else {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            }
        }
    }
}