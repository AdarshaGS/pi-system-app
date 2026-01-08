package com.example.pi_system.data.remote

import com.example.pi_system.data.local.UserPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestUrl = originalRequest.url.toString()

        // Skip authentication for public endpoints
        val isPublicEndpoint = requestUrl.contains("/api/auth/login") ||
                                requestUrl.contains("/api/auth/register")

        if (isPublicEndpoint) {
            android.util.Log.d("AuthInterceptor", "========== REQUEST INTERCEPTOR ==========")
            android.util.Log.d("AuthInterceptor", "Public endpoint detected: $requestUrl")
            android.util.Log.d("AuthInterceptor", "⚠️ Skipping Authorization header (public endpoint)")
            android.util.Log.d("AuthInterceptor", "=========================================")

            return chain.proceed(originalRequest)
        }

        val token = userPreferences.getToken()

        android.util.Log.d("AuthInterceptor", "========== REQUEST INTERCEPTOR ==========")
        android.util.Log.d("AuthInterceptor", "Intercepting request: ${originalRequest.url}")
        android.util.Log.d("AuthInterceptor", "Request method: ${originalRequest.method}")
        android.util.Log.d("AuthInterceptor", "Token available: ${token != null}")

        if (token != null) {
            android.util.Log.d("AuthInterceptor", "Token preview: ${token.take(30)}...")
            android.util.Log.d("AuthInterceptor", "Token length: ${token.length}")
        } else {
            android.util.Log.e("AuthInterceptor", "❌ NO TOKEN FOUND IN PREFERENCES!")
            android.util.Log.e("AuthInterceptor", "❌ API call will likely fail with 401/403")
        }

        val newRequest = if (token != null) {
            android.util.Log.d("AuthInterceptor", "✅ Adding Authorization header with Bearer token")
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            android.util.Log.w("AuthInterceptor", "⚠️ Proceeding without Authorization header")
            originalRequest
        }

        // Log all headers being sent
        android.util.Log.d("AuthInterceptor", "Request headers:")
        newRequest.headers.forEach { (name, value) ->
            if (name.equals("Authorization", ignoreCase = true)) {
                android.util.Log.d("AuthInterceptor", "  $name: Bearer ${value.substring(7).take(20)}...")
            } else {
                android.util.Log.d("AuthInterceptor", "  $name: $value")
            }
        }
        android.util.Log.d("AuthInterceptor", "=========================================")

        val response = chain.proceed(newRequest)

        // Log response code
        android.util.Log.d("AuthInterceptor", "Response code: ${response.code}")
        if (response.code == 401 || response.code == 403) {
            android.util.Log.e("AuthInterceptor", "❌ AUTH FAILED! Response code: ${response.code}")
            android.util.Log.e("AuthInterceptor", "This means the token is missing, invalid, or expired")
        }

        return response
    }
}

