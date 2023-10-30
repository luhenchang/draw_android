mapOf(
    Pair("minSdkVer", 22),
    Pair("targetSdkVer", 25),
    Pair("compiledSdkVer", 25),
    Pair("buildToolsVer", "26-rc4"),
    Pair("gradleVersion","7.4.2"),
    Pair("composeVersion","1.4.2")
).entries.forEach {
    project.extra.set(it.key, it.value)
}

//ext {
//    androids = [
//            compileSdkVersion: 33,
//            minSdkVersion    : 21,
//            targetSdkVersion : 33,
//            versionCode      : 1,
//            versionName      : "1.0"
//    ]
//    version = [
//
//    ]
//
//    library = [
//            room_version: '2.4.3',
//            arch_version: '1.1.1'
//
//    ]
//}