package com.example.pi_system.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pi_system.data.local.UserPreferences
import com.example.pi_system.data.model.AuthResponse
import com.example.pi_system.domain.usecase.LoginUseCase
import com.example.pi_system.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<AuthResponse>>()
    val loginState: LiveData<Resource<AuthResponse>> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            val result = loginUseCase(email, password)

            // Save token and user data on successful login
            if (result is Resource.Success && result.data != null) {
                result.data.token?.let { userPreferences.saveToken(it) }
                result.data.userId?.let { userPreferences.saveUserId(it) }
                result.data.name?.let { userPreferences.saveName(it) }
                result.data.email?.let { userPreferences.saveEmail(it) }

                android.util.Log.d("LoginViewModel", "✅ Token, userId, name, and email saved to preferences")
                android.util.Log.d("LoginViewModel", "Token: ${result.data.token?.take(20)}...")
                android.util.Log.d("LoginViewModel", "UserId: ${result.data.userId}")
                android.util.Log.d("LoginViewModel", "Name: ${result.data.name}")
                android.util.Log.d("LoginViewModel", "Email: ${result.data.email}")
            }

            _loginState.value = result
        }
    }
}

