package com.example.pi_system.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("userId")
    val userId: Long?,

    @SerializedName("token")
    val token: String?,

    @SerializedName("refreshToken")
    val refreshToken: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("mobileNumber")
    val mobileNumber: String?,

    @SerializedName("message")
    val message: String?
)

