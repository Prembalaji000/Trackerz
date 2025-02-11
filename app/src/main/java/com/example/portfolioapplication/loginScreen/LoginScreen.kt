package com.example.portfolioapplication.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.portfolioapplication.R
import com.example.portfolioapplication.signUpScreen.AnimatedLoader
import com.example.portfolioapplication.signUpScreen.SignInButton
import com.example.portfolioapplication.signUpScreen.StartButton
import com.example.portfolioapplication.signUpScreen.TextField
import com.example.portfolioapplication.ui.theme.bgColor


@Preview
@Composable
fun LoginPreview(){
    LoginScreen(modifier = Modifier, onButtonClick = {}, onLoginClick = {_,_ ->}, isAddedRememberMe = true, onAddedRememberMe = {  }, isLoading = true)
}

@Composable
fun LoginScreen(
    modifier: Modifier,
    onLoginClick: (String, String) -> Unit,
    onButtonClick: () -> Unit,
    onAddedRememberMe: (Boolean) -> Unit,
    isAddedRememberMe :Boolean,
    isLoading : Boolean
    ){
    val customFont = FontFamily(
        Font(R.font.exo2_extrabold, FontWeight.Normal)
    )
    val context = LocalContext.current
    var email by remember { mutableStateOf(sharedPreference(context).getEmail()?: "") }
    var password by remember { mutableStateOf(sharedPreference(context).getPassword()?: "") }
    val isEmailValid = email.endsWith("@mail.com") || email.endsWith("@gmail.com") || email.endsWith("@email.com") || email.isEmpty()
    val isPasswordValid = password.length >= 6 || password.isEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedLoader(isLoading = isLoading)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "MY PORTFOLIO",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontFamily = customFont,
            fontSize = 24.sp,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier,
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                isEmailValid = isEmailValid,
                isPasswordValid = isPasswordValid
            )
            Spacer(modifier = Modifier.padding(10.dp))
            RememberMeForgotPasswordRow(
                modifier = Modifier,
                onAddedRememberMe = onAddedRememberMe,
                isAddedRememberMe = isAddedRememberMe)
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton(
                text = "Sign In",
                onLoginClick = {
                    onLoginClick(email,password)
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "If you don't have an account yet?",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            SignInButton(
                text = "Sign Up",
                onButtonClick = onButtonClick
            )
        }
    }
}

@Composable
fun RememberMeForgotPasswordRow(
    modifier: Modifier = Modifier,
    onAddedRememberMe: (Boolean) -> Unit,
    isAddedRememberMe :Boolean,
) {
    var rememberMeChecked by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isAddedRememberMe,
                onCheckedChange = {
                    onAddedRememberMe(it)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.dark_grey_30)
                ),
            )
            Text(
                text = "Remember me",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
            )
        }

        Text(
            text = "Forgot password",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .clickable { /* Navigate to Forgot Password Screen */ }
        )
    }
}


@Composable
fun LoginButton(
    text : String,
    onLoginClick: () -> Unit
){
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF8E53))
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(brush = gradientBrush)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(listOf(Color.White.copy(alpha = 0.15f), Color.Black)),
                shape = RoundedCornerShape(25.dp)
            )
            .clickable { onLoginClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}
