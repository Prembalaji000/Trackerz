package com.example.portfolioapplication.dashBoardScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.portfolioapplication.R
import com.example.portfolioapplication.ui.theme.DarkBlue96
import com.example.portfolioapplication.ui.theme.Grey30
import com.example.portfolioapplication.ui.theme.Grey50
import com.example.portfolioapplication.ui.theme.bgColor
import com.example.portfolioapplication.ui.theme.darkBlue
import com.example.portfolioapplication.ui.theme.redAccent100
import com.example.todoroomdb.db.Todo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    modifier: Modifier,
    addButtonClick: (String, Int) -> Unit,
    todoList: List<Todo>?,
    deleteBottonClick: (Int) -> Unit,
    totalAmount: (Int) -> Unit,
    totalAmountColumn: Int
){
    println("totalAmount: ${totalAmountColumn}")
    val customFont = FontFamily(
        Font(R.font.exo2_extrabold, FontWeight.Normal)
    )
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(28.dp))
            Text(
                text = "MY PORTFOLIO",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = customFont,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(30.dp))
            todoList.let { list->
                val totalAmounts = (list ?: emptyList()).sumOf { it.totalAmount }
                CircularProgress(totalAmount = totalAmount, amounts = totalAmounts, totalAmountColumn = totalAmountColumn)
            }
            Spacer(modifier = Modifier.height(20.dp))
            /*SubscriptionTabs(modifier = Modifier)
            Spacer(modifier = Modifier.height(12.dp))*/
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 80.dp)
            ) {
                items(todoList ?: emptyList()) { item ->
                    TodoItem(
                        name = item.title,
                        amount = item.amount,
                        onDelete = { deleteBottonClick(item.id) }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubscriptionButton(text = "Add Subscription") {
                scope.launch {
                    showBottomSheet = true
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                containerColor = bgColor,
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                BottomSheetContent(
                    onClose = { showBottomSheet = false },
                    addButtonClick = addButtonClick
                )
            }
        }
    }
}


@Composable
fun BottomSheetContent(
    onClose: () -> Unit,
    addButtonClick: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val nameInteractionSource = remember { MutableInteractionSource() }
    val amountInteractionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier
                .padding(start = 4.dp),
            text = "Enter title",
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
                color = Color.Black
            ),
        )
        Spacer(modifier = Modifier.padding(6.dp))
        Text(
            modifier = Modifier
                .padding(start = 4.dp),
            text = "Enter amount",
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
            value = amount.toString(),
            onValueChange = { amounts ->
                val parsedAmount = amounts.toIntOrNull()
                if (parsedAmount != null) {
                    amount = parsedAmount
                    errorMessage = ""
                } else {
                    errorMessage = "Invalid input, please enter a number"
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions.Default,
            singleLine = true,
            interactionSource = amountInteractionSource,
            visualTransformation = VisualTransformation.None,
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(
                fontSize = 12.sp,
                color = Color.Black
            )

        )
        Spacer(modifier = Modifier.padding(12.dp))
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
                    brush = Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.Black
                        )
                    ),
                    shape = RoundedCornerShape(25.dp)
                )
                .clickable {
                    addButtonClick(name, amount)
                    amount = 0
                    name = ""
                },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Add Subscription!",
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
fun Preview(){
    DashBoardScreen(modifier = Modifier, addButtonClick = { _, _ -> }, todoList = null, deleteBottonClick = {}, totalAmount = {}, totalAmountColumn = 0)
}

