package com.example.portfolioapplication.settingScreen

import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Divider
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.portfolioapplication.R
import com.example.portfolioapplication.capitalizeFirstLetter
import com.example.portfolioapplication.signUpScreen.AnimatedLoader
import com.example.portfolioapplication.ui.theme.Grey30
import com.example.portfolioapplication.ui.theme.Grey50
import com.example.portfolioapplication.ui.theme.bgColor
import com.example.portfolioapplication.ui.theme.darkBlue
import com.example.portfolioapplication.ui.theme.line
import kotlinx.coroutines.launch

@Preview
@Composable
fun SettingPreview() {
    SettingScreen(
        modifier = Modifier,
        onSignOut = {},
        initialUserName = "User Name",
        userImageUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
        userEmail = "UserName@example.com",
        addButtonClick = { _, _ -> },
        isRefresh = false,
        isDarkMode = false,
        onThemeToggle = {},
        onCloudBackUp = {},
        isUploadSuccess = false,
    )
}

@Composable
fun SettingScreen(
    modifier: Modifier,
    onSignOut: () -> Unit,
    initialUserName: String,
    userImageUrl: String,
    userEmail: String,
    addButtonClick: (String, String?) -> Unit,
    isRefresh: Boolean,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    onCloudBackUp: () -> Unit,
    isUploadSuccess: Boolean,
) {
    var userName by remember { mutableStateOf(initialUserName) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    val profileEditeSheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmValueChange = { false }
    )
    val editImageSheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmValueChange = { false }
    )
    val scope = rememberCoroutineScope()
    var imagePicked by remember { mutableStateOf("") }

    ModalBottomSheetLayout(
        sheetState = profileEditeSheetState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetBackgroundColor = bgColor,
        sheetContent = {
            EditProfile(
                modifier = modifier,
                userName = userName,
                onClose = { showBottomSheet = false },
                addButtonClick = { name, amount ->
                    scope.launch {
                        userName = name
                        profileEditeSheetState.hide()
                        addButtonClick(name, amount)
                    }
                },
                onCancelClick = {
                    scope.launch {
                        showBottomSheet = false
                        profileEditeSheetState.hide()
                    }
                },
                isDarkMode = isDarkMode,
                imageUri = imagePicked,
                onEditButtonClick = {
                    scope.launch {
                        showBottomSheet = false
                        profileEditeSheetState.hide()
                        showEditDialog = true
                        editImageSheetState.show()
                    }
                }
            )
        }
    ) {
        ModalBottomSheetLayout(
            sheetState = editImageSheetState,
            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            sheetBackgroundColor = bgColor,
            sheetContent = {
                EditImage(
                    modifier = modifier,
                    onImageSelected = {
                        scope.launch {
                            imagePicked = it
                            editImageSheetState.hide()
                            showEditDialog = false
                            showBottomSheet = true
                            profileEditeSheetState.show()
                        }
                    },
                    onCancelClick = {
                        scope.launch {
                            showEditDialog = false
                            editImageSheetState.hide()
                            showBottomSheet = true
                            profileEditeSheetState.show()
                        }
                    }
                )
            }
        ) {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                containerColor = if (isDarkMode) Color.White.copy(alpha = 0.4f) else bgColor,
                topBar = {
                    TopBar(isDarkMode = isDarkMode)
                },
                content = { paddingValue ->
                    AnimatedLoader(isLoading = isRefresh)
                    Column(
                        modifier = modifier
                            .padding(paddingValue)
                            .padding(top = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ProfileView(
                                userName = initialUserName,
                                userImageUrl = userImageUrl,
                                userEmail = userEmail,
                                isDarkMode = isDarkMode
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            Button(
                                colors = ButtonColors(
                                    containerColor = Grey30,
                                    contentColor = Color.White,
                                    disabledContainerColor = Grey30,
                                    disabledContentColor = Color.White
                                ),
                                onClick = { scope.launch { profileEditeSheetState.show() } }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.edit_profile),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.padding(12.dp))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Grey50,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clip(RoundedCornerShape(16.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Grey30
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp, horizontal = 20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            modifier = Modifier.size(28.dp),
                                            painter = painterResource(id = R.drawable.ic_light_theme),
                                            contentDescription = "theme_icon"
                                        )
                                        Spacer(modifier = Modifier.padding(6.dp))
                                        Text(
                                            text = stringResource(id = R.string.theme_mode),
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Switch(
                                        checked = isDarkMode,
                                        onCheckedChange = { onThemeToggle() },
                                        colors = SwitchDefaults.colors(
                                            checkedTrackColor = Color(0xFF4CAF50),
                                            uncheckedTrackColor = Color(0xFF3A3A3C),
                                            checkedThumbColor = Color.White,
                                            uncheckedThumbColor = Color.White
                                        )
                                    )
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = Grey50
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            modifier = Modifier.size(28.dp),
                                            painter = painterResource(id = R.drawable.ic_icloud),
                                            contentDescription = "cloud_icon1"
                                        )
                                        Spacer(modifier = Modifier.padding(6.dp))
                                        Text(
                                            text = "iCloud sync",
                                            textAlign = TextAlign.Center,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White
                                        )
                                    }

                                    Switch(
                                        checked = isUploadSuccess,
                                        onCheckedChange = { onCloudBackUp.invoke() },
                                        colors = SwitchDefaults.colors(
                                            checkedTrackColor = Color(0xFF4CAF50),
                                            uncheckedTrackColor = Color(0xFF3A3A3C),
                                            checkedThumbColor = Color.White,
                                            uncheckedThumbColor = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }
                },
                bottomBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 18.dp, start = 6.dp, end = 6.dp)
                    ) {
                        SignOutButton(onSignOut = onSignOut, title = "Sign Out")
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    TopBar(isDarkMode = false)
}

@Composable
fun TopBar(isDarkMode: Boolean) {
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
            Text(
                text = stringResource(id = R.string.setting),
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


@Preview
@Composable
fun ProfilePreview() {
    ProfileView(
        userName = "User Name",
        userImageUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
        userEmail = "james.s.sherman@example-pet-store.com",
        isDarkMode = false
    )
}


@Composable
fun ProfileView(
    userName: String,
    userImageUrl: String,
    userEmail: String,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    println("userDetail: $userName, $userEmail $userImageUrl")
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val imageLoader = ImageLoader(context)

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(userImageUrl)
                .crossfade(true)
                .build(),
            imageLoader = imageLoader,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            placeholder = painterResource(id = R.drawable.ic_user),
            error = painterResource(id = R.drawable.ic_user)
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Text(
            text = userName.capitalizeFirstLetter(),
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) bgColor else Color.White,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.padding(2.dp))

        Text(
            text = userEmail,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
fun SignOutButton(onSignOut: () -> Unit, title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(line)
            .clickable { onSignOut() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}


@Composable
fun EditProfile(
    modifier: Modifier,
    userName: String,
    onClose: () -> Unit,
    onCancelClick: () -> Unit,
    imageUri: String,
    addButtonClick: (String, String?) -> Unit,
    onEditButtonClick: () -> Unit,
    isDarkMode: Boolean
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val nameInteractionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Edit profile",
                fontSize = 16.sp,
                color = Color.White,
            )
            Icon(
                modifier = Modifier
                    .size(22.dp)
                    .clickable {
                        keyboardController?.hide()
                        onCancelClick()
                    },
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel Button",
                tint = Color.Gray
            )
        }
        Spacer(modifier = Modifier.padding(2.dp))
        Divider(color = Grey50, thickness = 1.dp)
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            modifier = Modifier
                .padding(start = 4.dp),
            text = stringResource(id = R.string.edit_name),
            color = if (isDarkMode) Color.Black else Grey50,
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.padding(2.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .onFocusEvent {
                    isFocused = it.isFocused
                },
            value = name,
            onValueChange = {
                name = it
            },
            singleLine = true,
            interactionSource = nameInteractionSource,
            visualTransformation = VisualTransformation.None,
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(
                fontSize = 12.sp,
                color = if (isDarkMode) Color.Black else Grey50
            ),
            placeholder = {
                Text(
                    text = userName,
                    fontSize = 12.sp,
                    color = if (isDarkMode) Color.Black else Grey50.copy(alpha = 0.2f),
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Grey50,
                unfocusedBorderColor = Grey50,
                cursorColor = Color.Black,
                selectionColors = TextSelectionColors(
                    handleColor = Grey50,
                    backgroundColor = darkBlue
                )
            ),
        )
        Spacer(modifier = Modifier.padding(6.dp))
        Text(
            modifier = Modifier
                .padding(start = 4.dp),
            text = stringResource(id = R.string.edit_photo),
            color = if (isDarkMode) Color.Black else Grey50,
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Grey50, RoundedCornerShape(10.dp))
                .background(Grey30.copy(alpha = 0.1f))
                .clickable {
                    keyboardController?.hide()
                    onEditButtonClick()
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri.isNotEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Upload Icon",
                        tint = Color.Green.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Image selected",
                        color = Grey50.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Upload Icon",
                        tint = Grey50.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Click to add photo",
                        color = Grey50.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(line)
                .clickable {
                    keyboardController?.hide()
                    if (name.isEmpty() && imageUri.isEmpty()) {
                        onCancelClick()
                    } else {
                        addButtonClick(name, imageUri)
                        amount = ""
                        name = ""
                        onClose()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.done),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun EditProfilePreview() {
    EditImage(
        modifier = Modifier,
        onImageSelected = {},
        onCancelClick = {}
    )
}

@Composable
fun EditImage(
    modifier: Modifier,
    onImageSelected: (String) -> Unit,
    onCancelClick: () -> Unit
) {
    val defaultImages = listOf(
        R.drawable.ic_avator_2,
        //R.drawable.ic_avator_3,
        R.drawable.ic_avator_4,
        //R.drawable.ic_avator_5,
        R.drawable.ic_avator_6,
        R.drawable.ic_avator_7,
        R.drawable.ic_avator_8,
        R.drawable.ic_avator_9,
        //R.drawable.ic_avator_10
    )
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Choose an Avatar",
                fontSize = 16.sp,
                color = Color.White,
            )
            Icon(
                modifier = Modifier
                    .size(22.dp)
                    .clickable {
                        onCancelClick()
                    },
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel Button",
                tint = Color.Gray
            )
        }
        Spacer(modifier = Modifier.padding(2.dp))
        Divider(color = Grey50, thickness = 1.dp)
        Spacer(modifier = Modifier.padding(10.dp))
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(5),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(defaultImages) { resId ->
                val imageUri = Uri.parse("android.resource://${context.packageName}/$resId")
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable {
                            onImageSelected(imageUri.toString())
                            println("imageUri: $imageUri")
                        }
                )
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
    }
}