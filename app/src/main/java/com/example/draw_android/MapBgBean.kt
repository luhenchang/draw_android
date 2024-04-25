package com.example.draw_android

data class MapBgBean(
    val features: List<MapBgFeature>,
    val type: String
)

data class MapBgFeature(
    val bbox: List<Double>,
    val geometry: MapBgGeometry,
    val id: Int,
    val properties: MapBgProperties,
    val type: String
)

data class MapBgGeometry(
    val coordinates: List<Double>,
    val type: String
)

data class MapBgProperties(
    val _draw_type: String
)