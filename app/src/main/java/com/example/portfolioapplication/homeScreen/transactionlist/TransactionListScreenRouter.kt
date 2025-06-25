package com.example.portfolioapplication.homeScreen.transactionlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codewithfk.expensetracker.android.feature.transactionlist.TransactionListScreen
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.homeScreen.HomeScreenViewModel

@Composable
fun TransactionListScreenRouter(viewModel: HomeScreenViewModel, navController: NavController, modifier: Modifier){
    val state = viewModel.expenses.collectAsState(initial = emptyList())

    TransactionListScreen(
        modifier = modifier,
        state =  state,
        onBackClicked = {
            navController.popBackStack()
        },
        isDarkMode = false
    )
}