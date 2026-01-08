package com.example.pi_system.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pi_system.data.local.ThemePreferences
import com.example.pi_system.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val themePreferences: ThemePreferences,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _userName = MutableStateFlow("Guest User")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("guest@example.com")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    init {
        observeThemePreferences()
        loadUserData()
    }

    private fun observeThemePreferences() {
        viewModelScope.launch {
            themePreferences.isDarkMode.collect { isDark ->
                _isDarkMode.value = isDark
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val name = userPreferences.getName() ?: "Guest User"
            val email = userPreferences.getEmail() ?: "guest@example.com"

            _userName.value = name
            _userEmail.value = email

            android.util.Log.d("ProfileViewModel", "Loaded user data - Name: $name, Email: $email")
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            themePreferences.setDarkMode(enabled)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.clearAll()
        }
    }
}

