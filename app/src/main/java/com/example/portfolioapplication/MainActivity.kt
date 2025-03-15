package com.example.portfolioapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.portfolioapplication.authScreen.AuthScreen
import com.example.portfolioapplication.authScreen.AuthScreenRouter
import com.example.portfolioapplication.authScreen.AuthViewModel
import com.example.portfolioapplication.authScreen.AuthViewModelFactory
import com.example.portfolioapplication.dashBoardScreen.DashBoardRouter
import com.example.portfolioapplication.dashBoardScreen.DashBoardScreen
import com.example.portfolioapplication.dashBoardScreen.DashBoardViewModel
import com.example.portfolioapplication.dashBoardScreen.DashBoardViewModelFactory
import com.example.portfolioapplication.dashBoardScreen.MainApplication
import com.example.portfolioapplication.loginScreen.LoginRouter
import com.example.portfolioapplication.loginScreen.LoginScreen
import com.example.portfolioapplication.loginScreen.LoginViewModel
import com.example.portfolioapplication.loginScreen.LoginViewModelFactory
import com.example.portfolioapplication.loginScreen.sharedPreference
import com.example.portfolioapplication.settingScreen.SettingRouter
import com.example.portfolioapplication.settingScreen.SettingScreen
import com.example.portfolioapplication.settingScreen.SettingViewModel
import com.example.portfolioapplication.settingScreen.SettingViewModelFactory
import com.example.portfolioapplication.signUpScreen.SignUpRouter
import com.example.portfolioapplication.signUpScreen.SignUpScreen
import com.example.portfolioapplication.signUpScreen.SignUpViewModel
import com.example.portfolioapplication.signUpScreen.SignUpViewModelFactory
import com.example.portfolioapplication.splashScreen.SplashScreen
import com.example.portfolioapplication.ui.theme.PortfolioApplicationTheme
import com.example.portfolioapplication.ui.theme.bgColor
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        FacebookSdk.setAdvertiserIDCollectionEnabled(true)
        callbackManager = CallbackManager.Factory.create()
        AppEventsLogger.activateApp(application)
        auth = Firebase.auth
        enableEdgeToEdge()
        setContent {
            PortfolioApplicationTheme {
                SetStatusBarColor()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                        context = this,
                        modifier = Modifier.padding(innerPadding),
                        callbackManager = callbackManager,
                        preference = sharedPreference(this)
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

     override fun onStart() {
        super.onStart()
         val user = auth.currentUser
         user?.email
         println("login : ${user?.email}")
        println("successful started")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    context: Context,
    modifier: Modifier = Modifier,
    callbackManager: CallbackManager,
    preference: sharedPreference
){
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Screens.SplashScreen,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
        composable<Screens.SplashScreen>{
            SplashScreen(
                modifier = modifier,
                navController = navController,
                preference = preference
            )
        }
        composable<Screens.AuthScreen>{
            val viewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory()
            )
            val signUpViewModel: SignUpViewModel = viewModel(
                factory = SignUpViewModelFactory()
            )
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(context)
            )
            AuthScreenRouter(
                viewModel = viewModel,
                context = context,
                modifier = modifier,
                callbackManager = callbackManager,
                signUpViewModel = signUpViewModel,
                loginViewModel = loginViewModel
            )
        }
        composable<Screens.LoginScreen>{
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(context)
            )
            LoginRouter(
                viewModel = loginViewModel,
                modifier = modifier,
                context = context
            )
        }
        composable<Screens.SignUpScreen>{
            val viewModel : SignUpViewModel = viewModel(
                factory = SignUpViewModelFactory()
            )
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(context)
            )
            SignUpRouter(
                modifier = modifier,
                viewModel = viewModel,
                context = context,
                loginViewModel = loginViewModel
            )
        }
        composable<Screens.DashBoardScreen> {
            val todoDao = MainApplication.todoDatabase.getTodoDao()
            val viewModel: DashBoardViewModel = viewModel(
                factory = DashBoardViewModelFactory(todoDao, context)
            )
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(context)
            )
            DashBoardRouter(viewModel = viewModel, modifier = modifier, loginViewModel = loginViewModel)
        }
        composable<Screens.SettingScreen>{
            val todoDao = MainApplication.todoDatabase.getTodoDao()
            val viewModel: SettingViewModel = viewModel(
                factory = SettingViewModelFactory()
            )
            val dashBoardViewModel: DashBoardViewModel = viewModel(
                factory = DashBoardViewModelFactory(todoDao, context)
            )
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(context)
            )
            SettingRouter(viewModel = viewModel,dashBoardViewModel = dashBoardViewModel, loginViewModel = loginViewModel, modifier = modifier)
        }
    }
}


@Composable
fun SetStatusBarColor() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color = bgColor )
    }
}


fun Context.fixImageRotation(uri: Uri, bitmap: Bitmap): Bitmap {
    contentResolver.openInputStream(uri)?.use { inputStream ->
        val exif = ExifInterface(inputStream)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val rotationDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

        if (rotationDegrees == 0f) return bitmap

        val matrix = Matrix().apply { postRotate(rotationDegrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    return bitmap
}