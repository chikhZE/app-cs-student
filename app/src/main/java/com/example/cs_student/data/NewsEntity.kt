package com.example.cs_student.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class NewsEntity(
    @PrimaryKey
    val id: String,
    val color: String,
    val date: String,
    val titleEn: String,
    val titleFr: String,
    val descriptionEn: String,
    val descriptionFr: String,
    val tagEn: String,
    val tagFr: String
)