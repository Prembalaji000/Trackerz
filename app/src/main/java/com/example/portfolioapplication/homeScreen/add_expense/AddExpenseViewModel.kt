package com.codewithfk.expensetracker.android.feature.add_expense

import androidx.lifecycle.ViewModel
import com.example.portfolioapplication.dao.ExpenseDao
import com.example.todoroomdb.db.ExpenseEntity

class AddExpenseViewModel(val dao: ExpenseDao) : ViewModel() {


    suspend fun addExpense(expenseEntity: ExpenseEntity): Boolean {
        return try {
            dao.insertExpense(expenseEntity)
            true
        } catch (ex: Throwable) {
            false
        }
    }
}


