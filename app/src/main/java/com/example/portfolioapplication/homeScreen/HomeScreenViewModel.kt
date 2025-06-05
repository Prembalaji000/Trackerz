package com.example.portfolioapplication.homeScreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithfk.expensetracker.android.utils.Utils
import com.example.portfolioapplication.dao.ExpenseDao
import com.example.portfolioapplication.forgotPasswordScreen.ForgotPasswordState
import com.example.portfolioapplication.loginScreen.sharedPreference
import com.example.portfolioapplication.settingScreen.ExpenseRepository
import com.example.todoroomdb.db.ExpenseEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(val dao: ExpenseDao, context: Context, private val expenseRepository: ExpenseRepository) : ViewModel() {

    //val expenses = dao.getAllExpense()
    val preference = sharedPreference(context)
    val userName = preference.getUserName()
    val userImage = preference.getUserImageUrl()
    val showCase = preference.getShowCase()

    private val _HomeScreenState = MutableStateFlow(HomeScreenState())
    val HomeScreenStates = _HomeScreenState.asStateFlow()

    var hasData by mutableStateOf(false)

    val expenseData = mutableStateListOf<ExpenseEntity>()

    init {
        fetchExpensesFromFireStore(context)
    }

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

    private fun fetchExpensesFromFireStore(context: Context) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("expenses")
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.isEmpty){
                        _HomeScreenState.update { it.copy(isLoading = false) }
                        return@addOnSuccessListener
                    }
                    val expenses = snapshot.documents.mapNotNull { doc ->
                        doc.toExpenseEntity()
                    }
                    println("fetchExpensesFromFireStore : ${expenses}")
                    expenseData.clear()
                    expenseData.addAll(expenses)
                    hasData = true
                    //onResult(expenses)
                }
                .addOnFailureListener {
                    _HomeScreenState.update { it.copy(isLoading = false) }
                    Log.e("Firestore", "Error fetching data", it)
                }
        }
    }

    private fun DocumentSnapshot.toExpenseEntity(): ExpenseEntity? {
        return try {
            ExpenseEntity(
                id = this.id.toInt(),
                title = getString("title") ?: "",
                amount = getDouble("amount") ?: 0.0,
                date = getString("date") ?: "",
                type = getString("type") ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }


    fun toStoreData(expense: List<ExpenseEntity>, context: Context) {
        _HomeScreenState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(4000)
            dao.clearAll()
            expense.forEach { data ->
                expenseRepository.insertExpense(data)
            }
            _HomeScreenState.update { it.copy(isLoading = false) }
        }
    }

}

data class HomeScreenState(
    val isLoading: Boolean = false,
)