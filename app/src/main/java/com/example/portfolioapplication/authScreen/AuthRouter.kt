package com.example.portfolioapplication.authScreen

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.portfolioapplication.loginScreen.sharedPreference
import com.facebook.CallbackManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthScreenRouter(
    modifier: Modifier,
    context: Context,
    viewModel: AuthViewModel,
    navController: NavController,
    callbackManager: CallbackManager
){
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val preference = sharedPreference(context)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.googleSignIn(
                context = context,
                scope = lifecycleOwner.lifecycleScope,
                launcher = null,
                onNavigate = navController
            )
        } else {
            Toast.makeText(context, "Sign-In Failed", Toast.LENGTH_SHORT).show()
        }
    }

    AuthScreen(
        modifier = modifier,
        onButtonClick = {
            viewModel.navigateToLoginScreen(navController)
        },
        googleSignIn = {
            viewModel.googleSignIn(
                context = context,
                scope = scope,
                launcher = launcher,
                onNavigate = navController
            )
        },
        loginWithFacebook = {
            viewModel.loginWithFacebook(
                onNavigate = navController,
                context = context,
                callbackManager = callbackManager,
            )
        },
        isLoading = authState.isLoading,
        isDarkMode = preference.isDarkModeEnabled()
    )
}