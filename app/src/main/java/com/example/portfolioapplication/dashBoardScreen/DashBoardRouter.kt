package com.example.portfolioapplication.dashBoardScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.authScreen.AuthViewModel
import com.example.portfolioapplication.authScreen.AuthViewModelFactory
import com.example.portfolioapplication.loginScreen.LoginViewModel
import com.example.portfolioapplication.settingScreen.SettingRouter
import com.example.portfolioapplication.settingScreen.SettingViewModel
import com.example.portfolioapplication.settingScreen.SettingViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashBoardRouter(
    viewModel: DashBoardViewModel,
    loginViewModel: LoginViewModel,
    modifier: Modifier
){
    val todoList by viewModel.todoList.observeAsState(emptyList())
    val totalAmounts by viewModel.totalAmount.observeAsState(0)
    val navController = rememberNavController()
    println("totalAmount in : $totalAmounts ")

    NavHost(navController = navController, startDestination = Screens.DashBoardScreen){
        composable<Screens.DashBoardScreen> {
            DashBoardScreen(
                modifier = modifier,
                addButtonClick = { inputText, amount ->
                    val totalAmount = todoList.sumOf { it.totalAmount ?: 0 }
                    viewModel.addTodo(title = inputText, amount = amount, totalAmount = totalAmount)
                },
                deleteButtonClick = {
                    viewModel.deleteTodo(it)
                },
                todoList = todoList,
                totalAmount = { totalAmount ->
                    viewModel.updateTotalAmount(id = 1, totalAmount = totalAmount ?: 0)
                },
                totalAmountColumn = totalAmounts,
                settingButton = { navController.navigate(Screens.SettingScreen) }
            )
        }
        composable<Screens.SettingScreen>{
            val settingViewModel: SettingViewModel = viewModel(
                factory = SettingViewModelFactory()
            )
            SettingRouter(viewModel = settingViewModel, dashBoardViewModel = viewModel, loginViewModel = loginViewModel, modifier = modifier)
        }
    }
}





