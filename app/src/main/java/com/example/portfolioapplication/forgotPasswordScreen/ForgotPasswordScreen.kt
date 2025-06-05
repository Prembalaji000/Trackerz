package com.example.portfolioapplication.forgotPasswordScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.portfolioapplication.R
import com.example.portfolioapplication.settingScreen.TopBar
import com.example.portfolioapplication.signUpScreen.AnimatedLoader
import com.example.portfolioapplication.ui.theme.DarkBlue96
import com.example.portfolioapplication.ui.theme.Grey50
import com.example.portfolioapplication.ui.theme.bgColor
import com.example.portfolioapplication.ui.theme.darkBlue
import com.example.portfolioapplication.ui.theme.line
import com.example.portfolioapplication.ui.theme.redAccent100

@Preview
@Composable
fun ForgotPasswordPreview(){
    ForgotPasswordScreen(modifier = Modifier, isDarkMode = false, isLoading = false, onBackButtonClick = {}, onButtonClick = {}, isVerify = false, setVerifyDialogVisible = {})
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen(
    modifier: Modifier,
    isDarkMode : Boolean,
    isLoading : Boolean,
    onButtonClick: (String) -> Unit,
    onBackButtonClick: () -> Unit,
    isVerify: Boolean,
    setVerifyDialogVisible: (Boolean) -> Unit, // <-- add this
){
    var email by remember { mutableStateOf("") }
    val isEmailValid = email.endsWith("@mail.com") || email.endsWith("@gmail.com") || email.endsWith("@email.com") || email.isEmpty()
    val emailInteractionSource = remember { MutableInteractionSource() }
    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = modifier.fillMaxSize().imePadding(),
        containerColor = if (isDarkMode) Color.White.copy(alpha = 0.4f) else bgColor,
        topBar = {
            ForgotPasswordTopBar(onBackButtonClick = onBackButtonClick, isDarkMode = isDarkMode)
        },
        content = { paddingValue ->
            AnimatedLoader(isLoading = isLoading)
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValue)
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 28.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_forgot_password),
                    contentDescription = "forgot Password",
                    contentScale = ContentScale.Fit,
                )
                Text(
                    text = "Provide your email address. We’ll send you a link to reset your password.",
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkMode) bgColor else Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                )
                Spacer(modifier = Modifier.padding(12.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusEvent {
                            isFocused = it.isFocused
                        },
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    singleLine = true,
                    interactionSource = emailInteractionSource,
                    visualTransformation = VisualTransformation.None,
                    placeholder = {
                        Text(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            text = "Email address",
                            fontSize = 12.sp,
                            color = Grey50
                        )
                    },
                    prefix = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = "email",
                            modifier = Modifier
                                .size(20.dp),
                            tint = Grey50
                        )
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = darkBlue,
                        unfocusedBorderColor = Color.LightGray,
                        errorBorderColor = redAccent100,
                        errorCursorColor = Grey50,
                        focusedContainerColor = DarkBlue96,
                        unfocusedContainerColor = DarkBlue96,
                        errorContainerColor = Color.White,
                        cursorColor = Color.Black,
                        selectionColors = TextSelectionColors(handleColor = Grey50, backgroundColor = darkBlue)
                    ),
                    shape = RoundedCornerShape(26.dp),
                    textStyle = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.padding(22.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(line)
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(listOf(Color.White.copy(alpha = 0.15f), Color.Black)),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clickable {
                            onButtonClick(email.removeSuffix(" ")) },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Reset Password",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
            if(isVerify){
                VerifyEmailDialog(onDismiss = {
                    email = ""
                    setVerifyDialogVisible(false)
                    }
                )
            }
        }
    )
}

@Preview(showBackground = false)
@Composable
fun VerifyEmailDialogPreview(){
    VerifyEmailDialog(onDismiss = {})
}

@Composable
fun VerifyEmailDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false)
    ){
        Surface(
            modifier = Modifier
                //  .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = bgColor
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(52.dp)
                        .padding(bottom = 12.dp),
                    painter = painterResource(id = R.drawable.ic_email),
                    contentScale = ContentScale.Fit,
                    contentDescription = "email icon"
                )
                Text(
                    text = "Check your Email!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "We've emailed you a password reset link, valid for 10 minutes",
                    modifier = Modifier.padding(top = 12.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.padding(6.dp))
                Button(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = line),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    androidx.compose.material.Text(
                        text = "Verify email",
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
  /*  AlertDialog(
        properties = DialogProperties(
            dismissOnClickOutside = false
        ),
        onDismissRequest = onDismiss,
        containerColor = bgColor,
        confirmButton = {},
        text = {

        }
    )*/
}



@Composable
fun ForgotPasswordTopBar(onBackButtonClick:() -> Unit, isDarkMode: Boolean){
    val customFont = FontFamily(
        Font(R.font.exo2_extrabold, FontWeight.Normal)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterStart)
                    .clickable {
                        onBackButtonClick()
                    },
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "back_icon",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(if (isDarkMode) bgColor else Color.White)
            )
            Text(
                text = "Forgot Password",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = customFont,
                color = if (isDarkMode) bgColor else Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(6.dp))
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 22.dp), color = Grey50)
    }
}
