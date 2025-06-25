package com.example.portfolioapplication.homeScreen.reportScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
fun ReportScreenRouter(modifier: Modifier, viewModel: ReportViewModel) {

    val state = viewModel.expenses.collectAsState(initial = emptyList())

    ReportScreen(
        modifier = modifier,
        isDarkMode = false,
        expenses = state.value,
    )
}