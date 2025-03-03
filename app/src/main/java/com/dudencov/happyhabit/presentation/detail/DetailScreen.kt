@file:OptIn(ExperimentalMaterial3Api::class)

package com.dudencov.happyhabit.presentation.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.dudencov.happyhabit.R
import java.time.LocalDate

@Composable
fun DetailScreen(
    state: DetailState,
    onIntent: (DetailIntent) -> Unit,
) {
    Scaffold(
        topBar = { TopBar(state, onIntent) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CalendarGrid(
                modifier = Modifier.weight(1f),
                state = state,
                onIntent = onIntent,
            )

            BottomDots(state.currentDate)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    state: DetailState,
    onIntent: (DetailIntent) -> Unit
) {
    Surface(
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        TopAppBar(
            modifier = Modifier.testTag(DetailTestTags.TOP_APP_BAR.tag),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            title = {
                Text(text = state.createTitle())
            },
            navigationIcon = {
                IconButton(onClick = { onIntent(DetailIntent.OnNavigateBack) }) {
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
fun CalendarGrid(
    modifier: Modifier = Modifier,
    state: DetailState,
    onIntent: (DetailIntent) -> Unit,
) {
    Column(
        modifier = modifier
            .pointerInput(Unit) { horizontalDragDetector(onIntent) }
    ) {
        Header()

        AnimatedContent(
            targetState = state.currentDate,
            transitionSpec = { gridAnimatedTransform(state) },
            modifier = Modifier.weight(1f)
        ) { targetDate ->
            DatesGrid(state, onIntent, targetDate)
        }
    }
}

private fun gridAnimatedTransform(state: DetailState) =
    if (state.swipeDirection == SwipeDirection.LEFT) {
        slideInHorizontally { fullWidth -> fullWidth } togetherWith slideOutHorizontally { fullWidth -> -fullWidth }
    } else if (state.swipeDirection == SwipeDirection.RIGHT) {
        slideInHorizontally { fullWidth -> -fullWidth } togetherWith slideOutHorizontally { fullWidth -> fullWidth }
    } else {
        fadeIn() togetherWith fadeOut()
    }.apply {
        targetContentZIndex = 1f
    }

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val weekdays = stringArrayResource(R.array.weekdays)
        weekdays.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DatesGrid(
    state: DetailState,
    onIntent: (DetailIntent) -> Unit,
    targetDate: LocalDate,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        val targetFirstDay = targetDate.withDayOfMonth(1)
        val targetDaysInMonth = targetDate.lengthOfMonth()
        val targetFirstDayOfWeek = targetFirstDay.dayOfWeek.value
        val offset = targetFirstDayOfWeek - 1

        items(offset) {
            Box(modifier = Modifier.size(48.dp))
        }

        items(targetDaysInMonth) { dayIndex ->
            val date: LocalDate = targetDate.withDayOfMonth(dayIndex + 1)
            val isActive = state.selectedDates.contains(date)
            val textColor = if (isActive) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        onIntent(DetailIntent.OnDateSelected(date))
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (dayIndex + 1).toString(),
                    color = textColor,
                    textAlign = TextAlign.Center,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                )
            }
        }
    }
}

private suspend fun PointerInputScope.horizontalDragDetector(
    onIntent: (DetailIntent) -> Unit,
) {
    var totalDrag = 0f
    detectHorizontalDragGestures(
        onDragStart = { totalDrag = 0f },
        onDragEnd = {
            if (totalDrag > 20) {
                onIntent(DetailIntent.OnScreenSwiped(SwipeDirection.RIGHT))
            } else if (totalDrag < -20) {
                onIntent(DetailIntent.OnScreenSwiped(SwipeDirection.LEFT))
            }
        }
    ) { _, dragAmount ->
        totalDrag += dragAmount
    }
}

@Composable
private fun BottomDots(currentDate: LocalDate) {
    Row(
        modifier = Modifier
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(12) { index ->
            val isActive = currentDate.month.value - 1 == index
            val dotColor by animateColorAsState(
                targetValue = if (isActive) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                },
                animationSpec = tween(durationMillis = 300)
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color = dotColor, shape = CircleShape)
            )
        }
    }
}

@PreviewScreenSizes
@Composable
fun CalendarScreenPreview() {
    DetailScreen(DetailState(), {})
}