package com.example.pi_system.domain.repository

import com.example.pi_system.data.model.AuthResponse
import com.example.pi_system.data.model.LoginRequest
import com.example.pi_system.data.model.RegisterRequest
import com.example.pi_system.domain.util.Resource

interface AuthRepository {
    suspend fun register(request: RegisterRequest): Resource<AuthResponse>
    suspend fun login(request: LoginRequest): Resource<AuthResponse>
}

