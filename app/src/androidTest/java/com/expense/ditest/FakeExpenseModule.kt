package com.expense.ditest

import android.content.Context
import androidx.room.Room
import com.expense.ui.main.ExpenseRepository
import com.expense.di.ExpenseModule
import com.expense.persistance.TransactionDao
import com.expense.persistance.TransactionDatabase
import com.expense.utils.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ExpenseModule::class]
)
object FakeExpenseModule {
    @Provides
    fun providesChannelRepository(@ApplicationContext ctx: Context): ExpenseRepository {
        return ExpenseRepository(getDaoInstance(ctx))
    }

    @Provides
    @Synchronized
    fun getDaoInstance(@ApplicationContext ctx: Context): TransactionDao {
        return Room.databaseBuilder(
            ctx.applicationContext, TransactionDatabase::class.java,
            DB_NAME
        ).build().transactionDao()

    }
}