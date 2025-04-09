package com.example.assetxandroidapp.data

data class Expense(
    val expenseId: Int,
    val expenseTypeId: Int?,
    val vendorId: Int?,
    val assetId: Int?,
    val subscriptionId: Int?,
    val amount: Double?,
    val currencyCode: String?,
    val dateIncurred: String?,
    val paymentStatus: String?,
    val paymentDate: String?,
    val budgetAllocationId: Int?,
    val description: String?,
    val createdAt: String?,
    val createdBy: String?,
    val updatedAt: String?,
    val updatedBy: String?
)