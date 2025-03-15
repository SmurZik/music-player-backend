package com.smurzik.database.tracks

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tracks : Table() {
    private val id = Tracks.long("id")
    private val title = Tracks.varchar("title", 75)
    private val artist = Tracks.varchar("artist", 50)
    private val album = Tracks.varchar("album", 50)
    private val duration = Tracks.long("duration")
    private val uri = Tracks.varchar("uri", 100)
    private val albumUri = Tracks.varchar("albumUri", 100)
    private val index = Tracks.integer("index")

    fun insert(trackDTO: TrackDTO) {
        transaction {
            Tracks.insert {
                it[id] = trackDTO.id
                it[title] = trackDTO.title
                it[artist] = trackDTO.artist
                it[album] = trackDTO.album
                it[duration] = trackDTO.duration
                it[uri] = trackDTO.uri
                it[albumUri] = trackDTO.albumUri
                it[index] = trackDTO.index
            }
        }
    }

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
}