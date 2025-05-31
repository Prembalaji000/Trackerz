package com.example.portfolioapplication.homeScreen

import android.annotation.SuppressLint
import android.provider.ContactsContract.Data
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedButton
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
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
import com.example.portfolioapplication.showCase.IntroShowcase
import com.example.portfolioapplication.showCase.IntroShowcaseScope
import com.example.portfolioapplication.showCase.theme.component.ShowcaseStyle
import com.example.portfolioapplication.showCase.theme.component.rememberIntroShowcaseState
import com.example.portfolioapplication.ui.theme.Grey50
import com.example.portfolioapplication.ui.theme.bgColor
import com.example.portfolioapplication.ui.theme.line
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
        settingButton = {},
        toShowCase = false,
        onShowCaseCompleted = {}
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
    toShowCase: Boolean,
    onShowCaseCompleted: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader(context)
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var isFromIncome by mutableStateOf(false)
    val sheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmValueChange = { false }
    )
    var showAppIntro by remember {
        mutableStateOf(true)
    }

    val introShowcaseState = rememberIntroShowcaseState()
    LaunchedEffect(sheetState) {
        scope.launch {
            if (sheetState.isVisible){
                sheetState.hide()
            }
        }
    }

    IntroShowcase(
        showIntroShowCase = toShowCase,
        dismissOnClickOutside = false,
        onShowCaseCompleted = {
            onShowCaseCompleted(false)
            showAppIntro = false
        },
        state = introShowcaseState,
    ) {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            sheetContent = {
                BottomSheet(
                    onAddExpenseClick = {
                        onAddExpenseClick.invoke(it)
                        scope.launch {
                            showBottomSheet = false
                            sheetState.hide()
                        }
                    },
                    onCancelCLick = {
                        scope.launch {
                            sheetState.hide()
                        } },
                    isIncome = isFromIncome
                )
            }
        ){
            Surface(
                modifier = modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(bgColor)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_topbar),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 34.dp, start = 16.dp, end = 16.dp)
                            .align(Alignment.TopCenter)
                    ) {
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
                        IconButton(
                            onClick = {},
                            modifier = Modifier.introShowCaseTarget(
                                index = 0,
                                style = ShowcaseStyle.Default.copy(
                                    backgroundColor = Color(0xFF9AD0EC), // specify color of background
                                    backgroundAlpha = 0.98f, // specify transparency of background
                                    targetCircleColor = Color.White // specify color of target circle
                                ),
                                content = {
                                    Column {
                                        Image(
                                            painterResource(id = R.drawable.search_example),
                                            contentDescription = null,
                                            modifier = Modifier.size(100.dp)
                                        )

                                        androidx.compose.material.Text(
                                            text = "Profile View!!",
                                            color = Color.Black,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        androidx.compose.material.Text(
                                            text = "You can view and edit your profile",
                                            color = Color.Black,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                           ).align(Alignment.CenterEnd)
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.ic_profile_icon),
                                contentScale = ContentScale.Fit,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(color = White),
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .size(26.dp)
                                    .clickable { settingButton.invoke() }
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(top = 100.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CardItem(
                            modifier = Modifier,
                            balance = balance,
                            income = income,
                            expense = expense
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            TransactionList(
                                modifier = Modifier.fillMaxWidth(),
                                list = expenseList,
                                onSeeAllClicked = onSeeAllClicked,
                                onDeleteTransaction = onDeleteTransaction
                            )
                        }
                    }
Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        MultiFloatingActionButton(
                            modifier = modifier,
                            onAddExpenseClicked = {
                                scope.launch {
                                    isFromIncome = false
                                    sheetState.show()
                                    showBottomSheet = true
                                }
                            },
                            onAddIncomeClicked = {
                                scope.launch {
                                    isFromIncome = true
                                    sheetState.show()
                                    showBottomSheet = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun IntroShowcaseScope.MultiFloatingActionButton(
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
                    .introShowCaseTarget(
                        index = 1,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = Color(0xFF1C0A00),
                            backgroundAlpha = 0.98f,
                            targetCircleColor = Color.White
                        ),
                        content = {
                            Column {
                                Image(
                                    painterResource(id = R.drawable.ic_expenses),
                                    contentDescription = null,
                                    modifier = Modifier.size(82.dp)
                                )

                                androidx.compose.material.Text(
                                    text = "To add item",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                androidx.compose.material.Text(
                                    text = "You can add income and expenses",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    )
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
    val context = LocalContext.current

    if (list.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize(),
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
        Column {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
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
        LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
            items(items = list,
                key = { item -> item.id ?: 0 }) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = 0.dp,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                ){
                    TransactionItemContent(
                        expense = item,
                        isRevealed = false,
                        onExpanded = {},
                        onCollapsed = {},
                        onDeleteClick = {
                            Toast.makeText(context, "Transaction Deleted Successfully", Toast.LENGTH_SHORT).show()
                            onDeleteTransaction(item) },
                        onEditClick = {},
                        modifier = modifier,
                        title = item.title,
                        date = Utils.formatStringDateToMonthDayYear(item.date),
                        color = if (item.type == "Income") line else Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun TransactionItemContent(
    expense: ExpenseEntity,
    isRevealed: Boolean,
    onExpanded: () -> Unit,
    onCollapsed: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier,
    title: String,
    date: String,
    color: Color
) {
    val icon = Utils.getItemIcon(expense)
    val amount = if (expense.type == "Income") expense.amount else expense.amount * -1

    SwipeableItemWithActions(
        isRevealed = isRevealed,
        onExpanded = onExpanded,
        onCollapsed = onCollapsed,
        actions = {
            /*ActionIcon(
                onClick = onEditClick,
                backgroundColor = Color(0xFF4CAF50),
                icon = Icons.Default.Edit,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(64.dp)
            )*/
            ActionIcon(
                onClick = onDeleteClick,
                backgroundColor = Color(0xFFF44336),
                icon = Icons.Default.Delete,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(64.dp)
            )
        },
        modifier = modifier
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 0.dp,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                        ExpenseTextView(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        ExpenseTextView(
                            text = date,
                            fontSize = 13.sp,
                            color = Grey30
                        )
                    }
                }
                ExpenseTextView(
                    text = Utils.formatCurrency(amount),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = color
                )
            }
        }
    }
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
        onAddExpenseClick = {},
        onCancelCLick = {},
        isIncome = true
    )
}

@Composable
fun BottomSheet(
    onAddExpenseClick: (model: ExpenseEntity) -> Unit,
    onCancelCLick: () -> Unit = {},
    isIncome: Boolean
) {
    val name = remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val date = remember { mutableLongStateOf(0L) }
    val dateDialogVisibility = remember { mutableStateOf(false) }
    val type = rememberUpdatedState(if (isIncome) "Income" else "Expense")
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF8E53))
    )
    val scrollState = rememberScrollState()
    val customFont = FontFamily(
        Font(R.font.exo2_extrabold, FontWeight.Normal)
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val TitleInteraction = remember { MutableInteractionSource() }
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .background(bgColor)
            .padding(16.dp)
            .imePadding()
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
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            modifier = Modifier.padding(start = 6.dp, bottom = 8.dp),
            text = "Choose a category",
            color = White,
            fontSize = 14.sp
        )
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

        Spacer(modifier = Modifier.height(8.dp))

        /*TitleComponent("Amount")
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it.filter { ch -> ch.isDigit() || ch == '.' } },
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
        )*/
        Text(
            modifier = Modifier.padding(start = 6.dp, top = 8.dp),
            text = "Enter amount",
            color = White,
            fontSize = 14.sp
        )
        androidx.compose.material.OutlinedTextField(
            value = amount,
            onValueChange = { amount = it.filter { ch -> ch.isDigit() || ch == '.' } },
            visualTransformation = {
                TransformedText(
                    AnnotatedString("$${it.text}"),
                    object : OffsetMapping {
                        override fun originalToTransformed(offset: Int) = offset + 1
                        override fun transformedToOriginal(offset: Int) = if (offset > 0) offset - 1 else 0
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent {
                    isFocused = it.isFocused
                }
                .padding(top = 10.dp)
                .border(BorderStroke(0.5.dp, Color(0xFFD1D1D1)), RoundedCornerShape(25.dp)),
            singleLine = true,
            interactionSource = TitleInteraction,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            placeholder = {
                androidx.compose.material.Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Enter amount",
                    fontSize = 14.sp,
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color(0xFFD1D1D1),
                unfocusedBorderColor = Color(0xFFD1D1D1),
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(25.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(start = 6.dp, top = 8.dp),
            text = "Select data",
            color = White,
            fontSize = 14.sp
        )
        androidx.compose.material.OutlinedTextField(
            value = if (date.longValue == 0L) "" else Utils.formatDateToHumanReadableForm(date.longValue),
            onValueChange = { },
            visualTransformation = VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent {
                    isFocused = it.isFocused
                }
                .padding(top = 10.dp)
                .border(BorderStroke(0.5.dp, Color(0xFFD1D1D1)), RoundedCornerShape(25.dp))
                .clickable { dateDialogVisibility.value = true },
            enabled = false,
            textStyle = TextStyle(color = Color(0xFF757575)),
            singleLine = true,
            interactionSource = TitleInteraction,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            placeholder = {
                androidx.compose.material.Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "DD/MM/YYYY",
                    fontSize = 14.sp,
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color(0xFFD1D1D1),
                unfocusedBorderColor = Color(0xFFD1D1D1),
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(25.dp),
        )

        Spacer(modifier = Modifier.height(18.dp))
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), color = White)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            OutlinedButton(
                onClick = {
                    keyboardController?.hide()
                    onCancelCLick.invoke()
                    amount = ""
                    name.value = ""
                    date.longValue = 0
                },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(25.dp),
                border = BorderStroke(1.dp, Color(0xFFD1D1D1)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = bgColor)
            ) {
                androidx.compose.material.Text(
                    text = "Cancel",
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
            }
            androidx.compose.material.Button(
                onClick = {
                    keyboardController?.hide()
                    val model = ExpenseEntity(
                        null,
                        name.value,
                        amount.toDoubleOrNull() ?: 0.0,
                        Utils.formatDateToHumanReadableForm(date.longValue),
                        type.value,
                    )
                    when{
                        model.title.isEmpty() -> {
                            Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
                        }
                        model.amount == 0.0 -> {
                            Toast.makeText(context, "Please enter an amount", Toast.LENGTH_SHORT).show()
                        }
                        model.date == "01/01/1970" -> {
                            Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            println("model: ${model.date}")
                            onAddExpenseClick(model)
                            Toast.makeText(context, "Expense added successfully", Toast.LENGTH_SHORT).show()
                            amount = ""
                            name.value = ""
                            date.longValue = 0
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = line),
                shape = RoundedCornerShape(25.dp)
            ) {
                androidx.compose.material.Text(
                    text = "Add ${if (isIncome) "Income" else "Expense"}",
                    fontSize = 13.sp,
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




@Composable
fun ActionIcon(
    onClick: () -> Unit,
    backgroundColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = Color.White
) {
    androidx.compose.material3.IconButton(
        onClick = onClick,
        modifier = modifier
            .background(backgroundColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Composable
fun SwipeableItemWithActions(
    isRevealed: Boolean,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var contextMenuWidth by remember {
        mutableFloatStateOf(0f)
    }
    val offset = remember {
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isRevealed, contextMenuWidth) {
        if(isRevealed) {
            offset.animateTo(contextMenuWidth)
        } else {
            offset.animateTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .onSizeChanged {
                    contextMenuWidth = it.width.toFloat()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(contextMenuWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(0f, contextMenuWidth)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            when {
                                offset.value >= contextMenuWidth / 2f -> {
                                    scope.launch {
                                        offset.animateTo(contextMenuWidth)
                                        onExpanded()
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        offset.animateTo(0f)
                                        onCollapsed()
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}