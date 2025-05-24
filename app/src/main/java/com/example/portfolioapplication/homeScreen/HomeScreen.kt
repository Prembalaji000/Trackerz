package com.example.portfolioapplication.homeScreen

import android.annotation.SuppressLint
import android.provider.ContactsContract.Data
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithfk.expensetracker.android.utils.Utils
import com.example.portfolioapplication.R
import com.example.portfolioapplication.ui.theme.Grey30
import com.example.portfolioapplication.ui.theme.Red
import com.example.portfolioapplication.ui.theme.Zinc
import com.example.todoroomdb.db.ExpenseEntity
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.portfolioapplication.homeScreen.add_expense.DataForm
import com.example.portfolioapplication.homeScreen.add_expense.ExpenseDatePickerDialog
import com.example.portfolioapplication.homeScreen.add_expense.ExpenseDropDown
import com.example.portfolioapplication.homeScreen.add_expense.TitleComponent
import com.example.portfolioapplication.ui.theme.Grey50
import com.example.portfolioapplication.ui.theme.bgColor
import com.example.portfolioapplication.ui.theme.line
import kotlinx.coroutines.launch

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen(
        modifier = Modifier,
        userName = "Prem",
        userImageUrl = "",
        onAddExpenseClicked = {},
        onAddIncomeClicked = {},
        balance = "",
        income = "",
        expense = "",
        onSeeAllClicked = {},
        expenseList = listOf(),
        onDeleteTransaction = {},
        onAddExpenseClick = {},
        settingButton = {}
    )
}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    userName: String,
    userImageUrl: String,
    onAddExpenseClicked: () -> Unit,
    onAddIncomeClicked: () -> Unit,
    balance: String,
    income: String,
    expense: String,
    onSeeAllClicked: () -> Unit,
    expenseList: List<ExpenseEntity>,
    onDeleteTransaction: (ExpenseEntity) -> Unit,
    onAddExpenseClick: (model: ExpenseEntity) -> Unit,
    settingButton: () -> Unit,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader(context)
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isFromIncome by mutableStateOf(false)

    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize().background(bgColor)) {
            val (nameRow, list, card, topBar, add) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.ic_topbar), contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 38.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(userImageUrl)
                            .crossfade(true)
                            .build(),
                        imageLoader = imageLoader,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        placeholder = painterResource(id = R.drawable.ic_user),
                        error = painterResource(id = R.drawable.ic_user)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        ExpenseTextView(
                            text = "Good Afternoon",
                            color = White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        ExpenseTextView(
                            text = userName,
                            color = White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_icon),
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = White),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(26.dp)
                        .clickable {
                            settingButton.invoke()
                        }
                )
            }
            CardItem(
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                balance = balance, income = income, expense = expense
            )
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(list) {
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
                list = expenseList,
                onSeeAllClicked = onSeeAllClicked,
                onDeleteTransaction = onDeleteTransaction
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(add) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                , contentAlignment = Alignment.BottomEnd
            ) {
                MultiFloatingActionButton(
                    modifier = Modifier,
                    onAddExpenseClicked = {
                        scope.launch {
                            isFromIncome = false
                            showBottomSheet = true
                        }
                    },
                    onAddIncomeClicked = {
                        scope.launch {
                            isFromIncome = true
                            showBottomSheet = true
                        }
                    }
                )
            }
            if (showBottomSheet){
                ModalBottomSheet(
                    containerColor = bgColor,
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                ){
                    BottomSheet(
                        modifier = modifier,
                        onAddExpenseClick = {
                            onAddExpenseClick.invoke(it)
                            showBottomSheet = false
                        },
                        isIncome = isFromIncome
                    )
                }
            }
        }
    }
}


