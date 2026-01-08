package com.example.pi_system.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.pi_system.data.local.UserPreferences
import com.example.pi_system.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private var keepSplashOnScreen = true

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before calling super.onCreate()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen visible for 1 second
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }

        // Launch coroutine to navigate after 1 second
        lifecycleScope.launch {
            delay(1000) // 1 second delay
            keepSplashOnScreen = false
            navigateToNextScreen()
        }
    }

    private fun navigateToNextScreen() {
        // Always navigate to LoginActivity since onboarding is removed
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

