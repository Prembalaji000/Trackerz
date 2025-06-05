package com.example.portfolioapplication.forgotPasswordScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.loginScreen.sharedPreference

@Composable
fun ForgotPasswordScreenRouter(viewModel: ForgotPasswordViewModel, navController: NavController, modifier: Modifier){

    val forgotPasswordState by viewModel.forgotPasswordState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val preference = sharedPreference(context)
    var isVerify by remember { mutableStateOf(false) }

    ForgotPasswordScreen(
        modifier = modifier,
        isDarkMode = preference.isDarkModeEnabled(),
        isLoading = forgotPasswordState.isLoading,
        isVerify = isVerify,
        setVerifyDialogVisible = { isVerify = it },
        onButtonClick = { viewModel.sendPasswordResetEmail(email = it, context = context, setVerifyDialogVisible = { isVerify = it } )},
        onBackButtonClick = { navController.navigate(Screens.LoginScreen.route){
            popUpTo(navController.currentDestination?.id?:0) { inclusive = true }
        } },
    )
}