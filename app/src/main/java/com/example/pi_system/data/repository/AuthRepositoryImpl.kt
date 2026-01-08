package com.example.pi_system.data.repository

import com.example.pi_system.data.model.AuthResponse
import com.example.pi_system.data.model.LoginRequest
import com.example.pi_system.data.model.RegisterRequest
import com.example.pi_system.data.remote.AuthApiService
import com.example.pi_system.domain.repository.AuthRepository
import com.example.pi_system.domain.util.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService
) : AuthRepository {

    override suspend fun register(request: RegisterRequest): Resource<AuthResponse> {
        return try {
            android.util.Log.d("AuthRepositoryImpl", "========== REGISTER REQUEST ==========")
            android.util.Log.d("AuthRepositoryImpl", "Name: ${request.name}")
            android.util.Log.d("AuthRepositoryImpl", "Email: ${request.email}")
            android.util.Log.d("AuthRepositoryImpl", "Mobile: ${request.mobileNumber}")
            android.util.Log.d("AuthRepositoryImpl", "Password length: ${request.password.length}")

            val response = apiService.register(request)
            android.util.Log.d("AuthRepositoryImpl", "Response code: ${response.code()}")
            android.util.Log.d("AuthRepositoryImpl", "Response successful: ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val responseBody = response.body()!!
                android.util.Log.d("AuthRepositoryImpl", "✅ Registration successful")
                android.util.Log.d("AuthRepositoryImpl", "UserId: ${responseBody.userId}")
                android.util.Log.d("AuthRepositoryImpl", "Email: ${responseBody.email}")
                android.util.Log.d("AuthRepositoryImpl", "Name: ${responseBody.name}")
                android.util.Log.d("AuthRepositoryImpl", "Message: ${responseBody.message}")
                android.util.Log.d("AuthRepositoryImpl", "======================================")
                Resource.Success(responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("AuthRepositoryImpl", "❌ Registration failed")
                android.util.Log.e("AuthRepositoryImpl", "Error body: $errorBody")

                // Try to parse error body as AuthResponse to get the message
                val errorMessage = try {
                    if (errorBody != null) {
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(errorBody, AuthResponse::class.java)
                        errorResponse.message ?: "Registration failed"
                    } else {
                        response.message() ?: "Registration failed"
                    }
                } catch (e: Exception) {
                    response.message() ?: "Registration failed"
                }

                android.util.Log.d("AuthRepositoryImpl", "======================================")
                Resource.Error(errorMessage)
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepositoryImpl", "❌ Exception during registration", e)
            e.printStackTrace()
            android.util.Log.d("AuthRepositoryImpl", "======================================")
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun login(request: LoginRequest): Resource<AuthResponse> {
        return try {
            android.util.Log.d("AuthRepositoryImpl", "========== LOGIN REQUEST ==========")
            android.util.Log.d("AuthRepositoryImpl", "Email: ${request.email}")
            android.util.Log.d("AuthRepositoryImpl", "Password length: ${request.password.length}")

            val response = apiService.login(request)
            android.util.Log.d("AuthRepositoryImpl", "Response code: ${response.code()}")
            android.util.Log.d("AuthRepositoryImpl", "Response successful: ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val responseBody = response.body()!!
                android.util.Log.d("AuthRepositoryImpl", "✅ Login successful")
                android.util.Log.d("AuthRepositoryImpl", "Token: ${responseBody.token?.take(20) ?: "null"}")
                android.util.Log.d("AuthRepositoryImpl", "UserId: ${responseBody.userId}")
                android.util.Log.d("AuthRepositoryImpl", "Email: ${responseBody.email}")
                android.util.Log.d("AuthRepositoryImpl", "Name: ${responseBody.name}")
                android.util.Log.d("AuthRepositoryImpl", "======================================")
                Resource.Success(responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("AuthRepositoryImpl", "❌ Login failed")
                android.util.Log.e("AuthRepositoryImpl", "Response code: ${response.code()}")
                android.util.Log.e("AuthRepositoryImpl", "Error body: $errorBody")

                // Try to parse error body as AuthResponse to get the message
                val errorMessage = try {
                    if (errorBody != null) {
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(errorBody, AuthResponse::class.java)
                        errorResponse.message ?: "Login failed"
                    } else {
                        "Login failed"
                    }
                } catch (e: Exception) {
                    android.util.Log.e("AuthRepositoryImpl", "Failed to parse error body", e)
                    "Invalid email or password"
                }

                android.util.Log.e("AuthRepositoryImpl", "Error message: $errorMessage")
                android.util.Log.d("AuthRepositoryImpl", "======================================")
                Resource.Error(errorMessage)
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepositoryImpl", "❌ Exception during login", e)
            e.printStackTrace()
            android.util.Log.d("AuthRepositoryImpl", "======================================")
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}

