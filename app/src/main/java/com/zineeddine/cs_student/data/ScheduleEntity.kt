package com.zineeddine.cs_student.data

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_table")
data class ScheduleEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subjectRes: String,
    val typeRes: String,
    val dayRes: String,
    val time: String,

)