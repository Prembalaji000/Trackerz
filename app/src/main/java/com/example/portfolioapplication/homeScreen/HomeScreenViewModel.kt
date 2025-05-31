package com.example.portfolioapplication.homeScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithfk.expensetracker.android.utils.Utils
import com.example.portfolioapplication.dao.ExpenseDao
import com.example.portfolioapplication.loginScreen.sharedPreference
import com.example.todoroomdb.db.ExpenseEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeScreenViewModel(val dao: ExpenseDao, context: Context) : ViewModel() {

    //val expenses = dao.getAllExpense()
    val preference = sharedPreference(context)
    val userName = preference.getUserName()
    val userImage = preference.getUserImageUrl()
    val showCase = preference.getShowCase()

    val expenses: StateFlow<List<ExpenseEntity>> = dao.getAllExpense()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onShowCaseCompleted(isShowed: Boolean){
        preference.setShowShowCase(isShowed)
    }

    suspend fun deleteExpense(expenseEntity: ExpenseEntity){
        dao.deleteExpense(expenseEntity)
    }

    fun getBalance(list: List<ExpenseEntity>): String {
        var balance = 0.0
        for (expense in list) {
            if (expense.type == "Income") {
                balance += expense.amount
            } else {
                balance -= expense.amount
            }
        }
        return Utils.formatCurrency(balance)
    }

    fun getTotalExpense(list: List<ExpenseEntity>): String {
        var total = 0.0
        for (expense in list) {
            if (expense.type != "Income") {
                total += expense.amount
            }
        }

        return Utils.formatCurrency(total)
    }

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