package com.example.draw_android.section16_line_chart

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
    // 保留值为 null 的属性
    configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true)
}
