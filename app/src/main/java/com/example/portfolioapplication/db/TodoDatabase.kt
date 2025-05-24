package com.example.portfolioapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoroomdb.db.Converters
import com.example.todoroomdb.db.Todo
import com.example.todoroomdb.db.TodoDao

@Database(entities = [Todo::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {

    companion object{
        const val NAME = "todo_DB"
    }
    abstract fun getTodoDao() : TodoDao
}