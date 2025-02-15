package com.example.portfolioapplication.splashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.portfolioapplication.R
import com.example.portfolioapplication.Screens
import com.example.portfolioapplication.loginScreen.sharedPreference
import com.example.portfolioapplication.ui.theme.Grey50
import com.example.portfolioapplication.ui.theme.bgColor
import kotlinx.coroutines.delay


@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = NavController(LocalContext.current), preference = sharedPreference(context = LocalContext.current))
}

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    preference: sharedPreference
) {
    val customFont = FontFamily(
        Font(R.font.exo2_extrabold, FontWeight.Normal)
    )
    LaunchedEffect(Unit) {
        delay(6000)
        if (preference.getCheckRememberMe()){
            navController.navigate(Screens.DashBoardScreen){
                popUpTo(Screens.SplashScreen) { inclusive = true  }
            }
        }
        navController.navigate(Screens.AuthScreen) {
            popUpTo(Screens.SplashScreen) { inclusive = true }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "logo",
            modifier = Modifier
                .size(110.dp),
            )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            textAlign = TextAlign.Center,
            text = "MY PORTFOLIO",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontFamily = customFont,
            fontSize = 24.sp,
        )
        Spacer(modifier = Modifier.padding(22.dp))
        LinearProgressIndicator(
            modifier = Modifier
                .height(2.dp)
                .padding(horizontal = 12.dp),
            color = Color.White,
            trackColor = Color.Transparent,
        )
    }
}