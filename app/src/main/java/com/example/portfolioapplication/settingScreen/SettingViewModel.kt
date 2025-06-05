package com.example.portfolioapplication.settingScreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.portfolioapplication.loginScreen.sharedPreference
import com.example.todoroomdb.db.ExpenseEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingViewModel(context: Context, private val expenseRepository: ExpenseRepository) : ViewModel() {
    private val _settingState = MutableStateFlow(SettingState())
    val settingState = _settingState.asStateFlow()
    val preference = sharedPreference(context)

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userImage = MutableStateFlow("")
    val userImage: StateFlow<String> = _userImage

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    val userNameIn = preference.getUserName()
    val userImageIn = preference.getUserImageUrl()

    init {
        _userName.value = userNameIn ?: ""
        _userImage.value = userImageIn ?: ""
    }

    fun refreshProfile(context: Context) {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(2000)
            val preference = sharedPreference(context)
            _userName.value = preference.getUserName() ?: ""
            _userImage.value = preference.getUserImageUrl() ?: ""
            _isRefreshing.value = false
        }
    }

    private val _isDarkMode = MutableStateFlow(preference.isDarkModeEnabled())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    fun toggleTheme() {
        val newMode = !_isDarkMode.value
        _isDarkMode.value = newMode
        preference.setDarkModeEnabled(newMode)
    }

    fun backupToCloud(isUploadSuccessful: (Boolean) -> Unit) {
        _isRefreshing.value = true
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            val localExpenses = expenseRepository.getAllExpenseOnce()
            localExpenses.forEach { expense ->
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .collection("expenses")
                    .document(expense.id.toString())
                    .set(expense)
                delay(4000)
                isUploadSuccessful(true)
                _isRefreshing.value = false
            }
        }
    }

    fun syncFromCloudToRoom(context: Context) {
        fetchExpensesFromFireStore(context) { cloudExpenses ->
            viewModelScope.launch {
                delay(4000)
                cloudExpenses.forEach {
                    expenseRepository.insertExpense(it)
                    _isRefreshing.value = false
                }
            }
        }
    }


    private fun fetchExpensesFromFireStore(context: Context, onResult: (List<ExpenseEntity>) -> Unit) {
        _isRefreshing.value = true
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("expenses")
                .get()
                .addOnSuccessListener { data ->
                    if (data.isEmpty){
                        _isRefreshing.value = false
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }
                    val expenses = data.documents.mapNotNull { doc ->
                        doc.toExpenseEntity()
                    }
                    onResult(expenses)
                }
                .addOnFailureListener {
                    _isRefreshing.value = false
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


}

data class SettingState(
    var isLoading: Boolean = false,
)