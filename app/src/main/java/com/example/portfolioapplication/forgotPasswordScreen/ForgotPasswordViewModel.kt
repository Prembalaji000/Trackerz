package com.example.portfolioapplication.forgotPasswordScreen

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    private val _forgotPasswordState = MutableStateFlow(ForgotPasswordState())
    val forgotPasswordState = _forgotPasswordState.asStateFlow()

    fun sendPasswordResetEmail(
        context: Context,
        email: String,
        setVerifyDialogVisible: (Boolean) -> Unit,
    ) {
        isValidEmail(email = email)
        if (email.isEmpty()) {
            Toast.makeText(context, "Please Enter Email", Toast.LENGTH_LONG).show()
            return
        }
        _forgotPasswordState.update { it.copy(isLoading = true) }
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        delay(4000)
                        setVerifyDialogVisible(true)
                        _forgotPasswordState.update { it.copy(isLoading = false) }
                    }
                } else {
                    Toast.makeText(context, task.exception?.message ?: "Error", Toast.LENGTH_LONG)
                        .show()
                    _forgotPasswordState.update { it.copy(isLoading = false) }
                }
            }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}

data class ForgotPasswordState(
    val isLoading: Boolean = false,
)