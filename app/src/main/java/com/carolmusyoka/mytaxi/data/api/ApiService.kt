package com.carolmusyoka.mytaxi.data.api

import com.carolmusyoka.mytaxi.data.model.MyTaxiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(" ")
    suspend fun getVehicles(
        @Query("p1Lat") p1Lat: Double,
        @Query("p1Lon") p1Lon: Double,
        @Query("p2Lat") p2Lat: Double,
        @Query("p2Lon") p2Lon: Double
    ): MyTaxiResponse
}