package com.example.portfolioapplication.homeScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codewithfk.expensetracker.android.feature.add_expense.AddExpenseViewModel
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.loginScreen.sharedPreference
import com.example.portfolioapplication.settingScreen.SettingViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeScreenRouter(
    viewModel: HomeScreenViewModel,
    expenseViewModel: AddExpenseViewModel,
    settingViewModel: SettingViewModel,
    navController: NavController,
    isShowDialog: Boolean,
) {
    val state = viewModel.expenses.collectAsState(initial = emptyList())
    val loginState by viewModel.HomeScreenStates.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val preference = sharedPreference(LocalContext.current)
    val userEmail = preference.getUserEmailId() ?: ""
    val userName by settingViewModel.userName.collectAsState()
    val userImageUrl by settingViewModel.userImage.collectAsState()
    val isRefreshing by settingViewModel.isRefreshing.collectAsState()
    var isSuccessfullyUpload by remember { mutableStateOf(false) }

    HomeScreenRevamp(
        userName = userName,
        userEmail = userEmail,
        userImageUrl = userImageUrl,
        onSeeAllClicked = {
            navController.navigate(Screens.TransactionsScreen.route)
        },
        expenseList = state.value,
        onDeleteTransaction = {
            scope.launch {
                viewModel.deleteExpense(it)
            }
        },
        onAddExpenseClick = {
            scope.launch {
                expenseViewModel.addExpense(expenseEntity = it)
            }
        },
        toShowCase = viewModel.showCase,
        onShowCaseCompleted = { viewModel.onShowCaseCompleted(it) },
        hasData = viewModel.hasData,
        toShowDialog = isShowDialog,
        onDismissDialog = { viewModel.hasData = false },
        onButtonClick = {
            viewModel.hasData = false
            val data = viewModel.expenseData
            viewModel.toStoreData(expense = data, context = context)
        },
        isLoading = loginState.isLoading,
        onReportClicked = {
            navController.navigate(Screens.ReportScreen.route) {
                popUpTo(navController.currentDestination?.id ?: 0) { inclusive = true }
            }
        },
        state = state,
        onSignOut = {
            navController.navigate(Screens.LoginScreen.route) {
                popUpTo(navController.currentDestination?.id ?: 0) { inclusive = true }
            }
        },
        onAddButtonClick = { name, uri ->
            if (name.isNotEmpty()) {
                preference.setUserName(name)
            }
            if (uri != null) {
                preference.setUserImageUrl(uri)
            }
            if (name.isNotEmpty() || uri != null) {
                settingViewModel.refreshProfile(context)
            }
        },
        isRefresh = isRefreshing,
        onThemeToggle = { settingViewModel.toggleTheme() },
        onCloudBackUp = {
            settingViewModel.backupToCloud(isUploadSuccessful = {
                isSuccessfullyUpload = it
            })
        },
        isUploadSuccess = false,
        isDarkMode = settingViewModel.isDarkMode.collectAsState().value,
    )
}