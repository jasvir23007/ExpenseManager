package com.expense.ui.main

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.expense.utils.Constants.AMOUNT
import com.expense.utils.Constants.DELETE
import com.expense.utils.Constants.EXPENSE_LIST
import com.expense.utils.Constants.TRANSACTION_DES
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTests {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var activity: MainActivity


    @Before
    fun init() {
        composeTestRule.activityRule.scenario.onActivity {
            activity = it
        }
    }


    @Test
    fun isDialogDisplayed() {

        composeTestRule.onNodeWithContentDescription("displayDialog").performClick()
        composeTestRule.onNodeWithText("Add").assertIsDisplayed()
    }


    @Test
    fun addEntryToList() {

        val initialSize =
            activity.viewModel.uiState.value[activity.viewModel.uiState.value.size - 1].transactionDetails
                .size

        composeTestRule.onNodeWithContentDescription("displayDialog").performClick()
        composeTestRule.onNodeWithContentDescription(TRANSACTION_DES).performTextInput("test")
        composeTestRule.onNodeWithContentDescription(AMOUNT).performTextInput("1000")
        composeTestRule.onNodeWithText("Add").performClick()



        composeTestRule.onNodeWithContentDescription(EXPENSE_LIST).onChildren().onLast()
            .assertIsDisplayed()

        Thread.sleep(1000)
        TestCase.assertEquals(
            activity.viewModel.uiState.value[activity.viewModel.uiState.value.size - 1].transactionDetails
                .size, initialSize + 1
        )


    }



    @Test
    fun deleteEntryToList() {
        composeTestRule.onNodeWithContentDescription("displayDialog").performClick()
        composeTestRule.onNodeWithContentDescription(TRANSACTION_DES).performTextInput("test")
        composeTestRule.onNodeWithContentDescription(AMOUNT).performTextInput("1000")
        composeTestRule.onNodeWithText("Add").performClick()
        composeTestRule.onNodeWithContentDescription(EXPENSE_LIST).onChildren().onLast()
            .assertIsDisplayed()
        Thread.sleep(1000)
        composeTestRule.onAllNodesWithContentDescription(DELETE).onLast().performClick()

    }



}