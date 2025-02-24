package com.dudencov.happyhabit.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dudencov.happyhabit.R
import com.dudencov.happyhabit.presentation.home.HomeIntent.OnFabClicked
import com.dudencov.happyhabit.presentation.home.HomeIntent.OnHabitClicked
import com.dudencov.happyhabit.presentation.home.HomeTestTags.FAB
import com.dudencov.happyhabit.presentation.home.HomeTestTags.LIST_BTN_MENU
import com.dudencov.happyhabit.presentation.home.HomeTestTags.LIST_DROPDOWN_MENU
import com.dudencov.happyhabit.presentation.home.HomeTestTags.LIST_DROPDOWN_MENU_DELETE_ITEM
import com.dudencov.happyhabit.presentation.home.HomeTestTags.LIST_DROPDOWN_MENU_EDIT_ITEM
import com.dudencov.happyhabit.presentation.home.HomeTestTags.LIST_ITEM
import com.dudencov.happyhabit.presentation.home.HomeTestTags.WEEKLY_BTN
import com.dudencov.happyhabit.presentation.theme.HappyHabitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
) {
    val context = LocalContext.current
    val lazyListState: LazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = context.getString(R.string.app_name),
                        modifier = Modifier.testTag(HomeTestTags.TITLE.tag)
                    )
                },
                actions = {
                    IconButton(
                        modifier = Modifier.testTag(WEEKLY_BTN.tag),
                        onClick = {
                            onIntent(HomeIntent.OnWeeklyProgressClicked)
                        }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = context.getString(R.string.weekly_progress_content_desc)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.testTag(FAB.tag),
                onClick = {
                    onIntent(OnFabClicked)
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = context.getString(R.string.fab_add_content_desc)
                )
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp),
            state = lazyListState
        ) {
            items(
                state.habits,
                key = { habit -> habit.id }
            ) { habit ->
                var expanded by remember { mutableStateOf(false) }

                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onIntent(OnHabitClicked(habit.id)) }
                        .testTag(LIST_ITEM.tag),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = habit.name,
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                        )

                        Box(
                            modifier = Modifier
                                .wrapContentSize(Alignment.TopEnd)
                        ) {
                            IconButton(
                                modifier = Modifier.testTag(LIST_BTN_MENU.tag),
                                onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = context.getString(R.string.context_menu_content_desc)
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .testTag(LIST_DROPDOWN_MENU.tag)
                            ) {
                                DropdownMenuItem(
                                    text = { Text(context.getString(R.string.context_menu_edit_title)) },
                                    onClick = {
                                        onIntent(HomeIntent.OnHabitEditClicked(habit.id))
                                        expanded = false
                                    },
                                    modifier = Modifier.testTag(LIST_DROPDOWN_MENU_EDIT_ITEM.tag),
                                )
                                DropdownMenuItem(
                                    text = { Text(context.getString(R.string.context_menu_delete_title)) },
                                    onClick = {
                                        onIntent(HomeIntent.OnHabitDeleteClicked(habit.id))
                                        expanded = false
                                    },
                                    modifier = Modifier.testTag(LIST_DROPDOWN_MENU_DELETE_ITEM.tag),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HomeScreenPreview1() {
    HappyHabitTheme {
        HomeScreen(
            state = HomeState(),
            onIntent = {},
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HomeScreenPreview2() {
    HappyHabitTheme {
        HomeScreen(
            state = HomeState(habits = previewStub),
            onIntent = {},
        )
    }
}
























