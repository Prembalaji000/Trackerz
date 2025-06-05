package com.example.portfolioapplication.settingScreen

import com.example.portfolioapplication.dao.ExpenseDao
import com.example.todoroomdb.db.ExpenseEntity

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    suspend fun getAllExpenseOnce(): List<ExpenseEntity> {
        return expenseDao.getAllExpenseOnce()
    }

    suspend fun insertExpense(expense: ExpenseEntity) {
        expenseDao.insertExpense(expense)
    }
}