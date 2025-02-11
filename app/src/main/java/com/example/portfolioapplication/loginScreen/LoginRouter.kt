package com.example.portfolioapplication.loginScreen

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.dashBoardScreen.DashBoardRouter
import com.example.portfolioapplication.dashBoardScreen.DashBoardViewModel
import com.example.portfolioapplication.dashBoardScreen.DashBoardViewModelFactory
import com.example.portfolioapplication.dashBoardScreen.MainApplication
import com.example.portfolioapplication.signUpScreen.SignUpRouter
import com.example.portfolioapplication.signUpScreen.SignUpViewModel
import com.example.portfolioapplication.signUpScreen.SignUpViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginRouter(
    viewModel: LoginViewModel,
    modifier: Modifier,
    context: Context
){
    val navController = rememberNavController()
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = Screens.LoginScreen){
        composable<Screens.LoginScreen>{
            LoginScreen(
                modifier = modifier,
                onLoginClick = { email, password ->
                    viewModel.loginUser(email = email, password = password, navController = navController, context = context) },
                onButtonClick = {
                    navController.navigate(Screens.SignUpScreen)
                },
                onAddedRememberMe = {viewModel.onAddedRememberMe(it)},
                isAddedRememberMe = viewModel.isAddedRememberMe,
                isLoading = loginState.isLoading
            )
        }
        composable<Screens.SignUpScreen> {
            val signUpViewModel: SignUpViewModel = viewModel(
                factory = SignUpViewModelFactory()
            )
            SignUpRouter(
                modifier = modifier,
                viewModel = signUpViewModel,
                loginViewModel = viewModel,
                context = context
            )
        }
        composable<Screens.DashBoardScreen> {
            val todoDao = MainApplication.todoDatabase.getTodoDao()
            val viewModels: DashBoardViewModel = viewModel(
                factory = DashBoardViewModelFactory(todoDao, context)
            )
            DashBoardRouter(viewModel = viewModels, modifier = modifier)
        }
    }
}