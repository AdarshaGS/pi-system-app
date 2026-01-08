package com.example.pi_system.presentation.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.pi_system.MainActivity
import com.example.pi_system.presentation.login.LoginActivity
import com.example.pi_system.presentation.register.compose.RegisterScreen
import com.example.pi_system.presentation.theme.PISystemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : ComponentActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PISystemTheme {
                RegisterScreen(
                    viewModel = viewModel,
                    onRegisterSuccess = { navigateToMain() },
                    onNavigateToLogin = { navigateToLogin() }
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

    private fun navigateToLogin() {
        finish()
    }
}

