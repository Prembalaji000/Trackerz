package com.example.portfolioapplication.settingScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.portfolioapplication.R
import com.example.portfolioapplication.ui.theme.Grey30

@Preview
@Composable
fun SettingPreview(){
    SettingScreen(modifier = Modifier)
}

@Composable
fun SettingScreen(modifier: Modifier){
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopBar()
        },
        content = { paddingValue->
            Column(
                modifier = modifier
                    .padding(paddingValue),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBar()
            }
        }
    )

}

@Preview
@Composable
fun Preview(){
    TopBar()
}

@Composable
fun TopBar(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Image(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterStart),
            painter = painterResource(id = R.drawable.ic_back_button),
            contentDescription = "back_icon",
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Setting",
            fontSize = 18.sp,
            color = Grey30,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
}