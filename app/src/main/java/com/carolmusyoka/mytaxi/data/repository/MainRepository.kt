package com.carolmusyoka.mytaxi.data.repository

import com.carolmusyoka.mytaxi.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getVehicles(p1Lat: Double, p1Lon: Double, p2Lat: Double, p2Lon: Double) = apiHelper.getVehicles(
        p1Lat, p1Lon, p2Lat, p2Lon
    )
}