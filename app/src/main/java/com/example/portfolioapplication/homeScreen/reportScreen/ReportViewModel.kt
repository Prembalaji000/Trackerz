package com.example.portfolioapplication.homeScreen.reportScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithfk.expensetracker.android.utils.Utils
import com.example.portfolioapplication.dao.ExpenseDao
import com.example.todoroomdb.db.ExpenseEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ReportViewModel(val dao: ExpenseDao): ViewModel(){

    val expenses = dao.getAllExpense()


    fun getTotalIncome(list: List<ExpenseEntity>): String {
        var totalIncome = 0.0
        for (expense in list) {
            if (expense.type == "Income") {
                totalIncome += expense.amount
            }
        }
        return Utils.formatCurrency(totalIncome)
    }
}