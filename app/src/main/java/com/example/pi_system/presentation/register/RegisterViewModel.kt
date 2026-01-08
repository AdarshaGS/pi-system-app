package com.example.pi_system.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pi_system.data.local.UserPreferences
import com.example.pi_system.data.model.AuthResponse
import com.example.pi_system.domain.usecase.RegisterUseCase
import com.example.pi_system.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _registerState = MutableLiveData<Resource<AuthResponse>>()
    val registerState: LiveData<Resource<AuthResponse>> = _registerState

    fun register(name: String, email: String, mobileNumber: String, password: String) {
        viewModelScope.launch {
            _registerState.value = Resource.Loading()
            val result = registerUseCase(name, email, mobileNumber, password)

            // Save token and user data on successful registration
            if (result is Resource.Success && result.data != null) {
                result.data.token?.let { userPreferences.saveToken(it) }
                result.data.userId?.let { userPreferences.saveUserId(it) }
                result.data.name?.let { userPreferences.saveName(it) }
                result.data.email?.let { userPreferences.saveEmail(it) }

                android.util.Log.d("RegisterViewModel", "✅ Token, userId, name, and email saved to preferences")
                android.util.Log.d("RegisterViewModel", "Token: ${result.data.token?.take(20)}...")
                android.util.Log.d("RegisterViewModel", "UserId: ${result.data.userId}")
                android.util.Log.d("RegisterViewModel", "Name: ${result.data.name}")
                android.util.Log.d("RegisterViewModel", "Email: ${result.data.email}")
            }

            _registerState.value = result
        }
    }
}

