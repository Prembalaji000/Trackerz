package com.example.portfolioapplication.loginScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.signUpScreen.LoginState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(context: Context, private val preference: sharedPreference) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    var isAddedRememberMe by mutableStateOf(sharedPreference(context).getCheckRememberMe())

    fun loginUser(email: String, password: String, navController: NavController, context: Context) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context,"Email and password cannot be empty",Toast.LENGTH_LONG).show()
            return
        }
        _loginState.update { it.copy(isLoading = true) }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (isAddedRememberMe){
                        preference.setEmail(email)
                        preference.setPassword(password)
                        preference.setCheckRememberMe(isAddedRememberMe)
                    } else {
                        preference.clearCredentials()
                    }
                    viewModelScope.launch {
                        delay(4000)
                        _loginState.update { it.copy(isLoading = false) }
                        navController.navigate(Screens.DashBoardScreen)
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Email or Password are incorrect",
                        Toast.LENGTH_LONG
                    ).show()
                    _loginState.update { it.copy(isLoading = false) }
                }
            }
    }

    fun onAddedRememberMe(isSaveEmail: Boolean) {
        isAddedRememberMe = isSaveEmail
    }
}

data class LoginState(
    var isLoading: Boolean = false,
)