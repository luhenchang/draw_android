package com.example.draw_android

data class MapAreaNameBean(
    val features: List<MapAreaNameFeature>,
    val type: String
)

data class MapAreaNameFeature(
    val bbox: List<Double>,
    val geometry: MNGeometry,
    val id: Int,
    val properties: Properties,
    val type: String
)

data class MNGeometry(
    val coordinates: List<Double>,
    val type: String
)
