package com.example.todoroomdb.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo ORDER BY createAt DESC")
    fun getAllTodo(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(todo: Todo)

    @Query("UPDATE Todo SET totalAmount = :totalAmount WHERE id = :id")
    suspend fun updateTotalAmount(id: Int, totalAmount: Int): Int

    @Query("Delete FROM Todo where id = :id")
    fun deleteTodo(id: Int)

    @Query("SELECT COALESCE(SUM(amount), 0) FROM todo")
     fun getTotalAmount(): LiveData<Int>
}



