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
        private const val KEY_USER_NAME = "userName"
        private const val KEY_USER_EMAIL = "userEmail"
        private const val KEY_USER_IMAGE_URL = "userImageUrl"
        private const val KEY_DARK_MODE = "dark_mode"
    }

    fun clearCredentials() {
        with(sharedPref.edit()) {
            remove(KEY_EMAIL)
            remove(KEY_PASSWORD)
            remove(KEY_CHECK_REMEMBER_ME)
            apply()
        }
    }

    fun clearUserCredentials(){
        with(sharedPref.edit()){
            remove(KEY_USER_NAME)
            remove(KEY_USER_IMAGE_URL)
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
        return sharedPref.getString(KEY_EMAIL,"")
    }

    fun getPassword() : String?{
        return sharedPref.getString(KEY_PASSWORD, "")
    }

    fun getCheckRememberMe() : Boolean{
        return sharedPref.getBoolean(KEY_CHECK_REMEMBER_ME,false)
    }

    fun setUserName(userName : String){
        sharedPref.edit {
            putString(KEY_USER_NAME, userName)
        }
        println("setUserName : $userName")
    }

    fun getUserName() : String?{
        return sharedPref.getString(KEY_USER_NAME,"")
    }

    fun setUserEmailId(userEmailId : String){
        sharedPref.edit {
            putString(KEY_USER_EMAIL, userEmailId)
        }
        println("setUserEmailId : $userEmailId")
    }

    fun getUserEmailId() : String?{
        return sharedPref.getString(KEY_USER_EMAIL,"")
    }

    fun setUserImageUrl(userImageUrl : String){
        sharedPref.edit {
            putString(KEY_USER_IMAGE_URL, userImageUrl)
        }
        println("setUserImageUrl : $userImageUrl")
    }

    fun getUserImageUrl() : String?{
        return sharedPref.getString(KEY_USER_IMAGE_URL,"")
    }

    fun setDarkModeEnabled(isEnabled: Boolean) {
        sharedPref.edit {
            putBoolean(KEY_DARK_MODE, isEnabled)
        }
        println("setDarkModeEnabled : $isEnabled")
    }

    fun isDarkModeEnabled(): Boolean {
        return sharedPref.getBoolean(KEY_DARK_MODE, false)
    }
}