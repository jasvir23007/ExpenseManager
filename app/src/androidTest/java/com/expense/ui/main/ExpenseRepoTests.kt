package com.expense.ui.main

import com.expense.utils.Constants.DATE_FORMAT
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltAndroidTest
class ExpenseRepoTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    // Inject repository that uses empty DB
    @Inject
    lateinit var expenseRepository: ExpenseRepository

    @Before
    fun createDb() {
        hiltRule.inject()
    }


    @Test
    fun insertExpenseData() = runBlocking {

        val list = expenseRepository.insertExpenseData("100", "test", "Expense")

        assertEquals(list.isNullOrEmpty(), false)
    }

    @Test
    fun deleteExpenseData() = runBlocking {
        val oldList = expenseRepository.insertExpenseData("100", "test", "Expense")
        val oldListSize = oldList.get(oldList.size - 1).transactionDetails.size
        val list = expenseRepository.removeItems(getCurrentDate(), 0)

        val updatedListSize = list.get(list.size - 1).transactionDetails.size

        assertEquals(oldListSize > updatedListSize, true)
    }


    private fun getCurrentDate(): String {
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return df.format(c)
    }


}