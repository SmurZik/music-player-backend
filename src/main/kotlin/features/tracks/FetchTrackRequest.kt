package com.smurzik.features.tracks

import kotlinx.serialization.Serializable

@Serializable
data class FetchTrackRequest(
    val token: String
)