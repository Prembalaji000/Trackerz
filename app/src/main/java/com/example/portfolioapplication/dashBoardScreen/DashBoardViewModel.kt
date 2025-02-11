package com.example.portfolioapplication.dashBoardScreen

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.todoroomdb.db.Todo
import com.example.todoroomdb.db.TodoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date

class DashBoardViewModel(private val todoDao: TodoDao, val context: Context) : ViewModel() {

    val todoList = todoDao.getAllTodo().asLiveData()
    private val _totalAmount = MutableLiveData<Int>()
    //val totalAmount: LiveData<Int> get() = _totalAmount
    val totalAmount: LiveData<Int> = todoDao.getTotalAmount().map { it ?: 0 }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTodo(title : String, amount : Int, totalAmount: Int){
        if (title.isEmpty()){
            Toast.makeText(context, "Please enter title", Toast.LENGTH_SHORT).show()
            return
        }
        if (amount == 0){
            Toast.makeText(context, "Please enter amount", Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.addTodo(Todo(title = title, amount = amount, totalAmount = totalAmount, createAt = Date.from(Instant.now())))
        }
    }

    fun updateTotalAmount(id : Int, totalAmount: Int) {
        println("totalAmount3 : $totalAmount")
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.updateTotalAmount(totalAmount = totalAmount, id = id)
        }
    }

    fun totalAmount(){
        viewModelScope.launch {
            _totalAmount.value
        }
    }

    fun deleteTodo(id : Int){
        viewModelScope.launch (Dispatchers.IO){
            todoDao.deleteTodo(id)
        }
    }
}