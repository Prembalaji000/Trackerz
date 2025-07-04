@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.portfolioapplication.homeScreen.add_expense

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.codewithfk.expensetracker.android.utils.Utils
import com.example.portfolioapplication.homeScreen.ExpenseTextView
import com.example.portfolioapplication.ui.theme.Grey30
import com.example.todoroomdb.db.ExpenseEntity
import com.example.portfolioapplication.R
import com.example.portfolioapplication.ui.theme.Grey50
import com.example.portfolioapplication.ui.theme.bgColor
import com.example.portfolioapplication.ui.theme.line

@Composable
fun AddExpense(
    modifier: Modifier,
    isIncome: Boolean,
    onAddExpenseClick: (model: ExpenseEntity) -> Unit,
    onBackPressed: () -> Unit,
    onMenuClicked: () -> Unit
) {
    val menuExpanded = remember { mutableStateOf(false) }
   /* LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                NavigationEvent.NavigateBack -> navController.popBackStack()
                AddExpenseNavigationEvent.MenuOpenedClicked -> {
                    menuExpanded.value = true
                }
                else->{}
            }
        }
    }*/
    Surface(modifier = modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize().background(bgColor)) {
            val (nameRow, card, topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Image(painter = painterResource(id = R.drawable.ic_back), contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            onBackPressed.invoke()
                        })
                ExpenseTextView(
                    text = "Add ${if (isIncome) "Income" else "Expense"}",
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    Image(
                        painter = painterResource(id = R.drawable.dots_menu),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable {
                                onMenuClicked.invoke()
                            }
                    )
                    DropdownMenu(
                        expanded = menuExpanded.value,
                        onDismissRequest = { menuExpanded.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { ExpenseTextView(text = "Profile") },
                            onClick = {
                                menuExpanded.value = false
                                // Navigate to profile screen
                                // navController.navigate("profile_route")
                            }
                        )
                        DropdownMenuItem(
                            text = { ExpenseTextView(text = "Settings") },
                            onClick = {
                                menuExpanded.value = false
                                // Navigate to settings screen
                                // navController.navigate("settings_route")
                            }
                        )
                    }
                }

            }
            DataForm(modifier = Modifier.constrainAs(card) {
                top.linkTo(nameRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, onAddExpenseClick = onAddExpenseClick,
                isIncome
            )
        }
    }
}

@Composable
fun DataForm(
    modifier: Modifier,
    onAddExpenseClick: (model: ExpenseEntity) -> Unit,
    isIncome: Boolean
) {

    val name = remember {
        mutableStateOf("")
    }
    val amount = remember {
        mutableStateOf("")
    }
    val date = remember {
        mutableLongStateOf(0L)
    }
    val dateDialogVisibility = remember {
        mutableStateOf(false)
    }
    val type = remember {
        mutableStateOf(if (isIncome) "Income" else "Expense")
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.8.dp, color = line),
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(top = 122.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(bgColor)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TitleComponent(title = "name")
            ExpenseDropDown(
                if (isIncome) listOf(
                    "Paypal",
                    "Salary",
                    "Freelance",
                    "Investments",
                    "Bonus",
                    "Rental Income",
                    "Other Income"
                ) else listOf(
                    "Grocery",
                    "Netflix",
                    "Rent",
                    "Paypal",
                    "Starbucks",
                    "Shopping",
                    "Transport",
                    "Utilities",
                    "Dining Out",
                    "Entertainment",
                    "Healthcare",
                    "Insurance",
                    "Subscriptions",
                    "Education",
                    "Debt Payments",
                    "Gifts & Donations",
                    "Travel",
                    "Other Expenses"
                ),
                onItemSelected = {
                    name.value = it
                })
            Spacer(modifier = Modifier.size(24.dp))
            TitleComponent("amount")
            OutlinedTextField(
                value = amount.value,
                onValueChange = { newValue ->
                    amount.value = newValue.filter { it.isDigit() || it == '.' }
                }, textStyle = TextStyle(color = Color.White),
                visualTransformation = { text ->
                    val out = "$" + text.text
                    val currencyOffsetTranslator = object : OffsetMapping {
                        override fun originalToTransformed(offset: Int): Int {
                            return offset + 1
                        }

                        override fun transformedToOriginal(offset: Int): Int {
                            return if (offset > 0) offset - 1 else 0
                        }
                    }

                    TransformedText(AnnotatedString(out), currencyOffsetTranslator)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.8.dp, line, RoundedCornerShape(4.dp)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { ExpenseTextView(text = "Enter amount") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    disabledBorderColor = Color.Black, disabledTextColor = Color.White,
                    disabledPlaceholderColor = Color.Black,
                    focusedTextColor = Color.White,
                )
            )
            Spacer(modifier = Modifier.size(24.dp))
            TitleComponent("date")
            OutlinedTextField(value = if (date.longValue == 0L) "" else Utils.formatDateToHumanReadableForm(
                date.longValue
            ),
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.8.dp, line, RoundedCornerShape(4.dp))
                    .clickable { dateDialogVisibility.value = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Black, disabledTextColor = Color.White,
                    disabledPlaceholderColor = Color.Black,
                ),
                placeholder = { ExpenseTextView(text = "Select date", color = Color.White) })
            Spacer(modifier = Modifier.size(24.dp))
            Button(
                onClick = {
                    val model = ExpenseEntity(
                        null,
                        name.value,
                        amount.value.toDoubleOrNull() ?: 0.0,
                        Utils.formatDateToHumanReadableForm(date.longValue),
                        type.value,
                    )
                    onAddExpenseClick(model)
                }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)
            ) {
                ExpenseTextView(
                    text = "Add ${if (isIncome) "Income" else "Expense"}",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }

    if (dateDialogVisibility.value) {
        ExpenseDatePickerDialog(onDateSelected = {
            date.longValue = it
            dateDialogVisibility.value = false
        }, onDismiss = {
            dateDialogVisibility.value = false
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePickerDialog(
    onDateSelected: (date: Long) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L
    DatePickerDialog(onDismissRequest = { onDismiss() }, confirmButton = {
        TextButton(onClick = { onDateSelected(selectedDate) }) {
            ExpenseTextView(text = "Confirm")
        }
    }, dismissButton = {
        TextButton(onClick = { onDateSelected(selectedDate) }) {
            ExpenseTextView(text = "Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun TitleComponent(title: String) {
    val customFont = FontFamily(
        Font(R.font.exo2_extrabold, FontWeight.Normal)
    )
    ExpenseTextView(
        text = title.uppercase(),
        fontSize = 12.sp,
        fontFamily = customFont,
        fontWeight = FontWeight.Medium,
        color = White
    )
    Spacer(modifier = Modifier.size(10.dp))
}

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

@Preview(showBackground = true)
@Composable
fun PreviewAddExpense() {
    AddExpense(Modifier,true, {}, {}, {})
}

