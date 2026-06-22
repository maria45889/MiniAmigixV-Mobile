package com.miniamigixv.miniamigixv_app.auth.data.remote

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
