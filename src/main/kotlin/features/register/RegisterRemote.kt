package com.smurzik.features.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReceiveRemote(
    val login: String,
    val password: String
)

@Serializable
data class RegisterResponseRemote(
    val token: String
)
