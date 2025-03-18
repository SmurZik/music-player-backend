package com.smurzik.database.tracks

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tracks : Table() {
    val id = Tracks.long("id")
    val title = Tracks.varchar("title", 75)
    val artist = Tracks.varchar("artist", 50)
    val album = Tracks.varchar("album", 50)
    val duration = Tracks.long("duration")
    val uri = Tracks.varchar("uri", 300)
    val albumUri = Tracks.varchar("albumUri", 300)
    val index = Tracks.integer("index")

    fun fetchTracks(): List<TrackDTO> {
        return try {
            transaction {
                Tracks.selectAll().toList()
                    .map {
                        TrackDTO(
                            id = it[Tracks.id],
                            title = it[title],
                            artist = it[artist],
                            album = it[album],
                            duration = it[Tracks.duration],
                            uri = it[uri],
                            albumUri = it[albumUri],
                            index = it[index]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun insertTrack(track: TrackDTO) {
        transaction {
            Tracks.insert {
                it[id] = track.id
                it[title] = track.title
                it[artist] = track.artist
                it[album] = track.album
                it[duration] = track.duration
                it[uri] = track.uri
                it[albumUri] = track.albumUri
                it[index] = track.index
            }
        }
    }

    fun trackExists(trackId: Long): Boolean {
        return transaction {
            Tracks.selectAll().where { Tracks.id eq trackId }.count() > 0
        }
    }

    fun deleteTrack(trackId: Long) {
        transaction {
            Tracks.deleteWhere { Tracks.id eq trackId }
        }
    }
}