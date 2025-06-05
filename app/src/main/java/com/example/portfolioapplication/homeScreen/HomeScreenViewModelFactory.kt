package com.example.portfolioapplication.homeScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.portfolioapplication.dao.ExpenseDao
import com.example.portfolioapplication.settingScreen.ExpenseRepository

class HomeScreenViewModelFactor(private val repo: ExpenseRepository, private val dao: ExpenseDao, private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeScreenViewModel(dao, context, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}