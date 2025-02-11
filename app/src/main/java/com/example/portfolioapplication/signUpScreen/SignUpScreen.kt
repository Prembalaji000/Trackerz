package com.example.portfolioapplication.signUpScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.portfolioapplication.R
import com.example.portfolioapplication.ui.theme.DarkBlue96
import com.example.portfolioapplication.ui.theme.Grey50
import com.example.portfolioapplication.ui.theme.bgColor
import com.example.portfolioapplication.ui.theme.darkBlue
import com.example.portfolioapplication.ui.theme.redAccent100


@Preview
@Composable
fun SignUpScreePreview(){
    SignUpScreen(modifier = Modifier, onButtonClick = {_, _ ->}, isLoading = true)
}


@Composable
fun SignUpScreen(
    modifier: Modifier,
    onButtonClick: (String, String) -> Unit,
    isLoading: Boolean
    ){
    val customFont = FontFamily(
        Font(R.font.exo2_extrabold, FontWeight.Normal)
    )
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isEmailValid = email.endsWith("@mail.com") || email.endsWith("@gmail.com") || email.endsWith("@email.com") || email.isEmpty()
    val isPasswordValid = password.length >= 6 || password.isEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = bgColor)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
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
            modifier = Modifier.fillMaxWidth()
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
            Spacer(modifier = Modifier.height(6.dp))
            PasswordStrengthBar(password = password)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                modifier = Modifier.padding(horizontal = 6.dp),
                text = "Use 8 or more characters with a mix of letters, numbers & symbols.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Grey50
            )
            Spacer(modifier = Modifier.height(16.dp))
            StartButton("Get started, it's free!")
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Do you already have an account?",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            SignInButton(
                text = "Sign In",
                onButtonClick = { onButtonClick(email, password) }
            )
        }
    }
}


@Preview
@Composable
fun PreviewScreen(){
    PasswordStrengthBar(password = "prembalaji")
}


@Composable
fun TextField(
    modifier: Modifier,
    email : String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    isEmailValid : Boolean,
    isPasswordValid : Boolean
){
    val emailInteractionSource = remember { MutableInteractionSource() }
    val passwordInteractionSource = remember { MutableInteractionSource() }
    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isPasswordVisible by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "E-mail address",
            color = Grey50,
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.padding(2.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent {
                    isFocused = it.isFocused
                },
            value = email,
            onValueChange = {
                onEmailChange(it)
            },
            singleLine = true,
            interactionSource = emailInteractionSource,
            visualTransformation = VisualTransformation.None,
            placeholder = {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Email Id",
                    fontSize = 12.sp,
                    color = Grey50
                )
            },
            prefix = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "search",
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
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(
                fontSize = 12.sp,
                color = Color.Black
            ),
            supportingText = {
                AnimatedVisibility(visible = !isEmailValid) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .offset(x = (-10).dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = "warning",
                            modifier = modifier
                                .size(10.dp),
                            tint = redAccent100
                        )
                        Text(
                            text = "Please enter a valid registered email ID",
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 2.dp),
                            color = redAccent100
                        )
                    }
                }
            },
            isError = !isEmailValid
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Password",
            color = Grey50,
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.padding(2.dp))
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            value = password,
            onValueChange = {
                onPasswordChange(it)
            },
            interactionSource = passwordInteractionSource,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Password",
                    fontSize = 12.sp,
                    color = Color.LightGray,
                )
            },
            prefix = {
                Icon(
                    Icons.Outlined.Lock,
                    contentDescription = "search",
                    modifier = Modifier
                        .size(20.dp),
                    tint = Grey50
                )
            },
            suffix = {
                IconButton(
                    onClick = {
                        isPasswordVisible = !isPasswordVisible
                    },
                    modifier = Modifier.size(22.dp)
                ) {
                    AnimatedContent(
                        targetState = isPasswordVisible,
                        label = "password"
                    ) { isShow ->
                        if (isShow) {
                            Icon(
                                Icons.Default.Done,
                                contentDescription = "showPassword",
                                tint = Color.Black
                            )
                        } else {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "showPassword",
                                tint = Color.Black
                            )
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
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
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(
                fontSize = 12.sp,
                color = Color.Black
            ),
            isError = !isPasswordValid,
            supportingText = {
                AnimatedVisibility(visible = !isPasswordValid) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .offset(x = (-10).dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = "warning",
                            modifier = modifier
                                .size(10.dp),
                            tint = redAccent100
                        )
                        Text(
                            text = "Please enter a valid password",
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 2.dp),
                            color = redAccent100
                        )
                    }
                }
            }
        )
    }
}


@Composable
fun StartButton(text : String){
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
            .clickable { /* Handle click */ },
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

@Composable
fun SignInButton(
    text: String,
    onButtonClick: () -> Unit
    ){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color.DarkGray)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(listOf(Color.White.copy(alpha = 0.15f), Color.Black)),
                shape = RoundedCornerShape(25.dp)
            )
            .clickable { onButtonClick() },
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


@Composable
fun PasswordStrengthBar(password: String) {
    val strength = calculatePasswordStrength(password)
    val segmentColors = listOf(
        Color(0xFFD3D3D3), // Light Gray - No strength
        Color(0xFFFFC1C1), // Light Red - Weak
        Color(0xFFFFE5A1), // Light Yellow - Medium
        Color(0xFFB0F2B6), // Light Green - Strong
        Color(0xFFADD8E6)  // Light Blue - Very Strong
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween // Spacing between bars
        ) {
            // Create 4 bars and color them based on strength
            for (i in 0 until 4) {
                Box(
                    modifier = Modifier
                        .weight(1f) // Makes all bars equal width
                        .height(8.dp)
                        .background(
                            color = if (i < strength) segmentColors[strength] else Color.DarkGray,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                if (i < 3) Spacer(modifier = Modifier.width(4.dp)) // Space between bars
            }
        }
    }
}

// Function to calculate password strength
fun calculatePasswordStrength(password: String): Int {
    var strength = 0
    if (password.length >= 6) strength++  // Length Check
    if (password.any { it.isDigit() }) strength++  // Contains Number
    if (password.any { !it.isLetterOrDigit() }) strength++  // Contains Special Char
    if (password.length >= 10) strength++  // Strong Length Check
    return strength
}


@Preview
@Composable
fun preview(){
    AnimatedLoader(
        modifier = Modifier,
        isLoading = true
    )
}

@Composable
fun LottieAnimations() {
    val rawComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loader_animation))
    val progress by animateLottieCompositionAsState(composition = rawComposition)

    LottieAnimation(
        composition = rawComposition,
        progress = progress,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit,
    )
}


@Composable
fun AnimatedLoader(
    modifier: Modifier = Modifier,
    isLoading : Boolean
) {
    val preLoaderLottieAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loader_animation))

    val preLoaderProgress by animateLottieCompositionAsState(
        preLoaderLottieAnimation,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    AnimatedVisibility(visible = isLoading) {
        Dialog(onDismissRequest = {  }) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                LottieAnimation(
                    composition = preLoaderLottieAnimation,
                    progress = preLoaderProgress,
                    modifier = modifier.size(90.dp),
                    contentScale = ContentScale.Fit,
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = "Loading..",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}