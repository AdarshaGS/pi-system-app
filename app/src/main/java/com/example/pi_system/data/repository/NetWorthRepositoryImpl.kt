package com.example.pi_system.data.repository

import com.example.pi_system.data.model.NetWorthResponse
import com.example.pi_system.data.remote.NetWorthApiService
import com.example.pi_system.domain.repository.NetWorthRepository
import com.example.pi_system.domain.util.Resource
import javax.inject.Inject

class NetWorthRepositoryImpl @Inject constructor(
    private val apiService: NetWorthApiService
) : NetWorthRepository {

    override suspend fun getNetWorth(userId: Long): Resource<NetWorthResponse> {
        return try {
            android.util.Log.d("NetWorthRepositoryImpl", "========== NET WORTH REQUEST ==========")
            android.util.Log.d("NetWorthRepositoryImpl", "UserId: $userId")

            val response = apiService.getNetWorth(userId)
            android.util.Log.d("NetWorthRepositoryImpl", "Response code: ${response.code()}")
            android.util.Log.d("NetWorthRepositoryImpl", "Response successful: ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val responseBody = response.body()!!
                android.util.Log.d("NetWorthRepositoryImpl", "✅ Net worth retrieved successfully")
                android.util.Log.d("NetWorthRepositoryImpl", "Net Worth: ${responseBody.netWorth}")
                android.util.Log.d("NetWorthRepositoryImpl", "Total Assets: ${responseBody.totalAssets}")
                android.util.Log.d("NetWorthRepositoryImpl", "Total Liabilities: ${responseBody.totalLiabilities}")
                android.util.Log.d("NetWorthRepositoryImpl", "======================================")
                Resource.Success(responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("NetWorthRepositoryImpl", "❌ Net worth fetch failed")
                android.util.Log.e("NetWorthRepositoryImpl", "Error body: $errorBody")

                val errorMessage = errorBody ?: "Failed to fetch net worth"
                android.util.Log.d("NetWorthRepositoryImpl", "======================================")
                Resource.Error(errorMessage)
            }
        } catch (e: Exception) {
            android.util.Log.e("NetWorthRepositoryImpl", "❌ Exception during net worth fetch", e)
            e.printStackTrace()
            android.util.Log.d("NetWorthRepositoryImpl", "======================================")
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}

