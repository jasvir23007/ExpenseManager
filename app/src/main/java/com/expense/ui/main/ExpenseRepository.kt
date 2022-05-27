package com.expense.ui.main

import com.expense.persistance.TransactionDao
import com.expense.model.TransactionDetails
import com.expense.model.Transactions
import com.expense.utils.Constants.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ExpenseRepository @Inject constructor(private val dao: TransactionDao) {

    suspend fun insertExpenseData(amount: String, desc: String, type: String): List<Transactions> {
        var transactions: Transactions? = null
        val myList = dao.getTransactions()

        myList.let {
            it.forEach { items ->
                if (items.date == getCurrentDate()) {
                    transactions = items
                    val local = items.transactionDetails as ArrayList<TransactionDetails>
                    local.add(
                        TransactionDetails(amount, desc, type)
                    )
                }
            }
        }
        if (transactions == null) {
            val list = ArrayList<TransactionDetails>()
            list.add(
                TransactionDetails(amount, desc, type)
            )
            transactions = Transactions(getCurrentDate(), list)
            dao.insertTransactions(transactions!!)
        } else {
            dao.updateTransactions(transactions!!)

        }
        return dao.getTransactions()
    }

    private fun getCurrentDate(): String {
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return df.format(c)
    }

    suspend fun getInitialData(): List<Transactions> {
        return dao.getTransactions()

    }

    suspend fun removeItems(date: String, index: Int): List<Transactions> {
        var transactions: Transactions?
        val myList = dao.getTransactions()

        myList.let {
            it.forEach { items ->
                if (items.date == date) {
                    transactions = items
                    val local = items.transactionDetails as ArrayList<TransactionDetails>
                    local.removeAt(index)
                    transactions!!.transactionDetails = local
                    dao.updateTransactions(transactions!!)

                }
            }
        }

        return dao.getTransactions()
    }

}