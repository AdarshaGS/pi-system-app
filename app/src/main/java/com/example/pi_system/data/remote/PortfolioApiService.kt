package com.example.pi_system.data.remote

import com.example.pi_system.data.model.PortfolioResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PortfolioApiService {

    @GET("api/v1/portfolio/summary/{userId}")
    suspend fun getPortfolioSummary(@Path("userId") userId: Long): Response<PortfolioResponse>
}

