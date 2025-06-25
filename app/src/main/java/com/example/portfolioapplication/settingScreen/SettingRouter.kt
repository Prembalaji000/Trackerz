package com.example.portfolioapplication.settingScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.loginScreen.sharedPreference

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RestrictedApi", "UnrememberedMutableState")
@Composable
fun SettingRouter(
    viewModel: SettingViewModel,
    navController: NavController,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val preference = sharedPreference(LocalContext.current)
    val userEmail = preference.getUserEmailId() ?: ""
    val userName by viewModel.userName.collectAsState()
    val userImageUrl by viewModel.userImage.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var isSuccessfullyUpload by remember { mutableStateOf(false) }

    SettingScreen(
        modifier = modifier,
        onSignOut = {
            navController.navigate(Screens.LoginScreen.route) {
                popUpTo(navController.currentDestination?.id ?: 0) { inclusive = true }
            }
        },
        initialUserName = userName,
        userEmail = userEmail,
        userImageUrl = userImageUrl,
        addButtonClick = { name, uri ->
            if (name.isNotEmpty()) {
                preference.setUserName(name)
            }
            if (uri != null) {
                preference.setUserImageUrl(uri)
            }
            if (name.isNotEmpty() || uri != null) {
                viewModel.refreshProfile(context)
            }
        },
        isRefresh = isRefreshing,
        isDarkMode = viewModel.isDarkMode.collectAsState().value,
        onThemeToggle = { viewModel.toggleTheme() },
        onCloudBackUp = {
            viewModel.backupToCloud(isUploadSuccessful = {
                isSuccessfullyUpload = it
            })
        },
        isUploadSuccess = isSuccessfullyUpload,
    )
}