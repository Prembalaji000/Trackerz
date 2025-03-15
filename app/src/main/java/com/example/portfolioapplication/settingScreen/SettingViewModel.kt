package com.example.portfolioapplication.settingScreen

import androidx.lifecycle.ViewModel
import com.example.portfolioapplication.authScreen.LoginCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingViewModel() : ViewModel() {
    private val _settingState = MutableStateFlow(SettingState())
    val settingState = _settingState.asStateFlow()

}

data class SettingState(
    var isLoading: Boolean = false,
)