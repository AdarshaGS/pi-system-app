package com.example.pi_system.data.repository

import com.example.pi_system.data.model.PortfolioResponse
import com.example.pi_system.data.remote.PortfolioApiService
import com.example.pi_system.domain.repository.PortfolioRepository
import com.example.pi_system.domain.util.Resource
import javax.inject.Inject

class PortfolioRepositoryImpl @Inject constructor(
    private val apiService: PortfolioApiService
) : PortfolioRepository {

    override suspend fun getPortfolioSummary(userId: Long): Resource<PortfolioResponse> {
        return try {
            android.util.Log.d("PortfolioRepositoryImpl", "========== PORTFOLIO SUMMARY REQUEST ==========")
            android.util.Log.d("PortfolioRepositoryImpl", "UserId: $userId")

            val response = apiService.getPortfolioSummary(userId)
            android.util.Log.d("PortfolioRepositoryImpl", "Response code: ${response.code()}")
            android.util.Log.d("PortfolioRepositoryImpl", "Response successful: ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val responseBody = response.body()!!
                android.util.Log.d("PortfolioRepositoryImpl", "✅ Portfolio summary retrieved successfully")
                android.util.Log.d("PortfolioRepositoryImpl", "Score: ${responseBody.score}")
                android.util.Log.d("PortfolioRepositoryImpl", "Total Investment: ${responseBody.totalInvestment}")
                android.util.Log.d("PortfolioRepositoryImpl", "Current Value: ${responseBody.currentValue}")
                android.util.Log.d("PortfolioRepositoryImpl", "======================================")
                Resource.Success(responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("PortfolioRepositoryImpl", "❌ Portfolio summary failed")
                android.util.Log.e("PortfolioRepositoryImpl", "Error body: $errorBody")

                val errorMessage = errorBody ?: "Failed to fetch portfolio summary"
                android.util.Log.d("PortfolioRepositoryImpl", "======================================")
                Resource.Error(errorMessage)
            }
        } catch (e: Exception) {
            android.util.Log.e("PortfolioRepositoryImpl", "❌ Exception during portfolio summary fetch", e)
            e.printStackTrace()
            android.util.Log.d("PortfolioRepositoryImpl", "======================================")
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}

