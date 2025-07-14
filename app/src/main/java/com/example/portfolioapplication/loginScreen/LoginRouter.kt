package com.example.portfolioapplication.loginScreen

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.portfolioapplication.Screens

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginRouter(
    viewModel: LoginViewModel,
    navController: NavController,
    modifier: Modifier,
    context: Context
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val preference = sharedPreference(context)

    LoginScreen(
        modifier = modifier,
        onLoginClick = { email, password ->
            viewModel.loginUser(
                email = email,
                password = password,
                navController = navController,
                context = context
            )
        },
        onButtonClick = {
            navController.navigate(Screens.SignUpScreen.route) {
                popUpTo(navController.currentDestination?.id ?: 0) { inclusive = true }
            }
        },
        onForgotPasswordClick = {
            navController.navigate(Screens.ForgotPasswordScreen.route) {
                popUpTo(navController.currentDestination?.id ?: 0) { inclusive = true }
            }
        },
        onAddedRememberMe = { viewModel.onAddedRememberMe(it) },
        isAddedRememberMe = viewModel.isAddedRememberMe,
        isLoading = loginState.isLoading,
        isDarkMode = preference.isDarkModeEnabled()
    )
}