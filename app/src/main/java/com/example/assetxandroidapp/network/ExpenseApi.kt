package com.example.assetxandroidapp.network

import com.example.assetxandroidapp.data.Expense
import retrofit2.http.GET

interface ExpenseApi {
    @GET("api/Expense")
    suspend fun getExpenses(): List<Expense>
}