package com.example.pi_system.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUserId(userId: Long) {
        prefs.edit().putLong(KEY_USER_ID, userId).apply()
        android.util.Log.d("UserPreferences", "Saved userId: $userId")
    }

    fun getUserId(): Long {
        val userId = prefs.getLong(KEY_USER_ID, -1L)
        android.util.Log.d("UserPreferences", "Retrieved userId: $userId")
        return userId
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
        android.util.Log.d("UserPreferences", "Saved token: ${token.take(20)}...")
    }

    fun getToken(): String? {
        val token = prefs.getString(KEY_TOKEN, null)
        android.util.Log.d("UserPreferences", "Retrieved token: ${token?.take(20) ?: "null"}")
        return token
    }

    fun saveName(name: String) {
        prefs.edit().putString(KEY_NAME, name).apply()
        android.util.Log.d("UserPreferences", "Saved name: $name")
    }

    fun getName(): String? {
        val name = prefs.getString(KEY_NAME, null)
        android.util.Log.d("UserPreferences", "Retrieved name: $name")
        return name
    }

    fun saveEmail(email: String) {
        prefs.edit().putString(KEY_EMAIL, email).apply()
        android.util.Log.d("UserPreferences", "Saved email: $email")
    }

    fun getEmail(): String? {
        val email = prefs.getString(KEY_EMAIL, null)
        android.util.Log.d("UserPreferences", "Retrieved email: $email")
        return email
    }

    fun clearUserId() {
        prefs.edit().remove(KEY_USER_ID).apply()
        android.util.Log.d("UserPreferences", "Cleared userId")
    }

    fun clearAll() {
        prefs.edit().clear().apply()
        android.util.Log.d("UserPreferences", "Cleared all preferences")
    }

    fun isLoggedIn(): Boolean {
        return getUserId() != -1L && getToken() != null
    }

    companion object {
        private const val PREFS_NAME = "pi_system_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_NAME = "user_name"
        private const val KEY_EMAIL = "user_email"
    }
}

