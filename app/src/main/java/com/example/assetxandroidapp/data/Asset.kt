package com.example.assetxandroidapp.data

data class Asset(
    val assetId: Int,
    val assetName: String,
    val assetType: String,
    val assetTypeId: Int,
    val brand: String,
    val model: String,
    val personnel: String,
    val location: String,
    val serialNumber: String,
    val imei: String?,
    val simCode: String?,
    val mobileNumber: String?,
    val provider: String?,
    val plan: String?,
    val planType: String?,
    val purchaseDate: String,
    val purchaseInvoice: String,
    val warrantyExpiry: String,
    val assetValue: Double,
    val status: String,
    val createdAt: String,
    val createdBy: String,
    val updatedAt: String,
    val updatedBy: String,
    val associations: List<Association>,
    val assetAccessories: List<Any> // Define a specific type if needed
)

data class Association(
    val associationId: Int,
    val associationType: String,
    val referenceId: Int,
    val referenceName: String,
    val startDate: String,
    val endDate: String?
)