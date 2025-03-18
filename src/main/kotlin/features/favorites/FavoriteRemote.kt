package com.smurzik.features.favorites

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteResponseRemote(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: String,
    val albumUri: String,
    val index: Int
)

@Serializable
data class FavoriteReceiveRemote(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: String,
    val albumUri: String,
    val index: Int
)