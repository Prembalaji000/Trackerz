package com.example.portfolioapplication


sealed class Screens(val route: String) {
    object SplashScreen : Screens("splash_screen")
    object AuthScreen : Screens("auth_screen")
    object WelcomeScreen : Screens("welcome_screen")
    object SignUpScreen : Screens("signup_screen")
    object LoginScreen : Screens("login_screen")
    object ForgotPasswordScreen : Screens("forgot_password_screen")
    object SettingScreen : Screens("setting_screen")
    object ReportScreen : Screens("report_screen")

    object HomeScreen : Screens("home_screen?showWelcomeMessage={showWelcomeMessage}") {
        fun createRoute(showDialog: Boolean = false) =
            "home_screen?showWelcomeMessage=$showDialog"
    }

    object TransactionsScreen : Screens("transactions_screen")
}