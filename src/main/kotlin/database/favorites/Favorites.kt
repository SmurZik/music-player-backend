package com.smurzik.database.favorites

import com.smurzik.database.tracks.TrackDTO
import com.smurzik.database.tracks.Tracks
import com.smurzik.database.tracks.Tracks.album
import com.smurzik.database.tracks.Tracks.albumUri
import com.smurzik.database.users.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Favorites : Table("favorites") {
    private val userLogin = reference("userLogin", Users.login)
    private val trackId = reference("trackId", Tracks.id)

    fun addTrackToFavorites(userLogin: String, track: TrackDTO) {
        transaction {

            val trackExists = Tracks.trackExists(track.id)

            if (!trackExists) {
                Tracks.insertTrack(track)
            }

            Favorites.insertIgnore {
                it[Favorites.userLogin] = userLogin
                it[Favorites.trackId] = track.id
            }
        }
    }

    fun isFavorite(trackId: Long, userLogin: String): Boolean {
        return transaction {
            Favorites.selectAll().where { (Favorites.trackId eq trackId) and (Favorites.userLogin eq userLogin) }
                .count() > 0
        }
    }

    fun removeTrackFromFavorites(userLogin: String, trackId: Long) {
        transaction {
            Favorites.deleteWhere { (Favorites.userLogin eq userLogin) and (Favorites.trackId eq trackId) }
            val isStillFavorite = Favorites.selectAll().where { Favorites.trackId eq trackId }.count() > 0
            if (!isStillFavorite) {
                Tracks.deleteTrack(trackId)
            }
        }
    }

    fun getFavoriteTracks(userLogin: String): List<TrackDTO> {
        return transaction {
            (Favorites innerJoin Tracks)
                .selectAll().where { Favorites.userLogin eq userLogin }
                .map {
                    TrackDTO(
                        id = it[Tracks.id],
                        title = it[Tracks.title],
                        artist = it[Tracks.artist],
                        album = it[Tracks.album],
                        duration = it[Tracks.duration],
                        uri = it[Tracks.uri],
                        albumUri = it[Tracks.albumUri],
                        index = it[Tracks.index]
                    )
                }
        }
    }
}