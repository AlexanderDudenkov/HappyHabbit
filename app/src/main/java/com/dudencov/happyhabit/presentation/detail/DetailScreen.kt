package com.dudencov.happyhabit.presentation.detail

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
fun DetailScreen(
    state: DetailState,
    onIntent: (DetailIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.testTag(DetailTestTags.TOP_APP_BAR.tag),
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