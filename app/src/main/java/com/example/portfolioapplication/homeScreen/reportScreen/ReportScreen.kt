package com.example.portfolioapplication.homeScreen.reportScreen

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithfk.expensetracker.android.utils.Utils
import com.example.portfolioapplication.R
import com.example.portfolioapplication.ui.theme.Grey50
import com.example.portfolioapplication.ui.theme.bgColor
import com.example.todoroomdb.db.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun ReportScreen(
    modifier: Modifier,
    expenses: List<ExpenseEntity>,
    isDarkMode: Boolean,
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = if (isDarkMode) Color.White.copy(alpha = 0.4f) else bgColor,
        topBar = {
            TopBar(isDarkMode = isDarkMode)
        },
        content = { paddingValue ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(color = if (isDarkMode) Color.White.copy(alpha = 0.4f) else bgColor)
                    .padding(paddingValue),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Transactions by category",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CategoryPieChart(expenses = expenses)
                }
            }

        }
    )
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
                text = "Reports",
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
fun ReportScreenPreview() {
    ReportScreen(
        modifier = Modifier,
        expenses = listOf(
            ExpenseEntity(
                id = 1,
                title = "Netflix",
                amount = 3000.00,
                date = "01/05/2025",
                type = "Income",
            ),
            ExpenseEntity(
                id = 2,
                title = "Spotify",
                amount = 2500.00,
                date = "01/06/2025",
                type = "Expense"
            ),
            ExpenseEntity(
                id = 3,
                title = "Kite",
                amount = 1000.00,
                date = "01/06/2025",
                type = "Expense"
            ),
            ExpenseEntity(
                id = 4,
                title = "paypal",
                amount = 1000.00,
                date = "01/06/2025",
                type = "Expense"
            ),
            ExpenseEntity(
                id = 5,
                title = "coin",
                amount = 2500.00,
                date = "01/06/2025",
                type = "Expense"
            ),
            ExpenseEntity(
                id = 6,
                title = "salary",
                amount = 10000.00,
                date = "01/05/2025",
                type = "Income"
            )
        ),
        isDarkMode = false,
    )
}


@SuppressLint("DefaultLocale")
@Composable
fun CategoryPieChart(expenses: List<ExpenseEntity>) {

    var selectedMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    var selectedYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var type by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    val formatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH) }
    val filteredByMonth = expenses.filter { expense ->
        try {
            val date = formatter.parse(Utils.formatStringDateToMonthDayYear(expense.date))
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.get(Calendar.MONTH) == selectedMonth && calendar.get(Calendar.YEAR) == selectedYear
        } catch (e: Exception) {
            false
        }
    }

    val filteredExpenses = filteredByMonth.filter { it.type.equals("Expense", ignoreCase = true) }
    val filteredIncome = filteredByMonth.filter { it.type.equals("Income", ignoreCase = true) }
    val filteredItem = if (type) filteredIncome else filteredExpenses

    val totalExpenses = filteredItem.sumOf { it.amount }.toFloat()
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(filteredItem) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            if (filteredItem.isEmpty() || totalExpenses <= 0f) {
                drawArc(
                    color = Color.DarkGray.copy(alpha = 0.3f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Butt)
                )
            } else {
                var startAngle = -90f
                filteredItem.forEach { expense ->
                    val sweepAngle = (expense.amount / totalExpenses) * 360f
                    val animatedSweep = sweepAngle * animatedProgress.value

                    drawArc(
                        color = getCategoryColor(expense.title),
                        startAngle = startAngle,
                        sweepAngle = animatedSweep.toFloat(),
                        useCenter = false,
                        style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Butt)
                    )
                    startAngle += sweepAngle.toFloat()
                }
            }
        }

        Spacer(modifier = Modifier.height(34.dp))

        MonthSelector(
            selectedMonth = selectedMonth,
            selectedYear = selectedYear,
            onMonthChanged = { month, year ->
                selectedMonth = month
                selectedYear = year
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .background(Color(0xFF181818), shape = RoundedCornerShape(16.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabItem(
                text = "Expense",
                isSelected = selectedTab == 0,
                onClick = {
                    type = false
                    selectedTab = 0
                }
            )
            TabItem(
                text = "Income",
                isSelected = selectedTab == 1,
                onClick = {
                    type = true
                    selectedTab = 1
                }
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .border(width = 1.dp, color = Grey50, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF181818))
        ) {
            filteredItem.forEach { item ->
                val percentage = (item.amount / totalExpenses) * 100
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(RoundedCornerShape(50))
                                .background(getCategoryColor(item.title))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${item.title} | ₹${item.amount} | ${
                                String.format(
                                    "%.1f%%",
                                    percentage
                                )
                            }",
                            fontSize = 13.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = Grey50
                    )
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                text = "Total Balance: ₹$totalExpenses",
                fontSize = 13.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
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


fun getCategoryColor(type: String): Color {
    return when (type.lowercase()) {
        "paypal" -> Color(0xFF1E88E5)
        "netflix" -> Color(0xFFD32F2F)
        "starbucks" -> Color(0xFF388E3C)
        "kite" -> Color(0xFFFBC02D)
        "coin" -> Color(0xFF8D6E63)
        "spotify" -> Color(0xFF43A047)
        "youtube" -> Color(0xFFE53935)
        "other expenses" -> Color(0xFF757575)
        "debt payment" -> Color(0xFF5C6BC0)
        else -> Color(0xFF90A4AE)
    }.copy(alpha = 0.85f)
}


@Preview
@Composable
fun PreviewScreen() {
    MonthSelector(
        selectedMonth = 6,
        selectedYear = 2025,
        onMonthChanged = { _, _ -> },
    )
}


@Composable
fun MonthSelector(
    selectedMonth: Int,
    selectedYear: Int,
    onMonthChanged: (month: Int, year: Int) -> Unit
) {
    val months = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    Card(
        modifier = Modifier
            .border(width = 1.dp, color = Color(0xFF181818), shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF181818))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
                val newMonth = if (selectedMonth == 0) 11 else selectedMonth - 1
                val newYear = if (selectedMonth == 0) selectedYear - 1 else selectedYear
                onMonthChanged(newMonth, newYear)
            }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Previous Month",
                    tint = Color.White
                )
            }

            Text(
                text = "${months[selectedMonth]} $selectedYear",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )

            IconButton(onClick = {
                val newMonth = if (selectedMonth == 11) 0 else selectedMonth + 1
                val newYear = if (selectedMonth == 11) selectedYear + 1 else selectedYear
                onMonthChanged(newMonth, newYear)
            }) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Next Month",
                    tint = Color.White
                )
            }
        }
    }
}
