package com.codewithfk.expensetracker.android.feature.transactionlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithfk.expensetracker.android.utils.Utils
import com.example.portfolioapplication.R
import com.example.portfolioapplication.homeScreen.ExpenseTextView
import com.example.portfolioapplication.homeScreen.TransactionItem
import com.example.portfolioapplication.ui.theme.Grey50
import com.example.portfolioapplication.ui.theme.bgColor
import com.example.portfolioapplication.ui.theme.line
import com.example.todoroomdb.db.ExpenseEntity

@Preview
@Composable
fun TransactionsScreenPreview() {
    TransactionListScreen(
        modifier = Modifier,
        state = remember { mutableStateOf(listOf()) },
        isDarkMode = false
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionListScreen(
    modifier: Modifier,
    state: State<List<ExpenseEntity>>,
    isDarkMode: Boolean
) {
    var filterType by remember { mutableStateOf("All") }
    var dateRange by remember { mutableStateOf("All Time") }
    var menuExpanded by remember { mutableStateOf(false) }

    val filteredTransactions = when (filterType) {
        "Expense" -> state.value.filter { it.type == "Expense" }
        "Income" -> state.value.filter { it.type == "Income" }
        else -> state.value
    }

    val filteredByDateRange = filteredTransactions.filter { transaction ->
        true
    }

    Scaffold(
        modifier = modifier,
        containerColor = if (isDarkMode) Color.White.copy(alpha = 0.4f) else bgColor,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    /*Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable { onBackClicked.invoke() },
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                    )*/
                    ExpenseTextView(
                        text = "Transactions",
                        fontSize = 18.sp,
                        color = if (isDarkMode) bgColor else Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable { menuExpanded = !menuExpanded },
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(if (isDarkMode) bgColor else Color.White)
                    )
                    Spacer(modifier = modifier.padding(6.dp))
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 22.dp), color = Grey50)
            }

        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 18.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    AnimatedVisibility(
                        visible = menuExpanded,
                        enter = slideInVertically(initialOffsetY = { -it / 2 }),
                        exit = slideOutVertically(targetOffsetY = { -it }),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Column {
                            ExpenseDropDown(
                                listOfItems = listOf("All", "Expense", "Income"),
                                onItemSelected = { selected ->
                                    filterType = selected
                                    menuExpanded = false
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ExpenseDropDown(
                                listOfItems = listOf(
                                    "Yesterday",
                                    "Today",
                                    "Last 30 Days",
                                    "Last 90 Days",
                                    "Last Year"
                                ),
                                onItemSelected = { selected ->
                                    dateRange = selected
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
                items(filteredByDateRange) { item ->
                    val icon = Utils.getItemIcon(item)
                    val amount = if (item.type == "Income") item.amount else item.amount * -1
                    TransactionItem(
                        title = item.title,
                        amount = Utils.formatCurrency(amount),
                        icon = icon,
                        date = item.date,
                        color = if (item.type == "Income") line else Color.Black,
                        Modifier.animateItemPlacement(tween(100))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDown(listOfItems: List<String>, onItemSelected: (item: String) -> Unit) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val selectedItem = remember {
        mutableStateOf(listOfItems[0])
    }
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = it }) {
        androidx.compose.material.OutlinedTextField(
            value = selectedItem.value,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .border(
                    BorderStroke(0.5.dp, Color(0xFFD1D1D1)),
                    RoundedCornerShape(25.dp)
                ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color(0xFFD1D1D1),
                unfocusedBorderColor = Color(0xFFD1D1D1)
            ),
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = Color(0xFF757575)
            ),
            shape = RoundedCornerShape(24.dp),
            placeholder = {
                androidx.compose.material.Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Choose the name",
                    fontSize = 14.sp,
                )
            },
            trailingIcon = {
                //ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                )
            }
        )
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { }) {
            listOfItems.forEach {
                DropdownMenuItem(
                    text = { ExpenseTextView(text = it) },
                    onClick = {
                        selectedItem.value = it
                        onItemSelected(selectedItem.value)
                        expanded.value = false
                    })
            }
        }
    }
}

