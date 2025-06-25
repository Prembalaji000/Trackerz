package com.example.portfolioapplication.homeScreen.reportScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.portfolioapplication.Screens

@Composable
fun ReportScreenRouter(modifier: Modifier, viewModel: ReportViewModel, navController: NavController){

    val state = viewModel.expenses.collectAsState(initial = emptyList())

    ReportScreen(
        modifier = modifier,
        isDarkMode = false,
        expenses = state.value,
        onBackButtonClick = {
            navController.navigate(Screens.HomeScreen.route){
                popUpTo(navController.currentDestination?.id?:0) { inclusive = true }
            }
        }
    )
}