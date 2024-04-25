data class MapPoint(
    val features: List<Feature>,
    val type: String
)

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)

data class Geometry(
    val coordinates: List<List<List<List<Double>>>>,
    val type: String
)

data class Properties(
    val acroutes: List<Int>,
    val adchar: String,
    val adcode: Int,
    val center: List<Double>,
    val centroid: List<Double>,
    val childrenNum: Int,
    val level: String,
    val name: String,
    val parent: Parent,
    val subFeatureIndex: Int
)

data class Parent(
    val adcode: Int
)