package com.example.pi_system.domain.usecase

import com.example.pi_system.data.model.AuthResponse
import com.example.pi_system.data.model.RegisterRequest
import com.example.pi_system.domain.repository.AuthRepository
import com.example.pi_system.domain.util.Resource
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, mobileNumber:String, password: String): Resource<AuthResponse> {
        if (name.isBlank()) {
            return Resource.Error("Name cannot be empty")
        }
        if (email.isBlank()) {
            return Resource.Error("Email cannot be empty")
        }
        if (password.isBlank()) {
            return Resource.Error("Password cannot be empty")
        }

        val request = RegisterRequest(name, email, mobileNumber , password)
        return repository.register(request)
    }
}

