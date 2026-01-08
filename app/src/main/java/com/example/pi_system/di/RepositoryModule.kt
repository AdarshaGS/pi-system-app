package com.example.pi_system.di

import com.example.pi_system.data.repository.AuthRepositoryImpl
import com.example.pi_system.data.repository.NetWorthRepositoryImpl
import com.example.pi_system.data.repository.PortfolioRepositoryImpl
import com.example.pi_system.domain.repository.AuthRepository
import com.example.pi_system.domain.repository.NetWorthRepository
import com.example.pi_system.domain.repository.PortfolioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPortfolioRepository(
        portfolioRepositoryImpl: PortfolioRepositoryImpl
    ): PortfolioRepository

    @Binds
    @Singleton
    abstract fun bindNetWorthRepository(
        netWorthRepositoryImpl: NetWorthRepositoryImpl
    ): NetWorthRepository

}

