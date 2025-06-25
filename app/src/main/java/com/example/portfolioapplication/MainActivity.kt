package com.example.portfolioapplication

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codewithfk.expensetracker.android.feature.add_expense.AddExpenseViewModel
import com.example.portfolioapplication.authScreen.AuthScreenRouter
import com.example.portfolioapplication.authScreen.AuthViewModel
import com.example.portfolioapplication.authScreen.AuthViewModelFactory
import com.example.portfolioapplication.forgotPasswordScreen.ForgotPasswordScreenRouter
import com.example.portfolioapplication.forgotPasswordScreen.ForgotPasswordViewModel
import com.example.portfolioapplication.forgotPasswordScreen.ForgotPasswordViewModelFactory
import com.example.portfolioapplication.homeScreen.HomeScreenRouter
import com.example.portfolioapplication.homeScreen.HomeScreenViewModel
import com.example.portfolioapplication.homeScreen.HomeScreenViewModelFactor
import com.example.portfolioapplication.homeScreen.reportScreen.ReportScreenRouter
import com.example.portfolioapplication.homeScreen.reportScreen.ReportScreenViewModelFactory
import com.example.portfolioapplication.homeScreen.reportScreen.ReportViewModel
import com.example.portfolioapplication.homeScreen.transactionlist.TransactionListScreenRouter
import com.example.portfolioapplication.loginScreen.LoginRouter
import com.example.portfolioapplication.loginScreen.LoginViewModel
import com.example.portfolioapplication.loginScreen.LoginViewModelFactory
import com.example.portfolioapplication.loginScreen.sharedPreference
import com.example.portfolioapplication.settingScreen.ExpenseRepository
import com.example.portfolioapplication.settingScreen.SettingRouter
import com.example.portfolioapplication.settingScreen.SettingViewModel
import com.example.portfolioapplication.settingScreen.SettingViewModelFactory
import com.example.portfolioapplication.signUpScreen.SignUpRouter
import com.example.portfolioapplication.signUpScreen.SignUpViewModel
import com.example.portfolioapplication.signUpScreen.SignUpViewModelFactory
import com.example.portfolioapplication.splashScreen.SplashScreen
import com.example.portfolioapplication.splashScreen.WelcomeScreen
import com.example.portfolioapplication.ui.theme.PortfolioApplicationTheme
import com.example.portfolioapplication.ui.theme.bgColor
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        FacebookSdk.setAdvertiserIDCollectionEnabled(true)
        callbackManager = CallbackManager.Factory.create()
        AppEventsLogger.activateApp(application)
        auth = Firebase.auth
        enableEdgeToEdge()
        setContent {
            PortfolioApplicationTheme {
                SetStatusBarColor(context = this)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                        context = this,
                        modifier = Modifier.padding(innerPadding),
                        callbackManager = callbackManager,
                        preference = sharedPreference(this),
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        user?.email
        println("login : ${user?.email}")
        println("successful started")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    context: Context,
    modifier: Modifier = Modifier,
    callbackManager: CallbackManager,
    preference: sharedPreference
) {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Screens.SplashScreen.route
    ) {
        composable(
            route = Screens.SplashScreen.route,
        ) {
            SplashScreen(
                modifier = modifier,
                navController = navController,
                preference = preference
            )
        }
        composable(
            route = Screens.WelcomeScreen.route,
        ) {
            WelcomeScreen(
                modifier = modifier,
                navController = navController,
                preference = preference
            )
        }
        composable(
            route = Screens.AuthScreen.route,
        ) {
            val viewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory()
            )
            AuthScreenRouter(
                viewModel = viewModel,
                context = context,
                modifier = modifier,
                callbackManager = callbackManager,
                navController = navController
            )
        }
        composable(
            route = Screens.SignUpScreen.route,
        ) {
            val viewModel: SignUpViewModel = viewModel(
                factory = SignUpViewModelFactory()
            )
            SignUpRouter(
                modifier = modifier,
                viewModel = viewModel,
                context = context,
                navController = navController
            )
        }
        composable(
            route = Screens.LoginScreen.route,
        ) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(context)
            )
            LoginRouter(
                viewModel = loginViewModel,
                modifier = modifier,
                navController = navController,
                context = context
            )
        }
        composable(
            route = Screens.ForgotPasswordScreen.route,
        ) {
            val viewModel: ForgotPasswordViewModel = viewModel(
                factory = ForgotPasswordViewModelFactory()
            )
            ForgotPasswordScreenRouter(
                viewModel = viewModel,
                navController = navController,
                modifier = modifier
            )
        }
        composable(
            route = Screens.TransactionsScreen.route,
        ) {
            val expenseDao = MainApplication.expenseData.expenseDao()
            val repo = ExpenseRepository(expenseDao = expenseDao)
            val viewModel = HomeScreenViewModel(dao = expenseDao, context, repo)
            TransactionListScreenRouter(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(
            route = Screens.SettingScreen.route,
        ) {
            val expenseDao = MainApplication.expenseData.expenseDao()
            val repo = ExpenseRepository(expenseDao = expenseDao)
            val viewModel: SettingViewModel = viewModel(
                factory = SettingViewModelFactory(repo, context)
            )
            SettingRouter(
                viewModel = viewModel,
                modifier = modifier,
                navController = navController
            )
        }
        composable(
            route = Screens.HomeScreen.route,
            arguments = listOf(
                navArgument("showWelcomeMessage") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val showWelcomeMessage =
                backStackEntry.arguments?.getBoolean("showWelcomeMessage") ?: false
            val expenseDao = MainApplication.expenseData.expenseDao()
            val repo = ExpenseRepository(expenseDao = expenseDao)
            val viewModel: HomeScreenViewModel = viewModel(
                factory = HomeScreenViewModelFactor(repo, expenseDao, context)
            )
            val settingViewModel: SettingViewModel = viewModel(
                factory = SettingViewModelFactory(repo, context)
            )
            val expenseViewModel = AddExpenseViewModel(dao = expenseDao)
            HomeScreenRouter(
                viewModel = viewModel,
                expenseViewModel = expenseViewModel,
                settingViewModel = settingViewModel,
                isShowDialog = showWelcomeMessage,
                navController = navController
            )
        }
        composable(
            route = Screens.ReportScreen.route,
        ) {
            val expenseDao = MainApplication.expenseData.expenseDao()
            val viewModel: ReportViewModel = viewModel(
                factory = ReportScreenViewModelFactory(expenseDao)
            )
            ReportScreenRouter(
                modifier = modifier,
                viewModel = viewModel,
            )
        }
    }
}


@Composable
fun SetStatusBarColor(context: Context) {
    val systemUiController = rememberSystemUiController()
    val preference = sharedPreference(context)
    val isDarkMode = remember { mutableStateOf(preference.isDarkModeEnabled()) }

    LaunchedEffect(isDarkMode.value) {
        systemUiController.setSystemBarsColor(
            color = if (isDarkMode.value) Color.White.copy(alpha = 0.4f) else bgColor
        )
    }
}


fun String?.capitalizeFirstLetter(): String {
    return if (TextUtils.isEmpty(this)) {
        ""
    } else {
        this?.substring(0, 1)?.uppercase(Locale.getDefault()) + this?.substring(1)
            ?.lowercase(Locale.getDefault())
    }
}