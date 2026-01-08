package com.example.pi_system.domain.repository

import com.example.pi_system.data.model.NetWorthResponse
import com.example.pi_system.domain.util.Resource

interface NetWorthRepository {
    suspend fun getNetWorth(userId: Long): Resource<NetWorthResponse>
}

