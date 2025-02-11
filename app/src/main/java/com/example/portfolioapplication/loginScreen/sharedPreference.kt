package com.example.portfolioapplication.loginScreen

import android.content.Context
import android.util.Log
import androidx.core.content.edit

class sharedPreference(context: Context){

    private val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

    fun clearCredentials() {
        with(sharedPref.edit()) {
            remove("emailId")
            remove("Password")
            remove("checkRememberMe")
            apply()
        }
    }

    fun setEmail(email : String?){
        sharedPref.edit {
            putString("emailId",email)
            Log.e("LoginActivity", "Email saved: $email")
        }
    }

    fun setPassword(password : String?){
        sharedPref.edit {
            putString("Password",password)
            Log.e("LoginActivity", "Password saved: $password")
        }
    }

    fun setCheckRememberMe(check : Boolean){
        sharedPref.edit {
            putBoolean("checkRememberMe",check)
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