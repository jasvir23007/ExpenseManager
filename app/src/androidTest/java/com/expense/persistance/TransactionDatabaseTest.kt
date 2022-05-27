package com.expense.persistance

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.expense.model.TransactionDetails
import com.expense.model.Transactions
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Jasvir Partap Singh on 25,May,2022
 */
@RunWith(AndroidJUnit4::class) // Annotate with @RunWith
class TransactionDatabaseTest : TestCase() {

    private lateinit var db: TransactionDatabase
    private lateinit var dao: TransactionDao

    @Before
    public override fun setUp() {
        // get context -- since this is an instrumental test it requires
        // context from the running application
        val context = ApplicationProvider.getApplicationContext<Context>()
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, TransactionDatabase::class.java).build()
        dao = db.transactionDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun addDataToDb() = runBlocking {
        val list = ArrayList<TransactionDetails>()
        list.add(TransactionDetails("200", "salary", "income"))
        val language = Transactions("25--05-2022", list)
        dao.insertTransactions(language)
        val languages = dao.getTransactions()
        assertThat(languages.contains(language)).isTrue()
    }

    @Test
    fun updateDataInDb() = runBlocking {
        val list = ArrayList<TransactionDetails>()
        list.add(TransactionDetails("200", "salary", "income"))
        list.add(TransactionDetails("210", "salary", "income"))
        val transaction = Transactions("25--05-2022", list)
        dao.insertTransactions(transaction)
        val transactionListBeforeChange = dao.getTransactions()

        assertEquals(
            transactionListBeforeChange[0].transactionDetails.size,
            2
        )
        val transactionsToChange = transactionListBeforeChange[0]
        val transactionDetailsList = transactionsToChange.transactionDetails as ArrayList
        transactionDetailsList.removeAt(0)

        transactionsToChange.transactionDetails = transactionDetailsList

        dao.insertTransactions(transactionsToChange)

        val transactionListAfterChange = dao.getTransactions()



        assertEquals(
            transactionListAfterChange[0].transactionDetails.size,
            1
        )
    }
}