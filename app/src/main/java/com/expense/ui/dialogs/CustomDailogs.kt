package com.expense.ui.dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import com.expense.R
import com.expense.ui.theme.Purple200
import com.expense.ui.theme.Purple700
import com.expense.utils.Constants.AMOUNT
import com.expense.utils.Constants.TRANSACTION_DES

@Composable
fun CustomDialog(
    openDialogCustom: MutableState<Boolean>,
    onSelect: (String, String, String) -> Unit
) {
    if (openDialogCustom.value) {
        Dialog(onDismissRequest = { openDialogCustom.value = false }) {
            CustomDialogUI(openDialogCustom = openDialogCustom, onSelect = onSelect)
        }
    }
}

@Composable
fun CustomDialogUI(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean>,
    onSelect: (String, String, String) -> Unit
) {
    var textDescription by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    val invalidAmountInput = amount.count() < 1
    val invalidDescriptionInput = textDescription.count() < 1
    var transactionType by rememberSaveable {
        mutableStateOf("Expenses")
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 45.dp, 10.dp, 10.dp), elevation = 8.dp
    ) {
        Column(
            modifier
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                androidx.compose.material3.Text(
                    text = stringResource(R.string.add_transaction),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                TransactionTypeDropDown(onSelect = {
                    transactionType = it
                })

                TextField(
                    value = textDescription,
                    onValueChange = {
                        textDescription = it
                    },
                    isError = invalidDescriptionInput,
                    label = { Text(stringResource(R.string.transaction_description)) },
                    placeholder = { Text(text = stringResource(R.string.transaction_description)) },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = TRANSACTION_DES
                        }
                )

                TextField(
                    value = amount,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
                    onValueChange = {
                        amount = it
                    },
                    label = {
                        val label = stringResource(R.string.amount_hint)
                        Text(label)
                    },
                    isError = invalidAmountInput,
                    placeholder = { Text(stringResource(R.string.amount_hint)) },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = AMOUNT
                        }
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(Purple200),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                androidx.compose.material3.TextButton(onClick = {
                    openDialogCustom.value = false
                }) {

                    androidx.compose.material3.Text(
                        stringResource(R.string.text_cancel),
                        fontWeight = FontWeight.Bold,
                        color = Purple700,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
                androidx.compose.material3.TextButton(onClick = {
                    if (textDescription.isEmpty()) {
                        return@TextButton
                    }
                    if (amount.isEmpty()) {
                        return@TextButton
                    }
                    openDialogCustom.value = false
                    onSelect(textDescription, amount, transactionType)

                }) {
                    androidx.compose.material3.Text(
                        stringResource(R.string.text_add),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun TransactionTypeDropDown(onSelect: (String) -> Unit) {
    var mExpanded by remember { mutableStateOf(false) }
    val mCities = listOf(stringResource(R.string.expenses), stringResource(R.string.income))
    val context = LocalContext.current
    var mSelectedText by remember { mutableStateOf(context.getString(R.string.expenses)) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = mSelectedText,
            onValueChange = { mSelectedText = it },
            readOnly = true, modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text(stringResource(R.string.expenses)) },
            trailingIcon = {
                Icon(icon, "",
                    Modifier.clickable { mExpanded = !mExpanded })
            }
        )

        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            mCities.forEach { label ->
                DropdownMenuItem(onClick = {
                    mSelectedText = label
                    mExpanded = false
                }) {
                    Text(text = label)
                    onSelect(label)
                }
            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Preview(name = "Custom Dialog")
@Composable
fun MyDialogUIPreview() {
    CustomDialogUI(openDialogCustom = mutableStateOf(false),
        onSelect = { _, _, _ ->
        })
}