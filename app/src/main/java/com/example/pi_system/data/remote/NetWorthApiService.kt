package com.example.pi_system.data.remote

import com.example.pi_system.data.model.NetWorthResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NetWorthApiService {

    @GET("api/v1/net-worth/{userId}")
    suspend fun getNetWorth(@Path("userId") userId: Long): Response<NetWorthResponse>
}

