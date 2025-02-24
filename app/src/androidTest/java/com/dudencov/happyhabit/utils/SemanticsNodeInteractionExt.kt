package com.dudencov.happyhabit.utils

import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dudencov.happyhabit.presentation.MainActivity

fun SemanticsNodeInteraction.assertTextEquals(
    rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    value: Int,
    includeEditableText: Boolean = true
): SemanticsNodeInteraction {
    return this.assertTextEquals(
        values = arrayOf(rule.activity.getString(value)),
        includeEditableText = includeEditableText
    )
}

fun SemanticsNodeInteraction.assertTextEquals(
    @StringRes vararg values: Int,
    rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    includeEditableText: Boolean = true
): SemanticsNodeInteraction {
    return this.assertTextEquals(
        values = values.map { rule.activity.getString(it) }.toTypedArray(),
        includeEditableText = includeEditableText
    )
}
