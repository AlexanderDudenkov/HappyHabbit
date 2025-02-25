package com.dudencov.happyhabit.presentation.home

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dudencov.happyhabit.R
import com.dudencov.happyhabit.base.BaseTest
import com.dudencov.happyhabit.presentation.detail.DetailTestTags
import com.dudencov.happyhabit.presentation.habitdialog.DialogTestTags
import com.dudencov.happyhabit.presentation.weekly.WeeklyTestTags
import com.dudencov.happyhabit.utils.assertTextEquals
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class HomeScreenTest : BaseTest() {

    @Test
    fun testNavigateToWeeklyAndBack() {
        with(composeTestRule) {
            onNodeWithTag(HomeTestTags.WEEKLY_BTN.tag).performClick()
            onNodeWithTag(WeeklyTestTags.TOP_APP_BAR.tag).assertIsDisplayed()

            activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }

            onNodeWithTag(HomeTestTags.TITLE.tag).assertTextEquals(this, R.string.app_name)
            onNodeWithTag(HomeTestTags.WEEKLY_BTN.tag).assertIsDisplayed()
        }
    }

    @Test
    fun testNavigateToDetailsAndBack() {
        with(composeTestRule) {
            onNodeWithTag(HomeTestTags.FAB.tag).performClick()
            onNodeWithTag(DialogTestTags.TITLE.tag).assertTextEquals(
                this,
                R.string.habit_dialog_create_habit_title
            )
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).performTextInput("1")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).performClick()
            onNodeWithTag(HomeTestTags.LIST_ITEM.tag).assertTextEquals("1").performClick()
            onNodeWithTag(DetailTestTags.TOP_APP_BAR.tag).assertExists()

            activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }

            onNodeWithTag(HomeTestTags.TITLE.tag).assertTextEquals(this, R.string.app_name)
        }
    }

    //User taps Save during editing habit name
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testEditHabit1() {
        with(composeTestRule) {
            onNodeWithTag(HomeTestTags.FAB.tag).performClick()
            onNodeWithTag(DialogTestTags.TITLE.tag).assertTextEquals(
                this,
                R.string.habit_dialog_create_habit_title
            )
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).assertIsNotEnabled()
            onNodeWithTag(DialogTestTags.BTN_CANCEL.tag).assertIsEnabled()
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).performTextInput("1")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).assertIsEnabled().performClick()
            waitUntilAtLeastOneExists(hasText("1"), 5000)
            onNodeWithTag(HomeTestTags.LIST_ITEM.tag).assertTextEquals("1")
            onNodeWithTag(HomeTestTags.LIST_BTN_MENU.tag).performClick()
            waitUntilAtLeastOneExists(hasTestTag(HomeTestTags.LIST_DROPDOWN_MENU.tag),5000)
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU.tag).assertIsDisplayed()
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU_DELETE_ITEM.tag).assertTextEquals(
                this,
                R.string.context_menu_delete_title
            )
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU_EDIT_ITEM.tag).assertTextEquals(
                this,
                R.string.context_menu_edit_title
            ).performClick()
            onNodeWithTag(DialogTestTags.TITLE.tag).assertTextEquals(
                this,
                R.string.habit_dialog_edit_habit_title
            )
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).assertIsNotEnabled()
            onNodeWithTag(DialogTestTags.BTN_CANCEL.tag).assertIsEnabled()
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).assertTextEquals("1").performTextInput("1")
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).assertTextEquals("11")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).assertIsEnabled()
            onNodeWithTag(DialogTestTags.BTN_CANCEL.tag).assertIsEnabled()
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).performTextReplacement("1")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).assertIsNotEnabled()
            onNodeWithTag(DialogTestTags.BTN_CANCEL.tag).assertIsEnabled()
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).performTextInput("1")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).performClick()
            onNodeWithTag(HomeTestTags.LIST_ITEM.tag).assertTextEquals("11")
        }
    }

    //User taps Cancel during editing habit name
    @Test
    fun testEditHabit2() {
        with(composeTestRule) {
            onNodeWithTag(HomeTestTags.FAB.tag).performClick()
            onNodeWithTag(DialogTestTags.TITLE.tag).assertTextEquals(
                this,
                R.string.habit_dialog_create_habit_title
            )
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).performTextInput("1")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).performClick()
            waitUntilAtLeastOneExists(hasText("1"), 10000)
            onNodeWithTag(HomeTestTags.LIST_ITEM.tag).assertTextEquals("1")
            onNodeWithTag(HomeTestTags.LIST_BTN_MENU.tag).performClick()
            composeTestRule.waitForIdle()
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU.tag).assertIsDisplayed()
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU_DELETE_ITEM.tag).assertTextEquals(
                this,
                R.string.context_menu_delete_title
            )
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU_EDIT_ITEM.tag).assertTextEquals(
                this,
                R.string.context_menu_edit_title
            ).performClick()
            onNodeWithTag(DialogTestTags.TITLE.tag).assertTextEquals(
                this,
                R.string.habit_dialog_edit_habit_title
            )
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).assertTextEquals("1").performTextInput("1")
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).assertTextEquals("11")
            onNodeWithTag(DialogTestTags.BTN_CANCEL.tag).performClick()
            onNodeWithTag(HomeTestTags.LIST_ITEM.tag).assertTextEquals("1")
        }
    }

    //User taps Back btn during editing habit name
    @Test
    fun testEditHabit3() {
        with(composeTestRule) {
            onNodeWithTag(HomeTestTags.FAB.tag).performClick()
            onNodeWithTag(DialogTestTags.TITLE.tag).assertTextEquals(
                this,
                R.string.habit_dialog_create_habit_title
            )
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).performTextInput("1")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).performClick()
            waitUntilAtLeastOneExists(hasText("1"), 10000)
            onNodeWithTag(HomeTestTags.LIST_ITEM.tag).assertTextEquals("1")
            onNodeWithTag(HomeTestTags.LIST_BTN_MENU.tag).performClick()
            composeTestRule.waitForIdle()
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU.tag).assertIsDisplayed()
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU_DELETE_ITEM.tag).assertTextEquals(
                this,
                R.string.context_menu_delete_title
            )
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU_EDIT_ITEM.tag).assertTextEquals(
                this,
                R.string.context_menu_edit_title
            ).performClick()
            onNodeWithTag(DialogTestTags.TITLE.tag).assertTextEquals(
                this,
                R.string.habit_dialog_edit_habit_title
            )
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).assertTextEquals("1").performTextInput("1")
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).assertTextEquals("11")

            activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
            waitForIdle()
            onNodeWithTag(HomeTestTags.LIST_ITEM.tag).assertTextEquals("1")
        }
    }

    //User edits the same habit twice
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testEditHabit4() {
        with(composeTestRule) {
            //creating
            onNodeWithTag(HomeTestTags.FAB.tag).performClick()
            onNodeWithTag(DialogTestTags.TITLE.tag).assertTextEquals(
                this,
                R.string.habit_dialog_create_habit_title
            )
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).assertIsNotEnabled()
            onNodeWithTag(DialogTestTags.BTN_CANCEL.tag).assertIsEnabled()
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).performTextInput("1")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).assertIsEnabled().performClick()
            waitUntilAtLeastOneExists(hasText("1"), 10000)
            onNodeWithTag(HomeTestTags.LIST_ITEM.tag).assertTextEquals("1")

            //editing #1
            onNodeWithTag(HomeTestTags.LIST_BTN_MENU.tag).performClick()
            composeTestRule.waitUntilAtLeastOneExists(hasTestTag(HomeTestTags.LIST_DROPDOWN_MENU.tag), 5000)
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU.tag).assertIsDisplayed()
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU_DELETE_ITEM.tag).assertTextEquals(
                this,
                R.string.context_menu_delete_title
            )
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU_EDIT_ITEM.tag).assertTextEquals(
                this,
                R.string.context_menu_edit_title
            ).performClick()
            onNodeWithTag(DialogTestTags.TITLE.tag).assertTextEquals(
                this,
                R.string.habit_dialog_edit_habit_title
            )
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).assertIsNotEnabled()
            onNodeWithTag(DialogTestTags.BTN_CANCEL.tag).assertIsEnabled()
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).assertTextEquals("1").performTextInput("1")
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).assertTextEquals("11")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).assertIsEnabled()
            onNodeWithTag(DialogTestTags.BTN_CANCEL.tag).assertIsEnabled()
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).performTextReplacement("1")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).assertIsNotEnabled()
            onNodeWithTag(DialogTestTags.BTN_CANCEL.tag).assertIsEnabled()
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).performTextInput("1")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).performClick()

            //editing #2
            onNodeWithTag(HomeTestTags.LIST_BTN_MENU.tag).performClick()
            composeTestRule.waitForIdle()
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU_EDIT_ITEM.tag).performClick()
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).assertTextEquals("11").performTextInput("1")
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).assertTextEquals("111")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).performClick()
            waitUntilAtLeastOneExists(hasText("111"), 5000)
            onNodeWithTag(HomeTestTags.LIST_ITEM.tag).assertTextEquals("111")
        }
    }

    @Test
    fun testDeleteHabit() {
        with(composeTestRule) {
            onNodeWithTag(HomeTestTags.FAB.tag).performClick()
            onNodeWithTag(DialogTestTags.TITLE.tag).assertTextEquals(
                this,
                R.string.habit_dialog_create_habit_title
            )
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).performTextInput("1")
            onNodeWithTag(DialogTestTags.BTN_SAVE.tag).performClick()
            waitForIdle()
            onNodeWithTag(HomeTestTags.LIST_ITEM.tag).assertTextEquals("1")
            onNodeWithTag(HomeTestTags.LIST_BTN_MENU.tag).performClick()
            composeTestRule.waitForIdle()
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU_EDIT_ITEM.tag).assertTextEquals(
                this,
                R.string.context_menu_edit_title
            )
            composeTestRule.waitForIdle()
            onNodeWithTag(HomeTestTags.LIST_DROPDOWN_MENU_DELETE_ITEM.tag).assertTextEquals(
                this,
                R.string.context_menu_delete_title
            ).performClick()
            onNodeWithTag(HomeTestTags.LIST_ITEM.tag).assertDoesNotExist()
        }
    }

    @Test
    fun testKeyboardClosesOnDoneButtonTap() {
        with(composeTestRule) {
            onNodeWithTag(HomeTestTags.FAB.tag).performClick()
            onNodeWithTag(DialogTestTags.TITLE.tag).assertIsDisplayed()
            onNodeWithTag(DialogTestTags.TEXT_FIELD.tag).also {
                it.performTextInput("1")
                it.assertIsFocused()
                it.performImeAction()
                composeTestRule.waitForIdle()
                it.assertIsNotFocused()
            }
        }
    }

}