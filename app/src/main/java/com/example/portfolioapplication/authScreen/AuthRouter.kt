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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.dashBoardScreen.DashBoardRouter
import com.example.portfolioapplication.dashBoardScreen.DashBoardViewModel
import com.example.portfolioapplication.dashBoardScreen.DashBoardViewModelFactory
import com.example.portfolioapplication.dashBoardScreen.MainApplication
import com.example.portfolioapplication.loginScreen.LoginRouter
import com.example.portfolioapplication.loginScreen.LoginScreen
import com.example.portfolioapplication.loginScreen.LoginViewModel
import com.example.portfolioapplication.loginScreen.LoginViewModelFactory
import com.example.portfolioapplication.signUpScreen.SignUpRouter
import com.example.portfolioapplication.signUpScreen.SignUpViewModel
import com.facebook.CallbackManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthScreenRouter(
    modifier: Modifier,
    context: Context,
    viewModel: AuthViewModel,
    signUpViewModel: SignUpViewModel,
    loginViewModel: LoginViewModel,
    callbackManager: CallbackManager
){
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val navController = rememberNavController()
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.googleSignIn(
                context = context,
                scope = lifecycleOwner.lifecycleScope,
                launcher = null,
                onNavigate = {
                    navController.navigate(it)
                }
            )
        } else {
            Toast.makeText(context, "Sign-In Failed", Toast.LENGTH_SHORT).show()
        }
    }

    NavHost(navController = navController, startDestination = Screens.AuthScreen) {
        composable<Screens.AuthScreen> {
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
                        onNavigate = { screen ->
                            navController.navigate(screen)
                        }
                    )
                },
                loginWithFacebook = {
                    viewModel.loginWithFacebook(
                        onNavigate = {
                            navController.navigate(it)
                        },
                        context = context,
                        callbackManager = callbackManager,
                    )
                },
                isLoading = authState.isLoading
            )
        }
        composable<Screens.LoginScreen> {
            val viewModels: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(context)
            )
            LoginRouter(
                viewModel = viewModels,
                modifier = modifier,
                context = context
            )
        }
        composable<Screens.SignUpScreen>{
            SignUpRouter(
                modifier = modifier,
                viewModel = signUpViewModel,
                loginViewModel = loginViewModel,
                context = context,
            )
        }
        composable<Screens.DashBoardScreen> {
            val todoDao = MainApplication.todoDatabase.getTodoDao()
            val viewModels: DashBoardViewModel = viewModel(
                factory = DashBoardViewModelFactory(todoDao, context)
            )
            DashBoardRouter(viewModel = viewModels, modifier = modifier, loginViewModel = loginViewModel)
        }
    }
}