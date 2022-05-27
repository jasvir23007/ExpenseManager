package com.expense.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.R
import com.expense.extensions.swapList
import com.expense.ui.dialogs.CustomDialog
import com.expense.model.TransactionDetails
import com.expense.model.Transactions
import com.expense.utils.Constants.EXPENSE_LIST
import com.expense.utils.Constants.DELETE
import com.expense.utils.Constants.INCOME


/**
 * Created by Jasvir Partap Singh on 20,May,2022
 */

lateinit var viewModel: ExpenseViewModel

@ExperimentalFoundationApi
@Composable
fun MainView(vm: ExpenseViewModel) {
    val myList = remember { mutableStateListOf<Transactions>() }
    var expense by remember { mutableStateOf("0") }
    var income by remember { mutableStateOf("0") }
    var balance by remember { mutableStateOf("0") }
    viewModel = vm
    Scaffold(

        floatingActionButton = {
            val openDialog = remember { mutableStateOf(false) }
            CustomDialog(openDialogCustom = openDialog, onSelect = { desc, amt, type ->
                viewModel.insertExpenseData(amt, desc, type)
            })

            myList.swapList(viewModel.uiState.collectAsState().value)
            expense = viewModel.expense.collectAsState().value
            income = viewModel.income.collectAsState().value
            balance = viewModel.balance.collectAsState().value


            FloatingActionButton(
                onClick = {
                    openDialog.value = true
                }
            ) {
                Icon(Icons.Filled.Add, "displayDialog")
            }
        }, content = {
            Column(modifier = Modifier.fillMaxSize()) {
                PriceDivisionUi(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp), expense, income, balance
                )
                TransactionList(Modifier.fillMaxWidth(), myList)

            }
        }
    )

}


@ExperimentalFoundationApi
@Composable
fun TransactionList(modifier: Modifier, transactionList: List<Transactions>) {
    LazyColumn(modifier.semantics {
        contentDescription = EXPENSE_LIST
    }) {

        transactionList.forEach { transactions ->

            if (transactions.transactionDetails.isNotEmpty()) {
                stickyHeader {
                    HeaderUI(transactions.date)
                }
                items(transactions.transactionDetails.size) { index ->
                    ChildItemUi(transactions.transactionDetails[index], transactions, index)

                }
            }

        }

    }

}


@Composable
fun ChildItemUi(transactionDetails: TransactionDetails, transactions: Transactions, index: Int) {
    Column{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(.8f)
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = transactionDetails.description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Text(
                    text = if (transactionDetails.type.equals(
                            INCOME,
                            true
                        )
                    ) "$${transactionDetails.amount}" else "-$${transactionDetails.amount}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = if (transactionDetails.type.equals(
                            INCOME,
                            true
                        )
                    ) Color.Green else Color.Red,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            IconButton(modifier = Modifier.then(
                Modifier
                    .size(24.dp)
                    .weight(.1f).semantics {
                        contentDescription = DELETE
                    }
            ),
                onClick = { viewModel.removeItems(transactions.date, index) }) {
                Icon(
                    Icons.Filled.Delete,
                    null,
                    tint = Color.Red
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(start = 10.dp, end = 10.dp)
                .background(Color.Black)
        )
    }

}

@Composable
fun HeaderUI(date: String) {
    Text(
        text = date,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Blue,
        modifier = Modifier.padding(10.dp)
    )

}

@Composable
fun PriceDivisionUi(modifier: Modifier, expense: String, income: String, balance: String) {
    Card(
        border = BorderStroke(1.dp, Color.Black), modifier = Modifier.padding(10.dp)
    ) {
        val expenseIncomeRation = expense.toFloat() / income.toFloat()
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier, horizontalArrangement = Arrangement.SpaceAround) {
                ExpenseUi(stringResource(id = R.string.expenses), "$$expense")
                Spacer(1.dp, 40.dp, Color.Black)
                ExpenseUi(stringResource(id = R.string.income), "$$income")
                Spacer(1.dp, 40.dp, Color.Black)
                ExpenseUi(stringResource(R.string.balance), "$$balance")
            }
            Spacer(50.dp, 1.dp, Color.Transparent)
            Card(
                border = BorderStroke(1.dp, Color.Black)
            ) {
                LinearProgressIndicator(
                    progress = 1 - expenseIncomeRation,
                    modifier = Modifier.height(20.dp)
                )
            }
        }
    }


}

@Composable
fun ExpenseUi(text: String, amount: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = text)
        Text(text = amount)
    }


}

@Composable
fun Spacer(width: Dp, height: Dp, color: Color = Color.Black) {
    Spacer(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(color)
    )
}


