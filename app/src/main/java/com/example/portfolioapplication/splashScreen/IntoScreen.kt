package com.example.portfolioapplication.splashScreen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.portfolioapplication.ui.theme.bgColor

@Preview
@Composable
fun PreviewTestScreen() {
    WelcomeScreen(
        navController = NavController(LocalContext.current),
        preference = sharedPreference(context = LocalContext.current)
    )
}

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    preference: sharedPreference
) {
    val customFont = FontFamily(Font(R.font.exo2_extrabold, FontWeight.ExtraBold))
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF8E53))
    )
    val isDarkMode = preference.isDarkModeEnabled()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color.White.copy(alpha = 0.4f) else bgColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
                .background(if (isDarkMode) Color.White.copy(alpha = 0.4f) else bgColor),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_expenses_icon),
                contentDescription = "logo",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(id = R.string.app_title),
                textAlign = TextAlign.Center,
                color = if (isDarkMode) bgColor else Color.White,
                fontFamily = customFont,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_frame),
            contentDescription = "illustration",
            modifier = Modifier.size(532.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Welcome to Trackerz! Start your journey toward smarter money management.",
            modifier = Modifier.padding(horizontal = 18.dp),
            fontSize = 16.sp,
            fontFamily = FontFamily.Default,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(gradientBrush)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.15f), Color.Black)
                    ),
                    shape = RoundedCornerShape(25.dp)
                )
                .clickable {
                    preference.setShowShowCase(true)
                    navController.navigate(Screens.AuthScreen.route) {
                        popUpTo(navController.currentDestination?.id ?: 0) { inclusive = true }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Get Started",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDarkMode) bgColor else Color.White
            )
        }
    }
}
