package com.example.todoroomdb.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Todo::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {

    companion object{
        const val NAME = "todo_DB"
    }
    abstract fun getTodoDao() : TodoDao
}