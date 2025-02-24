package com.dudencov.happyhabit.base

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.dudencov.happyhabit.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule

internal abstract class BaseTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }
}