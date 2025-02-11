package com.example.portfolioapplication.dashBoardScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoroomdb.db.TodoDao

class DashBoardViewModelFactory(private val todoDao: TodoDao, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashBoardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashBoardViewModel(todoDao, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
