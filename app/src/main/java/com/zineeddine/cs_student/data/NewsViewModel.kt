package com.zineeddine.cs_student.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsDao = AppDatabase.getDatabase(application).newsDao()
    private val repository = NewsRepository(newsDao)

    val newsList: StateFlow<List<NewsEntity>> = repository.allNews.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        refreshNewsData()
    }

    fun refreshNewsData() {
        viewModelScope.launch {
            repository.refreshNews()
        }
    }
}
