package com.example.pi_system.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.pi_system.MainActivity
import com.example.pi_system.data.local.UserPreferences
import com.example.pi_system.presentation.login.compose.LoginScreen
import com.example.pi_system.presentation.register.RegisterActivity
import com.example.pi_system.presentation.theme.PISystemTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PISystemTheme {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = { navigateToMain() },
                    onNavigateToRegister = { navigateToRegister() },
                )
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

}

