package com.dudencov.happyhabit.presentation.weekly

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.dudencov.happyhabit.R
import com.dudencov.happyhabit.presentation.theme.HappyHabitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyProgressScreen(
    state: WeeklyProgressState,
    onIntent: (WeeklyProgressIntent) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopBar(scrollBehavior, onIntent) }
    ) { paddingValues ->
        Habits(paddingValues, state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onIntent: (WeeklyProgressIntent) -> Unit
) {
    val context = LocalContext.current

    Surface(
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        TopAppBar(
            modifier = Modifier.testTag(WeeklyTestTags.TOP_APP_BAR.tag),
            scrollBehavior = scrollBehavior,
            title = { Text(text = context.getString(R.string.weekly_appbar_title)) },
            navigationIcon = {
                IconButton(onClick = { onIntent(WeeklyProgressIntent.OnNavigateBack) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.button_back_content_desk),
                    )
                }
            }
        )
    }
}

@Composable
private fun Habits(
    paddingValues: PaddingValues,
    state: WeeklyProgressState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(bottom = 4.dp)
    ) {
        items(state.habits) { habit -> HabitItem(habit) }
    }
}

@Composable
private fun HabitItem(habit: WeeklyHabitUi) {
    Card(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                text = habit.name,
                modifier = Modifier.weight(1f)
            )

            HabitItemDays(habit)
        }
    }
}

@Composable
private fun RowScope.HabitItemDays(habit: WeeklyHabitUi) {
    val weekdaysShort = stringArrayResource(R.array.weekdays_short)

    habit.days.forEachIndexed { i, day ->
        Spacer(modifier = Modifier.Companion.weight(0.02f))
        HabitItemDay(weekdaysShort[i], day)
    }
}

@Composable
private fun RowScope.HabitItemDay(
    dayName: String,
    day: WeeklyDayUi
) {
    Text(
        text = dayName,
        modifier = Modifier.Companion.align(Alignment.CenterVertically),
        color = if (day.isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        },
        fontWeight = if (day.isSelected) FontWeight.Bold else FontWeight.Normal,
    )
}

@PreviewScreenSizes
@Composable
private fun Preview() {
    HappyHabitTheme {
        WeeklyProgressScreen(
            state = stub,
            onIntent = {}
        )
    }
}