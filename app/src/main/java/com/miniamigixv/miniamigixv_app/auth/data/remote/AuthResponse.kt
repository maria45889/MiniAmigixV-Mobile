package com.miniamigixv.miniamigixv_app.auth.data.remote

data class AuthResponse(
    val access: String,
    val refresh: String? = null
)

data class AuthErrorResponse(
    val error: String
)
