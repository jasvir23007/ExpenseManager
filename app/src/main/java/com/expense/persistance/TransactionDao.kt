package com.expense.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.expense.model.Transactions

@Dao
interface TransactionDao {

    @Query(" Select *  from `transaction`")
    suspend fun getTransactions(): List<Transactions>

    @Insert(onConflict = REPLACE)
    suspend fun insertTransactions(myInfo: Transactions)

    @Update(onConflict = REPLACE)
    suspend fun updateTransactions(myInfo: Transactions)
}