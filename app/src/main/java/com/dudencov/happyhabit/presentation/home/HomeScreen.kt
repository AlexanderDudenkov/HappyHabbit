package com.dudencov.happyhabit.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.dudencov.happyhabit.R
import com.dudencov.happyhabit.presentation.entities.HabitUi
import com.dudencov.happyhabit.presentation.home.HomeIntent.OnFabClicked
import com.dudencov.happyhabit.presentation.home.HomeIntent.OnHabitClicked
import com.dudencov.happyhabit.presentation.home.HomeTestTags.FAB
import com.dudencov.happyhabit.presentation.home.HomeTestTags.LIST_BTN_MENU
import com.dudencov.happyhabit.presentation.home.HomeTestTags.LIST_DROPDOWN_MENU
import com.dudencov.happyhabit.presentation.home.HomeTestTags.LIST_DROPDOWN_MENU_DELETE_ITEM
import com.dudencov.happyhabit.presentation.home.HomeTestTags.LIST_DROPDOWN_MENU_EDIT_ITEM
import com.dudencov.happyhabit.presentation.home.HomeTestTags.LIST_ITEM
import com.dudencov.happyhabit.presentation.home.HomeTestTags.SETTINGS_BTN
import com.dudencov.happyhabit.presentation.home.HomeTestTags.WEEKLY_BTN
import com.dudencov.happyhabit.presentation.theme.HappyHabitTheme

@Composable
fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
) {
    Scaffold(
        bottomBar = { BottomBar(state, onIntent) },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).padding(top = 16.dp)) {
            HabitList(state, onIntent)
            EmptyState(state)
        }
    }
}

@Composable
private fun BottomBar(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    BottomAppBar(
        actions = {
            IconButton(
                modifier = Modifier.testTag(WEEKLY_BTN.tag),
                enabled = state.isWeeklyEnabled,
                onClick = {
                    onIntent(HomeIntent.OnWeeklyProgressClicked)
                }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.weekly_progress_content_desc)
                )
            }
            IconButton(
                modifier = Modifier.testTag(SETTINGS_BTN.tag),
                onClick = {
                    onIntent(HomeIntent.OnSettingsClicked)
                }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings_title)
                )
            }
        },
        floatingActionButton = { Fab(onIntent) }
    )
}

@Composable
private fun Fab(
    onIntent: (HomeIntent) -> Unit,
) {
    FloatingActionButton(
        modifier = Modifier.testTag(FAB.tag),
        onClick = {
            onIntent(OnFabClicked)
        }) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.fab_add_content_desc)
        )
    }
}

@Composable
private fun HabitList(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
) {
    val lazyListState: LazyListState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = lazyListState
    ) {
        items(
            state.habitItems,
            key = { item -> item.habit.id }
        ) { item ->
            HabitItem(onIntent, item)
        }
    }
}

@Composable
private fun HabitItem(
    onIntent: (HomeIntent) -> Unit,
    item: HabitItemUi,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onIntent(OnHabitClicked(id = item.habit.id, name = item.habit.name)) }
            .testTag(LIST_ITEM.tag),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = item.habit.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            )

            HabitItemMenu(onIntent, item)
        }
    }
}

@Composable
private fun HabitItemMenu(
    onIntent: (HomeIntent) -> Unit,
    item: HabitItemUi,
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(
            modifier = Modifier.testTag(LIST_BTN_MENU.tag),
            onClick = {
                onIntent(
                    HomeIntent.OnHabitItemMenuClicked(
                        habitId = item.habit.id,
                        isExpended = item.menuExpended
                    )
                )
            }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = context.getString(R.string.context_menu_content_desc)
            )
        }

        DropdownMenu(
            expanded = item.menuExpended,
            onDismissRequest = { onIntent(HomeIntent.OnHabitItemMenuDismissed(item.habit.id)) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .testTag(LIST_DROPDOWN_MENU.tag)
        ) {
            DropdownMenuItem(
                text = { Text(context.getString(R.string.context_menu_edit_title)) },
                onClick = {
                    onIntent(HomeIntent.OnHabitEditClicked(item.habit.id))
                },
                modifier = Modifier.testTag(LIST_DROPDOWN_MENU_EDIT_ITEM.tag),
            )
            DropdownMenuItem(
                text = { Text(context.getString(R.string.context_menu_delete_title)) },
                onClick = {
                    onIntent(HomeIntent.OnHabitDeleteClicked(item.habit.id))
                },
                modifier = Modifier.testTag(LIST_DROPDOWN_MENU_DELETE_ITEM.tag),
            )
        }
    }
}

@Composable
private fun BoxScope.EmptyState(state: HomeState) {
    val context = LocalContext.current

    if (state.isEmptyStateVisible) {
        Text(
            text = context.getString(R.string.home_empty_state_title),
            modifier = Modifier.Companion
                .align(alignment = Alignment.Center)
                .testTag(HomeTestTags.EMPTY_STATE.tag)
        )
    }
}

@Composable
@PreviewScreenSizes
@Preview(showSystemUi = false, showBackground = true)
private fun PreviewEmptyState() {
    HappyHabitTheme {
        HomeScreen(
            state = HomeState(),
            onIntent = {},
        )
    }
}

@Composable
@PreviewScreenSizes
@Preview(showSystemUi = false, showBackground = true)
private fun Preview2() {
    HappyHabitTheme {
        HomeScreen(
            state = HomeState(
                isWeeklyEnabled = true,
                habitItems = previewStub,
                isEmptyStateVisible = false
            ),
            onIntent = {},
        )
    }
}

@Composable
@PreviewScreenSizes
@Preview(showSystemUi = false, showBackground = true)
private fun Preview3() {
    HappyHabitTheme {
        HomeScreen(
            state = HomeState(
                isWeeklyEnabled = true,
                habitItems = listOf(
                    HabitItemUi(
                        menuExpended = true,
                        habit = HabitUi(id = 0, name = "habit")
                    )
                ),
                isEmptyStateVisible = false
            ),
            onIntent = {},
        )
    }
}























