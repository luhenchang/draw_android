package com.example.draw_android.section16_line_chart

data class TaskManagement(
    val id: Int? = null,
    val personDelArr: List<Any>? = null,
    val taskDescribe: String? = null,
    val taskEndTime: String? = null,
    val taskInitiatorPerson: String? = null,
    val taskName: String? = null,
    val taskPersonList: List<TaskManagementPerson>? = null,
    val taskStartTime: String? = null,
    val taskStatus: String? = null,
    val taskTypeArr: String? = null,
    val typeStrArr: List<Any>? = null,
    val userId: Int? = null
)

data class TaskManagementPerson(
    val arr: List<Int>? = null,
    val cityId: Int? = null,
    val departmentName: String? = null,
    val description: String? = null,
    val id: Int? = null,
    val panoramicVideo: String? = null,
    val remark: String? = null,
    val startTime: Int? = null,
    val status: String? = null,
    val taskId: String? = null,
    val taskScore: Int? = null,
    val taskType: String? = null,
    val trapeze: Trapeze? = null,
    val typeNames: String? = null,
    val updateTime: String? = null,
    val userId: Int? = null,
    val userTier: String? = null,
    val username: String? = null
)

data class TaskAddResult(
    val code: Int,
    val mes: String,
    val responseData: TaskAddResponseData?
)
class Trapeze
class TaskAddResponseData