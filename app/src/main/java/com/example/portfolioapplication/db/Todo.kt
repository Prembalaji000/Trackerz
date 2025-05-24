package com.example.todoroomdb.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var title : String,
    var amount : Int,
    var totalAmount : Int = 0,
    var createAt : Date
)

@Entity(tableName = "expense_table")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val title: String,
    val amount: Double,
    val date: String,
    val type: String
)

data class ExpenseSummary(
    val type: String,
    val date: String,
    val total_amount: Double
)


