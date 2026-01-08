package com.example.pi_system.domain.repository

import com.example.pi_system.data.model.PortfolioResponse
import com.example.pi_system.domain.util.Resource

interface PortfolioRepository {
    suspend fun getPortfolioSummary(userId: Long): Resource<PortfolioResponse>
}

