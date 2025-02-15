package com.example.portfolioapplication.loginScreen

import android.content.Context
import android.util.Log
import androidx.core.content.edit

class sharedPreference(context: Context){

    private val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EMAIL = "emailId"
        private const val KEY_PASSWORD = "Password"
        private const val KEY_CHECK_REMEMBER_ME = "checkRememberMe"
    }

    fun clearCredentials() {
        with(sharedPref.edit()) {
            remove(KEY_EMAIL)
            remove(KEY_PASSWORD)
            remove(KEY_CHECK_REMEMBER_ME)
            apply()
        }
    }

    fun setEmail(email : String?){
        sharedPref.edit {
            putString(KEY_EMAIL, email)
            Log.e("LoginActivity", "Email saved: $email")
        }
    }

    fun setPassword(password : String?){
        sharedPref.edit {
            putString(KEY_PASSWORD, password)
            Log.e("LoginActivity", "Password saved: $password")
        }
    }

    fun setCheckRememberMe(check : Boolean){
        sharedPref.edit {
            putBoolean(KEY_CHECK_REMEMBER_ME, check)
            Log.e("LoginActivity", "Check saved: $check")
        }
    }

    fun getEmail() :String?{
        return sharedPref.getString("emailId","")
    }

    fun getPassword() : String?{
        return sharedPref.getString("Password", "")
    }

    fun getCheckRememberMe() : Boolean{
        return sharedPref.getBoolean("checkRememberMe",false)
    }
}