@Composable
fun MultiFloatingActionButton(
    modifier: Modifier,
    onAddExpenseClicked: () -> Unit,
    onAddIncomeClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(visible = expanded) {
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = Zinc, shape = RoundedCornerShape(12.dp))
                            .clickable {
                                onAddIncomeClicked.invoke()
                                expanded = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_income),
                            contentDescription = "Add Income",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = Zinc, shape = RoundedCornerShape(12.dp))
                            .clickable {
                                onAddExpenseClicked.invoke()
                                expanded = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_expense),
                            contentDescription = "Add Expense",
                            tint = Color.White
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Zinc)
                    .clickable {
                        expanded = !expanded
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_addbutton),
                    contentDescription = "small floating action button",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun CardItem(
    modifier: Modifier,
    balance: String,
    income: String,
    expense: String
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column {
                ExpenseTextView(
                    text = "Total Balance",
                    color = White
                )
                Spacer(modifier = Modifier.size(8.dp))
                ExpenseTextView(
                    text = balance, color = White,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CardRowItem(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                imaget = R.drawable.ic_income
            )
            Spacer(modifier = Modifier.size(8.dp))
            CardRowItem(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                title = "Expense",
                amount = expense,
                imaget = R.drawable.ic_expense
            )
        }

    }
}



