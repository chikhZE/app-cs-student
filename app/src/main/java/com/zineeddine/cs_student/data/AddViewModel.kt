package com.zineeddine.cs_student.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AddContentViewModel : ViewModel() {
    private val repository = AdminContentRepository()

    var date by mutableStateOf("")
    var newsTitleEn by mutableStateOf("")
    var newsTitleFr by mutableStateOf("")
    var descEn by mutableStateOf("")
    var descFr by mutableStateOf("")
    var tagEn by mutableStateOf("")
    var tagFr by mutableStateOf("")

    // Resource fields
    var resTitleEn by mutableStateOf("")
    var resTitleFr by mutableStateOf("")
    var semester by mutableStateOf("")
    var url by mutableStateOf("")

    // Schedule fields
    var scheduleTime by mutableStateOf("")

    fun clearForm() {
        date = ""
        newsTitleEn = ""
        newsTitleFr = ""
        descEn = ""
        descFr = ""
        tagEn = ""
        tagFr = ""
        resTitleEn = ""
        resTitleFr = ""
        semester = ""
        url = ""
        scheduleTime = ""
    }

    fun handlePostSubmission(
        contentType: String,
        moduleCode: String,
        selectedType: String,
        selectedDay: String = ""
    ) {
        viewModelScope.launch {
            when (contentType) {
                "News" -> {
                    repository.addNews(
                        titleEn = newsTitleEn,
                        titleFr = newsTitleFr,
                        descEn = descEn,
                        descFr = descFr,
                        tagEn = tagEn,
                        tagFr = tagFr,
                        date = date
                    )
                }
                "Res" -> {
                    repository.addResource(
                        moduleCode = moduleCode,
                        semester = semester,
                        titleEn = resTitleEn,
                        titleFr = resTitleFr,
                        url = url,
                        type = selectedType
                    )
                }
                "Schedule" -> {
                    repository.addSchedule(
                        subjectRes = moduleCode,
                        dayRes = selectedDay,
                        time = scheduleTime,
                        typeRes = selectedType
                    )
                }
            }
        }
    }
}
