package com.example.draw_android

data class MapTuBean(
    val features: List<MapTuFeature>,
    val type: String
)

data class MapTuFeature(
    val bbox: List<Double>,
    val geometry: MapTuGeometry,
    val id: Int,
    val properties: MapTuProperties,
    val type: String
)

data class MapTuGeometry(
    val coordinates: List<List<Double>>,
    val type: String
)

data class MapTuProperties(
    val _draw_type: String
)