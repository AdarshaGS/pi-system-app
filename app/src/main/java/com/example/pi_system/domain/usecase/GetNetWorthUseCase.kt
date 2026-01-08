package com.example.pi_system.domain.usecase

import com.example.pi_system.data.model.NetWorthResponse
import com.example.pi_system.domain.repository.NetWorthRepository
import com.example.pi_system.domain.util.Resource
import javax.inject.Inject

class GetNetWorthUseCase @Inject constructor(
    private val repository: NetWorthRepository
) {
    suspend operator fun invoke(userId: Long): Resource<NetWorthResponse> {
        return repository.getNetWorth(userId)
    }
}

