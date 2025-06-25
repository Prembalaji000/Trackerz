package com.example.portfolioapplication.homeScreen

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.codewithfk.expensetracker.android.feature.transactionlist.TransactionListScreen
import com.codewithfk.expensetracker.android.utils.Utils
import com.example.portfolioapplication.R
import com.example.portfolioapplication.homeScreen.reportScreen.ReportScreen
import com.example.portfolioapplication.homeScreen.reportScreen.ReportScreenRouter
import com.example.portfolioapplication.showCase.IntroShowcase
import com.example.portfolioapplication.showCase.theme.component.ShowcaseStyle
import com.example.portfolioapplication.showCase.theme.component.introShowcaseTarget
import com.example.portfolioapplication.showCase.theme.component.rememberIntroShowcaseState
import com.example.portfolioapplication.signUpScreen.AnimatedLoader
import com.example.portfolioapplication.ui.theme.Grey30
import com.example.portfolioapplication.ui.theme.Zinc
import com.example.portfolioapplication.ui.theme.bgColor
import com.example.todoroomdb.db.ExpenseEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.runtime.State
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.vectorResource
import com.example.portfolioapplication.settingScreen.SettingScreen

@Preview
@Composable
fun HomeScreenRevampPreview() {
    HomeScreenRevamp(
        modifier = Modifier,
        userName = "Prem",
        userImageUrl = "",
        userEmail = "",
        onSeeAllClicked = {},
        expenseList = listOf(),
        onDeleteTransaction = {},
        onAddExpenseClick = {},
        settingButton = {},
        toShowCase = false,
        onShowCaseCompleted = {},
        hasData = false,
        toShowDialog = false,
        onDismissDialog = {},
        onButtonClick = {},
        isLoading = false,
        isRefresh = false,
        onReportClicked = {},
        state = remember { mutableStateOf(listOf()) },
        onSignOut = {},
        onAddButtonClick = {_, _ ->},
        isDarkMode = true,
        onThemeToggle = {},
        onCloudBackUp = {},
        isUploadSuccess = false,
        getData = {}
    )
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeScreenRevamp(
    modifier: Modifier,
    onReportClicked: () -> Unit,
    onSeeAllClicked: () -> Unit,
    settingButton: () -> Unit,
    userName: String,
    userEmail: String,
    userImageUrl: String,
    expenseList: List<ExpenseEntity>,
    onDeleteTransaction: (ExpenseEntity) -> Unit,
    onAddExpenseClick: (model: ExpenseEntity) -> Unit,
    toShowCase: Boolean,
    onShowCaseCompleted: (Boolean) -> Unit,
    hasData: Boolean,
    toShowDialog: Boolean,
    onDismissDialog: (Boolean) -> Unit,
    onButtonClick:() -> Unit,
    isLoading: Boolean,
    state: State<List<ExpenseEntity>>,
    onSignOut: () -> Unit,
    onAddButtonClick: (String, String?) -> Unit,
    isDarkMode: Boolean,
    isRefresh: Boolean,
    onThemeToggle: () -> Unit,
    onCloudBackUp: () -> Unit,
    isUploadSuccess: Boolean,
    getData: () -> Unit
) {
    val navItems = listOf("Home", "Report", "Add", "SeeAll", "Settings")
    var selectedIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val imageLoader = ImageLoader(context)
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var isFromIncome by mutableStateOf(false)
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmValueChange = { false }
    )
    val addSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    var showAppIntro by remember {
        mutableStateOf(true)
    }

    val introShowcaseState = rememberIntroShowcaseState()
    var type by remember { mutableStateOf(false) }

    var selectedMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    var selectedYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val formatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH) }
    val filteredByMonth = expenseList.filter { expenses ->
        try {
            if (expenses.type == "Income") type = true else type = false
            val date = formatter.parse(Utils.formatStringDateToMonthDayYear(expenses.date))
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.get(Calendar.MONTH) == selectedMonth && calendar.get(Calendar.YEAR) == selectedYear
        } catch (e: Exception) {
            false
        }
    }
    val filteredExpenses = filteredByMonth.filter { it.type.equals("Expense", ignoreCase = true) }
    val filteredIncome = filteredByMonth.filter { it.type.equals("Income", ignoreCase = true) }
    val totalBalance = filteredIncome.sumOf { it.amount } - filteredExpenses.sumOf { it.amount }

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
    ){
        ModalBottomSheetLayout(
            sheetState = addSheetState,
            sheetBackgroundColor = bgColor,
            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            sheetContent = {
                ChooseCategoryBottomSheet(
                    onAddExpenseClicked = {
                        scope.launch {
                            addSheetState.hide()
                            isFromIncome = false
                            sheetState.show()
                            showBottomSheet = true
                        }
                    },
                    onAddIncomeClicked = {
                        scope.launch {
                            addSheetState.hide()
                            isFromIncome = true
                            sheetState.show()
                            showBottomSheet = true
                        }
                    }
                )
            }
        ) {
            ModalBottomSheetLayout(
                sheetState = sheetState,
                sheetBackgroundColor = bgColor,
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
                AnimatedLoader(isLoading = isLoading)
                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color(0xFF181818)
                        ) {
                            navItems.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedIndex == index,
                                    onClick = {
                                        if (item == "Add") {
                                            scope.launch {
                                                addSheetState.show()
                                            }
                                        } else {
                                            selectedIndex = index
                                            when (item) {
                                                "Home" -> selectedIndex = 0
                                                "Report" -> selectedIndex = 1
                                                "SeeAll" -> selectedIndex = 3
                                                "Settings" -> selectedIndex = 4
                                            }
                                        }
                                    },
                                    icon = {
                                        when(item){
                                            "Home" -> {
                                                Icon(
                                                    modifier = Modifier.introShowCaseTarget(
                                                        index = 0,
                                                        style = ShowcaseStyle.Default.copy(
                                                            backgroundColor = Color(0xFF9AD0EC),
                                                            backgroundAlpha = 0.98f,
                                                            targetCircleColor = White
                                                        ),
                                                        content = {
                                                            Column {
                                                                Image(
                                                                    painterResource(id = R.drawable.search_example),
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(100.dp)
                                                                )
                                                                Spacer(modifier = Modifier.padding(4.dp))
                                                                Text(
                                                                    text = "Home View",
                                                                    color = Black,
                                                                    fontSize = 24.sp,
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                                Spacer(modifier = Modifier.padding(4.dp))
                                                                Text(
                                                                    text = "You can view, update and delete expenses by swiping on left on item",
                                                                    color = Black,
                                                                    fontSize = 16.sp
                                                                )
                                                            }
                                                        }
                                                    ),
                                                    imageVector = Icons.Default.Home,
                                                    contentDescription = item,
                                                    tint = if (selectedIndex == index) White else Color.Gray
                                                )
                                            }
                                            "Report" -> {
                                                Icon(
                                                    modifier = Modifier.introShowCaseTarget(
                                                        index = 1,
                                                        style = ShowcaseStyle.Default.copy(
                                                            backgroundColor = Color(0xFF9AD0EC),
                                                            backgroundAlpha = 0.98f,
                                                            targetCircleColor = White
                                                        ),
                                                        content = {
                                                            Column {
                                                                Image(
                                                                    painterResource(id = R.drawable.ic_report_icon),
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(100.dp)
                                                                )
                                                                Spacer(modifier = Modifier.padding(4.dp))
                                                                Text(
                                                                    text = "Reports Screen",
                                                                    color = Black,
                                                                    fontSize = 24.sp,
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                                Spacer(modifier = Modifier.padding(4.dp))
                                                                Text(
                                                                    text = "You can view all expenses and income in a month in pie chart view",
                                                                    color = Black,
                                                                    fontSize = 16.sp
                                                                )
                                                            }
                                                        }
                                                    ),
                                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_report),
                                                    contentDescription = item,
                                                    tint = if (selectedIndex == index) White else Color.Gray
                                                )
                                            }
                                            "Add" -> {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .align(Alignment.Top),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(8.dp)
                                                            .size(60.dp)
                                                            .clip(RoundedCornerShape(16.dp))
                                                            .background(color = Zinc)
                                                            .introShowCaseTarget(
                                                                index = 2,
                                                                style = ShowcaseStyle.Default.copy(
                                                                    backgroundColor = Color(
                                                                        0xFF1C0A00
                                                                    ),
                                                                    backgroundAlpha = 0.98f,
                                                                    targetCircleColor = White
                                                                ),
                                                                content = {
                                                                    Column {
                                                                        Image(
                                                                            painterResource(id = R.drawable.ic_expenses),
                                                                            contentDescription = null,
                                                                            modifier = Modifier.size(
                                                                                82.dp
                                                                            )
                                                                        )
                                                                        Spacer(
                                                                            modifier = Modifier.padding(
                                                                                4.dp
                                                                            )
                                                                        )
                                                                        Text(
                                                                            text = "To add item",
                                                                            color = White,
                                                                            fontSize = 24.sp,
                                                                            fontWeight = FontWeight.Bold
                                                                        )
                                                                        Spacer(
                                                                            modifier = Modifier.padding(
                                                                                4.dp
                                                                            )
                                                                        )
                                                                        Text(
                                                                            text = "You can add income and expenses",
                                                                            color = White,
                                                                            fontSize = 16.sp
                                                                        )
                                                                    }
                                                                }
                                                            )
                                                            .clickable {
                                                                scope.launch {
                                                                    addSheetState.show()
                                                                }
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
                                            "SeeAll" -> {
                                                Icon(
                                                    modifier = Modifier.introShowCaseTarget(
                                                        index = 3,
                                                        style = ShowcaseStyle.Default.copy(
                                                            backgroundColor = Color(0xFF9AD0EC),
                                                            backgroundAlpha = 0.98f,
                                                            targetCircleColor = White
                                                        ),
                                                        content = {
                                                            Column {
                                                                Image(
                                                                    painterResource(id = R.drawable.ic_transaction),
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(100.dp)
                                                                )
                                                                Spacer(modifier = Modifier.padding(4.dp))
                                                                Text(
                                                                    text = "Transaction Screen",
                                                                    color = Black,
                                                                    fontSize = 24.sp,
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                                Spacer(modifier = Modifier.padding(4.dp))
                                                                Text(
                                                                    text = "You can view all income and expense",
                                                                    color = Black,
                                                                    fontSize = 16.sp
                                                                )
                                                            }
                                                        }
                                                    ),
                                                    imageVector = Icons.Default.List,
                                                    contentDescription = item,
                                                    tint = if (selectedIndex == index) White else Color.Gray
                                                )
                                            }
                                            "Settings" -> {
                                                Icon(
                                                    modifier = Modifier.introShowCaseTarget(
                                                        index = 4,
                                                        style = ShowcaseStyle.Default.copy(
                                                            backgroundColor = Color(0xFF9AD0EC),
                                                            backgroundAlpha = 0.98f,
                                                            targetCircleColor = White
                                                        ),
                                                        content = {
                                                            Column {
                                                                Image(
                                                                    painterResource(id = R.drawable.ic_user_setting),
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(100.dp)
                                                                )
                                                                Spacer(modifier = Modifier.padding(4.dp))
                                                                Text(
                                                                    text = "Setting Screen",
                                                                    color = Black,
                                                                    fontSize = 24.sp,
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                                Spacer(modifier = Modifier.padding(4.dp))
                                                                Text(
                                                                    text = "You can view, upload data to cloud and edit your profile",
                                                                    color = Black,
                                                                    fontSize = 16.sp
                                                                )
                                                            }
                                                        }
                                                    ),
                                                    imageVector = Icons.Default.Settings,
                                                    contentDescription = item,
                                                    tint = if (selectedIndex == index) White else Color.Gray
                                                )
                                            }
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = White,
                                        unselectedIconColor = Color.Gray,
                                        indicatorColor = Color(0xFF242424)
                                    )
                                )
                            }
                        }
                    },
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding(), top = paddingValues.calculateTopPadding())
                    ) {
                        when (selectedIndex) {
                            0 -> {
                                RevampHomeScreen(
                                    userName = userName,
                                    userImageUrl = userImageUrl,
                                    expenseList = expenseList,
                                    onDeleteTransaction = onDeleteTransaction,
                                    onReportClicked = onReportClicked,
                                    onSeeAllClicked = onSeeAllClicked,
                                    settingButton = settingButton,
                                    isDarkMode = isDarkMode
                                )
                            }
                            1 -> {
                                ReportScreen(
                                    modifier = Modifier,
                                    isDarkMode = isDarkMode,
                                    expenses = expenseList,
                                    onBackButtonClick = { }
                                )
                            }
                            2 -> {}
                            3 -> {
                                TransactionListScreen(
                                    modifier = Modifier,
                                    state = state,
                                    onBackClicked = { },
                                    isDarkMode = isDarkMode
                                )
                            }
                            4 -> {
                                SettingScreen(
                                    modifier = Modifier,
                                    onBackButtonClick = { },
                                    onSignOut = onSignOut,
                                    initialUserName = userName,
                                    userEmail = userEmail,
                                    userImageUrl = userImageUrl,
                                    addButtonClick = onAddButtonClick,
                                    isRefresh = isRefresh,
                                    isDarkMode = isDarkMode,
                                    onThemeToggle = onThemeToggle,
                                    onCloudBackUp = onCloudBackUp,
                                    isUploadSuccess = isUploadSuccess,
                                    getData = getData
                                )
                            }
                        }
                    }
                    if (hasData && toShowDialog){
                        StoreDataDialog(
                            onDismiss = { onDismissDialog(false) },
                            onButtonClick = onButtonClick
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun RevampHomeScreen(
    userName: String,
    userImageUrl: String,
    expenseList: List<ExpenseEntity>,
    onDeleteTransaction: (ExpenseEntity) -> Unit,
    onReportClicked: () -> Unit,
    onSeeAllClicked: () -> Unit,
    settingButton: () -> Unit,
    isDarkMode: Boolean
){
    val context = LocalContext.current
    val imageLoader = ImageLoader(context)
    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
    var type by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    var selectedYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val formatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH) }
    val filteredByMonth = expenseList.filter { expenses ->
        try {
            if (expenses.type == "Income") type = true else type = false
            val date = formatter.parse(Utils.formatStringDateToMonthDayYear(expenses.date))
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.get(Calendar.MONTH) == selectedMonth && calendar.get(Calendar.YEAR) == selectedYear
        } catch (e: Exception) {
            false
        }
    }
    val filteredExpenses = filteredByMonth.filter { it.type.equals("Expense", ignoreCase = true) }
    val filteredIncome = filteredByMonth.filter { it.type.equals("Income", ignoreCase = true) }
    val totalBalance = filteredIncome.sumOf { it.amount } - filteredExpenses.sumOf { it.amount }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkMode) Color.White.copy(alpha = 0.4f) else bgColor)
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
                            text = greeting,
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
            }
            Column(
                modifier = Modifier
                    .padding(top = 100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardItem(
                    modifier = Modifier,
                    balance = Utils.formatCurrency(totalBalance),
                    income = Utils.formatCurrency(filteredIncome.sumOf { it.amount }),
                    expense = Utils.formatCurrency(filteredExpenses.sumOf { it.amount }),
                    onReportClicked = onReportClicked
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    MonthTab(
                        onMonthChanged = { month, year ->
                            selectedMonth = month
                            selectedYear = year
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TransactionList(
                        modifier = Modifier.fillMaxWidth(),
                        list = filteredByMonth,
                        isDarkMode = isDarkMode,
                        onSeeAllClicked = onSeeAllClicked,
                        onDeleteTransaction = onDeleteTransaction
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Previews(){
    ChooseCategoryBottomSheet(
        onAddExpenseClicked = {},
        onAddIncomeClicked = {}
    )
}

@Composable
fun ChooseCategoryBottomSheet(
    onAddExpenseClicked: () -> Unit,
    onAddIncomeClicked: () -> Unit
) {
    val customFont = FontFamily(
        Font(R.font.exo2_extrabold, FontWeight.Normal)
    )
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            androidx.compose.material3.Text(
                text = "Choose Category",
                color = White,
                fontFamily = customFont,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Button(
            onClick = { onAddIncomeClicked.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(
                text = "Add Income",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Button(
            onClick = { onAddExpenseClicked.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Grey30),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(
                text = "Add Expense",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
    }
}

