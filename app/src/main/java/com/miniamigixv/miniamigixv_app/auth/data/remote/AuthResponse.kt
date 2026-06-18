package com.miniamigixv.miniamigixv_app.auth.data.remote

data class AuthResponse(
    val token: String,
    val user: UserInfo
)

data class UserInfo(
    val id: Int,
    val email: String
)

data class AuthErrorResponse(
    val error: String
)
