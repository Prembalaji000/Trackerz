package com.example.portfolioapplication

import kotlinx.serialization.Serializable

sealed class Screens(val route: String) {
    object SplashScreen : Screens("splash_screen")
    object AuthScreen : Screens("auth_screen")
    object WelcomeScreen : Screens("welcome_screen")
    object SignUpScreen : Screens("signup_screen")
    object LoginScreen : Screens("login_screen")
    object ForgotPasswordScreen : Screens("forgot_password_screen")
    object DashBoardScreen : Screens("dashboard_screen")
    object SettingScreen : Screens("setting_screen")

    // HomeScreen with parameter
    object HomeScreen : Screens("home_screen?showWelcomeMessage={showWelcomeMessage}") {
        fun createRoute(showDialog: Boolean = false) =
            "home_screen?showWelcomeMessage=$showDialog"
    }

    object AddExpenseScreen : Screens("add_expense_screen")
    object TransactionsScreen : Screens("transactions_screen")
}