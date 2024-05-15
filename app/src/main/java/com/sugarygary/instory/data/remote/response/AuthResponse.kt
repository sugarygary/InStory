package com.sugarygary.instory.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse(

    @Json(name = "loginResult") val loginResult: LoginResult? = null,

    @Json(name = "error") val error: Boolean,

    @Json(name = "message") val message: String
)

@JsonClass(generateAdapter = true)
data class LoginResult(

    @Json(name = "name") val name: String,

    @Json(name = "userId") val userId: String,

    @Json(name = "token") val token: String
)
