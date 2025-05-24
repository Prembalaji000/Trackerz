package com.example.portfolioapplication.settingScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolioapplication.authScreen.LoginCredential
import com.example.portfolioapplication.loginScreen.sharedPreference
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingViewModel(context: Context) : ViewModel() {
    private val _settingState = MutableStateFlow(SettingState())
    val settingState = _settingState.asStateFlow()
    val preference = sharedPreference(context)

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userImage = MutableStateFlow("")
    val userImage: StateFlow<String> = _userImage

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    val userNameIn = preference.getUserName()
    val userImageIn = preference.getUserImageUrl()

    init {
        _userName.value = userNameIn ?: ""
        _userImage.value = userImageIn ?: ""
    }

    fun refreshProfile(context: Context) {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(2000)
            val preference = sharedPreference(context)
            _userName.value = preference.getUserName() ?: ""
            _userImage.value = preference.getUserImageUrl() ?: ""
            _isRefreshing.value = false
        }
    }

    private val _isDarkMode = MutableStateFlow(preference.isDarkModeEnabled())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    fun toggleTheme() {
        val newMode = !_isDarkMode.value
        _isDarkMode.value = newMode
        preference.setDarkModeEnabled(newMode)
    }


}

data class SettingState(
    var isLoading: Boolean = false,
)