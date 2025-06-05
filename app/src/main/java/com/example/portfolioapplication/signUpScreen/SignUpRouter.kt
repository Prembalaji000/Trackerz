package com.example.portfolioapplication.signUpScreen

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.loginScreen.sharedPreference

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpRouter(
    modifier: Modifier,
    viewModel: SignUpViewModel,
    navController: NavController,
    context: Context
){
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val preference = sharedPreference(context)

    SignUpScreen(
        modifier = modifier,
        onButtonClick = { email, password ->
            viewModel.signUpWithEmail(email = email, password = password, navController = navController, context = context)
        },
        isLoading = loginState.isLoading,
        isDarkMode = preference.isDarkModeEnabled(),
        onClickSocials = {
            navController.navigate(Screens.AuthScreen.route){
                popUpTo(navController.currentDestination?.id?:0) { inclusive = true }
            }
        }
    )
}
