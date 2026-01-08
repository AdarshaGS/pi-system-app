package com.example.pi_system.presentation.networth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pi_system.data.local.UserPreferences
import com.example.pi_system.data.model.NetWorthResponse
import com.example.pi_system.domain.usecase.GetNetWorthUseCase
import com.example.pi_system.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetWorthViewModel @Inject constructor(
    private val getNetWorthUseCase: GetNetWorthUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _netWorthState = MutableLiveData<Resource<NetWorthResponse>>()
    val netWorthState: LiveData<Resource<NetWorthResponse>> = _netWorthState

    init {
        loadNetWorth()
    }

    fun loadNetWorth() {
        viewModelScope.launch {
            _netWorthState.value = Resource.Loading()

            val userId = userPreferences.getUserId()
            android.util.Log.d("NetWorthViewModel", "Loading net worth for userId: $userId")

            if (userId == -1L) {
                _netWorthState.value = Resource.Error("User not logged in")
                return@launch
            }

            val result = getNetWorthUseCase(userId)
            _netWorthState.value = result

            if (result is Resource.Success) {
                android.util.Log.d("NetWorthViewModel", "✅ Net worth loaded successfully")
            } else if (result is Resource.Error) {
                android.util.Log.e("NetWorthViewModel", "❌ Failed to load net worth: ${result.message}")
            }
        }
    }
}

