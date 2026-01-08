package com.example.pi_system.data.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val mobileNumber: String,
    val password: String,
)
