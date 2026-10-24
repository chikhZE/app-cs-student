package com.zineeddine.cs_student.data

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zineeddine.cs_student.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val adminRepository = AdminRepository()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    init {
        checkAdminStatus()
    }

    private fun checkAdminStatus() {
        viewModelScope.launch {
            _isAdmin.value = adminRepository.checkIfCurrentUserIsAdmin()
        }
    }



    val android.content.Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_prefs")
    private val dataStore = application.dataStore

    val currentTheme: StateFlow<AppTheme> = dataStore.data
        .map { preferences ->
            val themeName = preferences[THEME_KEY] ?: AppTheme.GREEN.name
            AppTheme.valueOf(themeName)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.GREEN
        )

    fun changeTheme(newTheme: AppTheme) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[THEME_KEY] = newTheme.name
            }
        }
    }

    companion object {
        private val THEME_KEY = stringPreferencesKey("app_theme")
    }
}
