package com.example.portfolioapplication.dashBoardScreen

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoroomdb.db.TodoDatabase
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class MainApplication : Application(){
     companion object{
         lateinit var todoDatabase : TodoDatabase
         lateinit var callbackManager: CallbackManager
     }

    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this)
        FacebookSdk.sdkInitialize(applicationContext)
        FacebookSdk.setAdvertiserIDCollectionEnabled(true)
        callbackManager = CallbackManager.Factory.create()
        todoDatabase = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            TodoDatabase.NAME
        ).build()
    }
}