@Composable
fun TransactionList(
    modifier: Modifier,
    list: List<ExpenseEntity>,
    title: String = "Recent Transactions",
    onSeeAllClicked: () -> Unit,
    onDeleteTransaction: (ExpenseEntity) -> Unit
) {
    if (list.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 142.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Transactions Found",
                fontSize = 16.sp,
                color = White,
                fontWeight = FontWeight.Normal,
            )
        }
    } else {
        LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
            item {
                Column {
                    Box(modifier = modifier.fillMaxWidth()) {
                        ExpenseTextView(
                            text = title,
                            color = White
                        )
                        if (title == "Recent Transactions") {
                            ExpenseTextView(
                                text = "See all",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .clickable {
                                        onSeeAllClicked.invoke()
                                    },
                                color = White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                }
            }
            items(items = list,
                key = { item -> item.id ?: 0 }) { item ->
                SwipeableTransactionItem(
                    expense = item,
                    onDelete = { onDeleteTransaction(item) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)
@Composable
fun SwipeableTransactionItem(
    expense: ExpenseEntity,
    onDelete: () -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = {
                val color = MaterialTheme.colors.error
                val scale by animateFloatAsState(
                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                )
                val dismissProgress = if (dismissState.progress.fraction > 0.05f)
                    dismissState.progress.fraction else 0f
                val alpha by animateFloatAsState(targetValue = dismissProgress * 0.9f)

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color.copy(alpha = alpha))
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Transaction",
                        modifier = Modifier.scale(scale),
                        tint = Color.White
                    )
                }
            },
            directions = setOf(DismissDirection.EndToStart),
            dismissThresholds = { direction ->
                FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.9f else 0.5f)
            },
            dismissContent = {
                TransactionItemContent(expense = expense, removeOuterBorder = true)
            }
        )
    }

    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun TransactionItemContent(expense: ExpenseEntity, removeOuterBorder: Boolean = false) {
    val icon = Utils.getItemIcon(expense)
    val amount = if (expense.type == "Income") expense.amount else expense.amount * -1

    TransactionItem(
        title = expense.title,
        amount = Utils.formatCurrency(amount),
        icon = icon,
        date = Utils.formatStringDateToMonthDayYear(expense.date),
        color = if (expense.type == "Income") line else Color.Black,
        modifier = Modifier,
        removeOuterBorder = removeOuterBorder
    )
}


@Composable
fun TransactionItem(
    title: String,
    amount: String,
    icon: Int,
    date: String,
    color: Color,
    modifier: Modifier,
    removeOuterBorder: Boolean = false
) {
    println("TransactionItem : $removeOuterBorder")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (!removeOuterBorder) {
                    Modifier.border(
                        BorderStroke(2.dp, line),
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier
                }
            )
            .background(
                Color.White,
                shape = if (removeOuterBorder) RectangleShape else RoundedCornerShape(16.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bgColor)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                ExpenseTextView(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                ExpenseTextView(text = date, fontSize = 13.sp, color = Grey30)
            }
        }
        ExpenseTextView(
            text = amount,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}



@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, imaget: Int) {
    Column(modifier = modifier) {
        Row {

            Image(
                painter = painterResource(id = imaget),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            ExpenseTextView(text = title, color = Color.White)
        }
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseTextView(text = amount, color = Color.White)
    }
}


@Composable
fun ExpenseTextView(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {

    Text(
        text = text,
        modifier,
        color,
        fontSize,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        minLines,
        onTextLayout,
        style

    )
}

@Preview
@Composable
fun Preview(){
    BottomSheet(
        modifier = Modifier,
        onAddExpenseClick = {},
        isIncome = true
    )
}

@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    onAddExpenseClick: (model: ExpenseEntity) -> Unit,
    isIncome: Boolean
) {
    val name = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val date = remember { mutableLongStateOf(0L) }
    val dateDialogVisibility = remember { mutableStateOf(false) }
    val type = remember { mutableStateOf(if (isIncome) "Income" else "Expense") }
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF8E53))
    )
    val scrollState = rememberScrollState()
    val customFont = FontFamily(
        Font(R.font.exo2_extrabold, FontWeight.Normal)
    )

    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = if (isIncome) "Add Income" else "Add Expense",
                color = White,
                fontFamily = customFont,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TitleComponent(title = "Name")
        ExpenseDropDown(
            listOfItems = if (isIncome) listOf(
                "Paypal", "Salary", "Freelance", "Investments", "Bonus",
                "Rental Income", "Other Income"
            ) else listOf(
                "Grocery", "Netflix", "Rent", "Paypal", "Starbucks", "Shopping",
                "Transport", "Utilities", "Dining Out", "Entertainment", "Healthcare",
                "Insurance", "Subscriptions", "Education", "Debt Payments",
                "Gifts & Donations", "Travel", "Other Expenses", "Kite", "Coin"
            ),
            onItemSelected = { name.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TitleComponent("Amount")
        OutlinedTextField(
            value = amount.value,
            onValueChange = { amount.value = it.filter { ch -> ch.isDigit() || ch == '.' } },
            visualTransformation = {
                TransformedText(
                    AnnotatedString("$${it.text}"),
                    object : OffsetMapping {
                        override fun originalToTransformed(offset: Int) = offset + 1
                        override fun transformedToOriginal(offset: Int) = if (offset > 0) offset - 1 else 0
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(fontSize = 12.sp, color = White),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { ExpenseTextView(text = "Enter amount") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = White,
                unfocusedBorderColor = White,
                disabledBorderColor = White,
                disabledTextColor = Color.White,
                disabledPlaceholderColor = Color.Black,
                focusedTextColor = Color.White,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TitleComponent("Date")
        OutlinedTextField(
            value = if (date.longValue == 0L) "" else Utils.formatDateToHumanReadableForm(date.longValue),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dateDialogVisibility.value = true },
            enabled = false,
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(fontSize = 12.sp, color = Grey50),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = White,
                unfocusedBorderColor = White,
                disabledBorderColor = White,
                disabledTextColor = Color.Black,
                disabledPlaceholderColor = Color.Black,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
            ),
            placeholder = { ExpenseTextView(text = "Select date", color = White) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(line)
                .clickable {
                    val model = ExpenseEntity(
                        null,
                        name.value,
                        amount.value.toDoubleOrNull() ?: 0.0,
                        Utils.formatDateToHumanReadableForm(date.longValue),
                        type.value
                    )
                    onAddExpenseClick(model)
                },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Add ${if (isIncome) "Income" else "Expense"}",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }

    if (dateDialogVisibility.value) {
        ExpenseDatePickerDialog(
            onDateSelected = {
                date.longValue = it
                dateDialogVisibility.value = false
            },
            onDismiss = {
                dateDialogVisibility.value = false
            }
        )
    }
}
