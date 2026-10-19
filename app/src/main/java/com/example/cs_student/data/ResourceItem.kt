package com.example.cs_student.data

data class ResourceItem(
    val semester: String = "",
    val type: String = "" ,
    val url: String = "",
    val title: Map<String, String> = emptyMap()
)