@Composable
fun CircularProgress(totalAmount: (Int) -> Unit, amounts: Int, totalAmountColumn: Int) {
    var showDialog by remember { mutableStateOf(false) }
    var amount by remember { mutableIntStateOf(amounts) }
    var newAmount by remember { mutableStateOf("") }
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF8E53))
    )
    LaunchedEffect(amounts) {
        amount = amounts
    }
    val initialAmount = 10000f
    val remainingAmount = (initialAmount - totalAmountColumn).coerceAtLeast(0f)
    val progress = (remainingAmount / initialAmount).coerceIn(0f, 1f)


    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawArc(
                color = Color.DarkGray.copy(alpha = 0.3f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(12.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                brush = gradientBrush,
                startAngle = 135f,
                sweepAngle = 270f * progress, // Dynamic progress
                useCenter = false,
                style = Stroke(12.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(
            modifier = Modifier.clickable { showDialog = true },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("TRACKIZER", color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                "$${amount}",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text("This month bills", color = Color.Gray, fontSize = 12.sp)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Edit Amount") },
                text = {
                    OutlinedTextField(
                        value = newAmount,
                        onValueChange = { newAmount = it },
                        label = { Text("Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = darkBlue,
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )
                    )
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color(0xFF1771E6)),
                        onClick = {
                            totalAmount(newAmount.toInt())
                            newAmount = 0.toString()
                            showDialog = false
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color(0xFF1771E6)),
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancel")
                    }
                },
                containerColor = bgColor
            )
        }
    }
}



@Composable
fun SubscriptionTabs(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }

    Row(
        modifier = modifier
            .background(Color(0xFF181818), shape = RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TabItem(
            text = "Your subscriptions",
            isSelected = selectedTab == 0,
            onClick = { selectedTab = 0 }
        )
        TabItem(
            text = "Upcoming bills",
            isSelected = selectedTab == 1,
            onClick = { selectedTab = 1 }
        )
    }
}

@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFF242424) else Color.Transparent)
            .clickable { onClick() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}


@Composable
fun SubscriptionList(todoList: List<Todo>?, deleteBottonClick: (Int) -> Unit) {

    Column {
        TodoList(todoList = todoList, deleteBottonClick = deleteBottonClick)
    }
}

@Composable
fun SubscriptionItem(name: String, price: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                BorderStroke(1.dp, Grey50), // Stroke border with custom color
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color.Transparent, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = name, color = Color.White)
        }
        Text(text = price, color = Color.White)
    }
}

@Composable
fun SubscriptionButton(
    text : String,
    onLoginClick: () -> Unit,
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

@Composable
fun TodoList(todoList: List<Todo>?, deleteBottonClick: (Int) -> Unit) {
    println("input text show in compose: $todoList")
    todoList?.let {
        LazyColumn(
            modifier = Modifier,
            content = {
                itemsIndexed(it) { index: Int, item: Todo ->
                    TodoItem(
                        name = item.title, // Assuming 'title' is the name of the Todo item
                        amount = item.amount, // Assuming there's a description
                        onDelete = {
                            deleteBottonClick(item.id)
                        }
                    )
                }
            }
        )
    } ?: Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = "No items yet",
        fontSize = 16.sp
    )
}

@Preview
@Composable
fun TodoListPreview(){
    BottomSheetContent(onClose = {}, addButtonClick = { _, _ -> })
}

@Composable
fun TodoItem(name: String, amount: Int, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                Color(0xFF1E1E1E),
                shape = RoundedCornerShape(16.dp)
            ) // Subtle dark background
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Section (Icon + Name & Amount)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            if (name == "EMI" || name == "RD"){
                Image(
                    painter = painterResource(id = R.drawable.bank_logo),
                    contentDescription = "Logo for $name", // Dynamic description
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray.copy(alpha = 0.2f))
                        .padding(4.dp)
                )
            }
            if (name == "kite"){
                Image(
                    painter = painterResource(id = R.drawable.kite_logo),
                    contentDescription = "Logo for $name", // Dynamic description
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray.copy(alpha = 0.2f))
                        .padding(4.dp)
                )
            }
            if (name == "coin"){
                Image(
                    painter = painterResource(id = R.drawable.coin_logo),
                    contentDescription = "Logo for $name", // Dynamic description
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray.copy(alpha = 0.2f))
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.width(22.dp))

            Column {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Amount: $$amount",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }

        // Delete Button
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .size(32.dp)
                .padding(end = 12.dp)
                .background(Color.Red.copy(alpha = 0.2f), shape = CircleShape) // Soft red background
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete $name",
                tint = Color.Red
            )
        }
    }
}



