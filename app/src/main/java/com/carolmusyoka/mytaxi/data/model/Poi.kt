package com.carolmusyoka.mytaxi.data.model

data class Poi(
    val coordinate: Coordinate,
    val fleetType: String,
    val heading: Double,
    val id: Int
)