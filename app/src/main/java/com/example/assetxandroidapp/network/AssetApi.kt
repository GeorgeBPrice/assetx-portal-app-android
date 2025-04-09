package com.example.assetxandroidapp.network

import com.example.assetxandroidapp.data.Asset
import retrofit2.http.GET

interface AssetApi {
    @GET("api/asset/complex")
    suspend fun getAssets(): List<Asset> // No parameters

//    @GET("api/Asset/[{id}")
//    suspend fun getAssets(@Query("id") id: String): List<Asset>
}