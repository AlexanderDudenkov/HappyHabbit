package com.dudencov.happyhabit.presentation.weekly

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyProgressScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.testTag(WeeklyTestTags.TOP_APP_BAR.tag),
                title = { Text("") }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {

        }
    }
}