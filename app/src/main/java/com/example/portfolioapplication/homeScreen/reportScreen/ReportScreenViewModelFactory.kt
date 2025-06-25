package com.example.portfolioapplication.homeScreen.reportScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.portfolioapplication.dao.ExpenseDao

class ReportScreenViewModelFactory(private val dao: ExpenseDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReportViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}