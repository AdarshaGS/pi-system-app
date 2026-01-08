package com.example.pi_system.domain.usecase

import com.example.pi_system.data.model.AuthResponse
import com.example.pi_system.data.model.LoginRequest
import com.example.pi_system.domain.repository.AuthRepository
import com.example.pi_system.domain.util.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<AuthResponse> {
        if (email.isBlank()) {
            return Resource.Error("Email cannot be empty")
        }
        if (password.isBlank()) {
            return Resource.Error("Password cannot be empty")
        }

        val request = LoginRequest(email, password)
        return repository.login(request)
    }
}

