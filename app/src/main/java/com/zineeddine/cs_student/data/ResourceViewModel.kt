package com.zineeddine.cs_student.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResourcesListViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = ResourceRepository()
    private val moduleCode: String = savedStateHandle["moduleCode"] ?: ""
    private val contentType: String = savedStateHandle["contentType"] ?: ""

    private val _resources = MutableStateFlow<List<ResourceItem>>(emptyList())
    val resources: StateFlow<List<ResourceItem>> = _resources

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadResources()
    }

     fun loadResources() {
        viewModelScope.launch {
            _isLoading.value = true
            _resources.value = repository.getResources(moduleCode, contentType)
            delay(500)
            _isLoading.value = false
        }
    }

    fun deleteResources(itemToDelete: ResourceItem) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteResource(moduleCode, itemToDelete)
            loadResources()
        }
    }
}
