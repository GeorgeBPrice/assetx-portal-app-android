package com.example.assetxandroidapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assetxandroidapp.network.AssetApi
import com.example.assetxandroidapp.network.ExpenseApi
import com.example.assetxandroidapp.network.RetrofitInstance

/**
 * Factory for creating a DashboardViewModel with the necessary dependencies
 */
class DashboardViewModelFactory(
    private val assetApi: AssetApi = RetrofitInstance.assetApi,
    private val expenseApi: ExpenseApi = RetrofitInstance.expenseApi
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(assetApi, expenseApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}