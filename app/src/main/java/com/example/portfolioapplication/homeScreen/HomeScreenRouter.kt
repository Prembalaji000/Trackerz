package com.example.portfolioapplication.homeScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codewithfk.expensetracker.android.feature.add_expense.AddExpenseViewModel
import com.codewithfk.expensetracker.android.feature.transactionlist.TransactionListScreen
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.homeScreen.add_expense.AddExpense
import com.example.portfolioapplication.loginScreen.LoginViewModel
import com.example.portfolioapplication.settingScreen.SettingRouter
import com.example.portfolioapplication.settingScreen.SettingViewModel
import com.example.portfolioapplication.settingScreen.SettingViewModelFactory
import com.facebook.CallbackManager
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeScreenRouter(
    viewModel: HomeScreenViewModel,
    expenseViewModel: AddExpenseViewModel,
    navController: NavController,
    modifier: Modifier
){
    val state = viewModel.expenses.collectAsState(initial = emptyList())
    val expense = viewModel.getTotalExpense(state.value)
    val income = viewModel.getTotalIncome(state.value)
    val balance = viewModel.getBalance(state.value)
    var isFromAddExpense by mutableStateOf(false)
    val scope = rememberCoroutineScope()

    HomeScreen(
        modifier = modifier,
        userName = viewModel.userName?:"",
        userImageUrl = viewModel.userImage?:"",
        onAddExpenseClicked = {
            isFromAddExpense = false
            navController.navigate(Screens.AddExpenseScreen)
        },
        onAddIncomeClicked = {
            isFromAddExpense = true
            navController.navigate(Screens.AddExpenseScreen)
        },
        balance = balance,
        income = income,
        expense = expense,
        onSeeAllClicked = {
            navController.navigate(Screens.TransactionsScreen)
        },
        expenseList = state.value,
        onDeleteTransaction = {
            scope.launch {
                viewModel.deleteExpense(it)
            }
        },
        settingButton = {
            navController.navigate(Screens.SettingScreen){
                popUpTo(navController.currentDestination?.id?:0) { inclusive = true }
            }
        },
        onAddExpenseClick = {
            scope.launch {
                expenseViewModel.addExpense(expenseEntity = it)
            }
        },
        toShowCase = viewModel.showCase,
        onShowCaseCompleted = { viewModel.onShowCaseCompleted(it) }
    )
}