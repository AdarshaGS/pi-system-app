package com.example.pi_system.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pi_system.data.local.UserPreferences
import com.example.pi_system.data.model.PortfolioResponse
import com.example.pi_system.domain.usecase.GetPortfolioSummaryUseCase
import com.example.pi_system.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getPortfolioSummaryUseCase: GetPortfolioSummaryUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _portfolioState = MutableLiveData<Resource<PortfolioResponse>>()
    val portfolioState: LiveData<Resource<PortfolioResponse>> = _portfolioState

    init {
        loadPortfolioSummary()
    }

    fun loadPortfolioSummary() {
        viewModelScope.launch {
            _portfolioState.value = Resource.Loading()

            val userId = userPreferences.getUserId()
            android.util.Log.d("PortfolioViewModel", "Loading portfolio for userId: $userId")

            if (userId == -1L) {
                _portfolioState.value = Resource.Error("User not logged in")
                return@launch
            }

            val result = getPortfolioSummaryUseCase(userId)
            _portfolioState.value = result

            if (result is Resource.Success) {
                android.util.Log.d("PortfolioViewModel", "✅ Portfolio loaded successfully")
            } else if (result is Resource.Error) {
                android.util.Log.e("PortfolioViewModel", "❌ Failed to load portfolio: ${result.message}")
            }
        }
    }
}

