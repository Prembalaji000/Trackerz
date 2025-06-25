package com.example.portfolioapplication

import android.app.Application
import androidx.room.Room
import com.example.portfolioapplication.di.ExpenseDatabase
import com.example.portfolioapplication.di.MIGRATION_1_2
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class MainApplication : Application() {
    companion object {
        lateinit var callbackManager: CallbackManager
        lateinit var expenseData: ExpenseDatabase
    }

    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this)
        FacebookSdk.sdkInitialize(applicationContext)
        FacebookSdk.setAdvertiserIDCollectionEnabled(true)
        callbackManager = CallbackManager.Factory.create()
        expenseData = Room.databaseBuilder(
            applicationContext,
            ExpenseDatabase::class.java,
            ExpenseDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}