package com.example.portfolioapplication.signUpScreen

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.example.portfolioapplication.Screens
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.portfolioapplication.dashBoardScreen.DashBoardRouter
import com.example.portfolioapplication.dashBoardScreen.DashBoardViewModel
import com.example.portfolioapplication.dashBoardScreen.DashBoardViewModelFactory
import com.example.portfolioapplication.dashBoardScreen.MainApplication
import com.example.portfolioapplication.loginScreen.LoginRouter
import com.example.portfolioapplication.loginScreen.LoginScreen
import com.example.portfolioapplication.loginScreen.LoginViewModel
import com.example.portfolioapplication.loginScreen.LoginViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpRouter(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    loginViewModel: LoginViewModel,
    context: Context
){
    val navController = rememberNavController()
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = Screens.SignUpScreen) {
        composable<Screens.SignUpScreen>{
            SignUpScreen(
                modifier = modifier,
                onButtonClick = { email, password ->
                    viewModel.signUpWithEmail(email = email, password = password, navController = navController, context = context)
                },
                isLoading = loginState.isLoading
            )
        }
        composable<Screens.LoginScreen> {
            val viewModels: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(context = context)
            )
            LoginRouter(
                viewModel = viewModels,
                modifier = modifier,
                context = context
            )
        }
        composable<Screens.DashBoardScreen> {
            val todoDao = MainApplication.todoDatabase.getTodoDao()
            val viewModels: DashBoardViewModel = viewModel(
                factory = DashBoardViewModelFactory(todoDao = todoDao, context = context)
            )
            DashBoardRouter(viewModel = viewModels, modifier = modifier)
        }
    }
}
