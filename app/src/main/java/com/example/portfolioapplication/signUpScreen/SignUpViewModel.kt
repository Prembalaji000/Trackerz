package com.example.portfolioapplication.signUpScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.portfolioapplication.Screens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel() : ViewModel(){

    private val auth = Firebase.auth
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun signUpWithEmail(email: String, password: String, navController: NavController, context: Context) {
        isValidEmail(email = email)
        isValidPassword(password = password)
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context,"Please Enter Email and password",Toast.LENGTH_LONG).show()
            return
        }
        _loginState.update { it.copy(isLoading = true) }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (password.length >= 6 && email.length >= 8){
                    if (task.isSuccessful) {
                        viewModelScope.launch {
                            delay(4000)
                            _loginState.update { it.copy(isLoading = false) }
                            navController.navigate(Screens.LoginScreen)
                        }

                    } else {
                        _loginState.update { it.copy(isLoading = false) }
                        Toast.makeText(context,task.exception?.message ?: "Sign up failed",Toast.LENGTH_LONG).show()
                    }
                } else {
                    _loginState.update { it.copy(isLoading = false) }
                    Toast.makeText(context,"Please enter a valid credential",Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}


data class LoginState(
    var isLoading: Boolean = false,
)