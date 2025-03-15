package com.example.portfolioapplication

import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable
    data object SplashScreen : Screens

    @Serializable
    data object AuthScreen : Screens

    @Serializable
    data object SignUpScreen : Screens

    @Serializable
    data object LoginScreen : Screens

    @Serializable
    data object DashBoardScreen : Screens

    @Serializable
    data object SettingScreen : Screens

}