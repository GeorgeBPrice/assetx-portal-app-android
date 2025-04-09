package com.example.assetxandroidapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val ASSET_BASE_URL = "http://10.0.2.2:8011/"
    private const val EXPENSE_BASE_URL = "http://10.0.2.2:8002/"

    // Define the logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Logs URL, headers, body, and response
    }

    // Add it to your OkHttpClient
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Configure Retrofit with the client for assetApi
    val assetApi: AssetApi by lazy {
        Retrofit.Builder()
            .baseUrl(ASSET_BASE_URL)
            .client(okHttpClient) // Use the client with logging
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AssetApi::class.java)
    }

    // Configure Retrofit with the client for expenseApi
    val expenseApi: ExpenseApi by lazy {
        Retrofit.Builder()
            .baseUrl(EXPENSE_BASE_URL)
            .client(okHttpClient) // Use the same client with logging
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExpenseApi::class.java)
    }
}