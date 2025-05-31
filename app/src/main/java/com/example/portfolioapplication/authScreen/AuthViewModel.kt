package com.example.portfolioapplication.authScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.portfolioapplication.R
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.loginScreen.sharedPreference
import com.example.portfolioapplication.signUpScreen.LoginState
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel() : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()
    private val _userDetail = MutableStateFlow<LoginCredential?>(null)
    val userDetail = _userDetail.asStateFlow()

    fun setUserDetail(user: LoginCredential) {
        _userDetail.value = user
    }

    fun googleSignIn(
        context: Context,
        scope: CoroutineScope,
        launcher: ActivityResultLauncher<Intent>?,
        onNavigate: (Screens) -> Unit
    ) {
        val preference = sharedPreference(context)
        val credentialManager = CredentialManager.create(context)
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(getCredentialOptions(context))
            .build()
        scope.launch {
            try {
                val result = credentialManager.getCredential(context = context, request = request)
                when(result.credential){
                    is CustomCredential -> {
                        _authState.update { it.copy(isLoading = true) }
                        if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                            val googleTokenId = googleIdTokenCredential.idToken
                            val authCredential = GoogleAuthProvider.getCredential(googleTokenId,null)
                            val user = Firebase.auth.signInWithCredential(authCredential).await().user
                            user?.let {
                                if (!it.isAnonymous) {
                                    setUserDetail(user = LoginCredential(
                                        userName = user.displayName,
                                        userEmail = user.email,
                                        userImageUrl = user.photoUrl?.toString()
                                    ))
                                    preference.setUserName(user.displayName?:"")
                                    preference.setUserEmailId(user.email?:"")
                                    //preference.setUserImageUrl(user.photoUrl?.toString()?:"")
                                    viewModelScope.launch {
                                        delay(4000)
                                        _authState.update { state ->
                                            state.copy(isLoading = false)
                                        }
                                        onNavigate(Screens.HomeScreen)
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        Toast.makeText(context,"Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: NoCredentialException){
                Log.e("GoogleSignIn", "No credential found $e")
                launcher?.launch(getIntent())
            } catch (e: GetCredentialException){
                e.printStackTrace()
            }
        }
    }

    fun navigateToLoginScreen(navController: NavController){
        _authState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(1000)
            _authState.update { it.copy(isLoading = false) }
            navController.navigate(Screens.LoginScreen){
                popUpTo(navController.currentDestination?.id?:0) { inclusive = true }
            }
        }
    }


    private fun getIntent(): Intent {
        return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
            putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
        }
    }

    private fun getCredentialOptions(context: Context): CredentialOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setServerClientId(context.getString(R.string.client_id))
            .build()
    }

    fun loginWithFacebook(context: Context, onNavigate: (Screens) -> Unit, callbackManager: CallbackManager) {
        val preference = sharedPreference(context)
        val loginManager = LoginManager.getInstance()
        loginManager.logInWithReadPermissions(context as Activity, listOf("email", "public_profile"))
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                _authState.update { it.copy(isLoading = true) }
                val accessToken = result.accessToken
                println("login success : $accessToken")
                val request = GraphRequest.newMeRequest(accessToken) { jsonObject, _ ->
                    val credential = FacebookAuthProvider.getCredential(accessToken.token)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                val name = user?.displayName
                                val email = user?.email
                                val uid = user?.uid
                                preference.setUserName(user?.displayName?:"")
                                preference.setUserEmailId(user?.email?:"")
                                //preference.setUserImageUrl(user?.photoUrl?.toString()?:"")
                                println("Firebase Auth Success: $name, $email, $uid")
                                viewModelScope.launch {
                                    delay(4000)
                                    _authState.update { it.copy(isLoading = false) }
                                    onNavigate(Screens.HomeScreen)
                                }
                            } else {
                                _authState.update { it.copy(isLoading = false) }
                                println("Firebase Auth Failed: ${task.exception?.message}")
                            }
                        }
                }
                val params = Bundle()
                params.putString("fields", "id,name,email")
                request.parameters = params
                request.executeAsync()
            }
            override fun onCancel() {
                println("Facebook login cancelled")
            }
            override fun onError(error: FacebookException) {
                println("Facebook login error: ${error.message}")
            }
        })
    }
}

data class AuthState(
    var isLoading: Boolean = false,
)


data class LoginCredential(
    var userName : String?,
    var userEmail : String?,
    var userImageUrl : String?
)