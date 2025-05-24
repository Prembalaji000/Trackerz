package com.example.portfolioapplication.homeScreen.add_expense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codewithfk.expensetracker.android.feature.add_expense.AddExpenseViewModel
import com.example.portfolioapplication.Screens
import kotlinx.coroutines.launch

@Composable
fun AddExpenseScreenRouter(
    modifier: Modifier,
    viewModel: AddExpenseViewModel
){
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = Screens.AddExpenseScreen){
        composable<Screens.AddExpenseScreen> {
            AddExpense(
                modifier = modifier,
                isIncome = false,
                onAddExpenseClick = {
                    scope.launch {
                        viewModel.addExpense(expenseEntity = it)
                        navController.popBackStack()
                    } },
                onMenuClicked = {},
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}