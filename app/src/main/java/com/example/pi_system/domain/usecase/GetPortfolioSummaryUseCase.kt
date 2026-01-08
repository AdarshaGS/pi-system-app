package com.example.pi_system.domain.usecase

import com.example.pi_system.data.model.PortfolioResponse
import com.example.pi_system.domain.repository.PortfolioRepository
import com.example.pi_system.domain.util.Resource
import javax.inject.Inject

class GetPortfolioSummaryUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    suspend operator fun invoke(userId: Long): Resource<PortfolioResponse> {
        return repository.getPortfolioSummary(userId)
    }
}

