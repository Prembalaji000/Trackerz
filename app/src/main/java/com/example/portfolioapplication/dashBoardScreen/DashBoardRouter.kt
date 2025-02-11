package com.example.portfolioapplication.dashBoardScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashBoardRouter(
    viewModel: DashBoardViewModel,
    modifier: Modifier
){
    val todoList by viewModel.todoList.observeAsState(emptyList())
    val totalAmounts by viewModel.totalAmount.observeAsState(0)
    println("totalAmount in : $totalAmounts ")
    DashBoardScreen(
        modifier = modifier,
        addButtonClick = { inputText, amount ->
            val totalAmount = todoList.sumOf { it.totalAmount ?: 0 }
            viewModel.addTodo(title = inputText, amount = amount, totalAmount = totalAmount)
        },
        deleteBottonClick = {
            viewModel.deleteTodo(it)
        },
        todoList = todoList,
        totalAmount = { totalAmount ->
            viewModel.updateTotalAmount(id = 1, totalAmount = totalAmount ?: 0)
        },
        totalAmountColumn = totalAmounts
    )
}





