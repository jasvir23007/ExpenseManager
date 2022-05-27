package com.expense.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.model.Transactions
import com.expense.utils.Constants.EXPENSES
import com.expense.utils.Constants.INCOME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(val expenseRepository: ExpenseRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow<List<Transactions>>(ArrayList<Transactions>())
    val uiState: StateFlow<List<Transactions>> = _uiState
    private val _expense = MutableStateFlow("0")
    val expense: StateFlow<String> = _expense
    private val _income = MutableStateFlow("0")
    val income: StateFlow<String> = _income
    private val _balance = MutableStateFlow("0")
    val balance: StateFlow<String> = _balance

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val list = expenseRepository.getInitialData()
            withContext(Dispatchers.Main) {
                refreshData(list)
            }
        }
    }

    fun insertExpenseData(amount: String, desc: String, type: String) {
        viewModelScope.launch {
            val list = expenseRepository.insertExpenseData(amount, desc, type)
            refreshData(list)
        }
    }

    private fun refreshData(list: List<Transactions>) {
        var expenseValue = 0
        var incomeValue = 0
        for (transactions in list) {
            transactions.transactionDetails.forEach {
                if (it.type.equals(INCOME, true)) {
                    incomeValue += it.amount.toInt()
                } else if (it.type.equals(EXPENSES, true)) {
                    expenseValue += it.amount.toInt()
                }
            }
        }
        _income.value = "$incomeValue"
        _expense.value = "$expenseValue"
        _balance.value = "${incomeValue - expenseValue}"
        _uiState.value = list
    }

    fun removeItems(date: String, index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = expenseRepository.removeItems(date, index)
            withContext(Dispatchers.Main) {
                refreshData(list = list)
            }
        }
    }

}