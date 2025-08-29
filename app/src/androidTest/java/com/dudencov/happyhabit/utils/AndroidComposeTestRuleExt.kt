package com.dudencov.happyhabit.utils

import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule

fun AndroidComposeTestRule<*, *>.string(@StringRes id: Int): String =
    activity.getString(id)
