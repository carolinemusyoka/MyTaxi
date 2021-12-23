package com.carolmusyoka.mytaxi.data.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getVehicles(p1Lat: Double, p1Lon: Double, p2Lat: Double, p2Lon: Double) = apiService.getVehicles(
        p1Lat, p1Lon, p2Lat, p2Lon
    )
}