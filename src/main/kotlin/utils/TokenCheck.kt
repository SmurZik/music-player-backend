package com.smurzik.utils

import com.smurzik.database.tokens.Tokens

object TokenCheck {

    fun isTokenValid(token: String): Boolean = Tokens.fetchTokens().firstOrNull { it.token == token } != null
}