package com.smurzik.database.tracks

data class TrackDTO(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: String,
    val albumUri: String,
    val index: Int
)