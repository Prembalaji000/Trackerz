package com.example.portfolioapplication.settingScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.authScreen.AuthScreenRouter
import com.example.portfolioapplication.authScreen.AuthViewModel
import com.example.portfolioapplication.dashBoardScreen.DashBoardScreen
import com.example.portfolioapplication.dashBoardScreen.DashBoardViewModel
import com.example.portfolioapplication.loginScreen.LoginRouter
import com.example.portfolioapplication.loginScreen.LoginViewModel
import com.example.portfolioapplication.loginScreen.sharedPreference

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RestrictedApi")
@Composable
fun SettingRouter(
    viewModel: SettingViewModel,
    loginViewModel: LoginViewModel,
    dashBoardViewModel: DashBoardViewModel,
    modifier: Modifier,
){
    val navController = rememberNavController()
    val context = LocalContext.current
    val todoList by dashBoardViewModel.todoList.observeAsState(emptyList())
    val totalAmounts by dashBoardViewModel.totalAmount.observeAsState(0)
    val preference = sharedPreference(LocalContext.current)
    val userName = preference.getUserName()?:""
    val userEmail = preference.getUserEmailId()?:""
    val userImageUrl = preference.getUserImageUrl()?:""
    NavHost(navController = navController, startDestination = Screens.SettingScreen){
        composable<Screens.SettingScreen>{
            SettingScreen(
                modifier = modifier,
                onBackButtonClick = {
                    navController.navigate(Screens.DashBoardScreen)
                },
                onSignOut = {
                    navController.navigate(Screens.LoginScreen)
                },
                initialUserName = userName,
                userEmail = userEmail,
                userImageUrl = userImageUrl,
                addButtonClick = { name, uri ->
                    preference.setUserName(name)
                    preference.setUserImageUrl(uri.toString())
                }
            )
        }
        composable<Screens.DashBoardScreen> {
            DashBoardScreen(
                modifier = modifier,
                addButtonClick = { inputText, amount ->
                    val totalAmount = todoList.sumOf { it.totalAmount ?: 0 }
                    dashBoardViewModel.addTodo(title = inputText, amount = amount, totalAmount = totalAmount)
                },
                deleteButtonClick = {
                    dashBoardViewModel.deleteTodo(it)
                },
                todoList = todoList,
                totalAmount = { totalAmount ->
                    dashBoardViewModel.updateTotalAmount(id = 1, totalAmount = totalAmount ?: 0)
                },
                totalAmountColumn = totalAmounts,
                settingButton = { navController.navigate(Screens.SettingScreen) }
            )
        }
        composable<Screens.LoginScreen> {
            LoginRouter(viewModel = loginViewModel, modifier = modifier, context = context)
        }
    }
}