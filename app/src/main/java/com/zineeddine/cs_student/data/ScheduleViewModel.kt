package com.zineeddine.cs_student.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val scheduleDao = AppDatabase.getDatabase(application).scheduleDao()
    private val repository = ScheduleRepository(scheduleDao)

    val scheduleList: StateFlow<List<ScheduleEntity>> = repository.allSchedule.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        refreshSchedule()
    }

    fun refreshSchedule() {
        viewModelScope.launch {
            repository.refreshSchedule()
        }
    }

    fun deleteSchedule(itemToDelete: ScheduleEntity) {
        viewModelScope.launch {
            repository.deleteSchedule(itemToDelete)
        }
    }
}

