package com.example.draw_android

data class TestDemo(
    val code: String,
    val `data`: Data,
    val message: String,
    val messageId: String,
    val version: String
)

data class Data(
    val dataList: List<DataX>,
    val page: Page
)

data class DataX(
    val fieldValues: List<FieldValue>
)

data class Page(
    val pageNo: Int,
    val pageSize: Int,
    val total: Int
)

data class FieldValue(
    val `field`: String,
    val value: String
)