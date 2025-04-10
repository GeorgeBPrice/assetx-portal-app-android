package com.example.assetxandroidapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetxandroidapp.data.Asset
import com.example.assetxandroidapp.data.Expense
import com.example.assetxandroidapp.network.AssetApi
import com.example.assetxandroidapp.network.ExpenseApi
import com.example.assetxandroidapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(assetApi: AssetApi, expenseApi: ExpenseApi) : ViewModel() {
    private val _assets = MutableStateFlow<List<Asset>>(emptyList())
    val assets: StateFlow<List<Asset>> = _assets

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            // Fetch Assets from the API
            try {
                Log.d("DashboardViewModel", "Fetching Assets...")
                val assetList = RetrofitInstance.assetApi.getAssets()
                _assets.value = assetList
                Log.d("DashboardViewModel", "Assets loaded: ${assetList.size}")
                if (assetList.isNotEmpty()) {
                    Log.d("DashboardViewModel", "First asset: ${assetList[0]}")
                }
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error fetching data: ${e.message}", e)
            }

            // Fetch Expenses from the API
            try {
                Log.d("DashboardViewModel", "Fetching Expenses...")
                val expenseList = RetrofitInstance.expenseApi.getExpenses()
                _expenses.value = expenseList
                Log.d("DashboardViewModel", "Expenses loaded: ${expenseList.size}")
                if (expenseList.isNotEmpty()) {
                    Log.d("DashboardViewModel", "First expense: ${expenseList[0]}")
                }
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error fetching data: ${e.message}", e)
            }
        }
    }
}