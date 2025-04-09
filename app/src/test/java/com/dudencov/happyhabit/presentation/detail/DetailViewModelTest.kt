package com.dudencov.happyhabit.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dudencov.happyhabit.domain.data.HabitRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.datetime.LocalDate as KtLocalDate

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailViewModel
    private lateinit var repository: HabitRepository

    private val testDispatcher = UnconfinedTestDispatcher()
    private val fixedDate = KtLocalDate(2023, 1, 1)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()

        mockkStatic("kotlinx.datetime.ClockKt")
        every { Clock.System.todayIn(TimeZone.currentSystemDefault()) } returns fixedDate

        viewModel = DetailViewModel(repository)
    }

    @After
    fun tearDown() {
        unmockkStatic("kotlinx.datetime.ClockKt")
        Dispatchers.resetMain()
    }

    @Test
    fun test_GIVEN_habit_id_WHEN_setHabitId_THEN_updates_state_with_dates() = runTest {
        // Given
        val habitId = 1
        val selectedDates = setOf(KtLocalDate(2023, 1, 1))
        coEvery { repository.getHabitDates(habitId) } returns selectedDates

        // When
        viewModel.onIntent(DetailIntent.SetHabitId(habitId))

        // Then
        val exp = DetailState(
            habitId = habitId,
            currentDate = KtLocalDate(2023, 1, 1),
            selectedDates = selectedDates
        )
        val act = viewModel.state.first()

        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_current_date_WHEN_onScreenSwiped_left_THEN_increments_month() = runTest {
        // Given
        val habitId = 1
        coEvery { repository.getHabitDates(habitId) } returns emptySet()
        viewModel.onIntent(DetailIntent.SetHabitId(habitId)) // Set initial state

        // When
        viewModel.onIntent(DetailIntent.OnScreenSwiped(SwipeDirection.LEFT))

        // Then
        val exp = DetailState(
            habitId = habitId,
            currentDate = KtLocalDate(2023, 2, 1),
            swipeDirection = SwipeDirection.LEFT,
            calendarDataUi = CalendarDataUi(
                targetFirstDay = KtLocalDate(2023, 2, 1),
                targetDaysInMonth = 28,
                targetFirstDayOfWeek = 3,
                offset = 2
            )
        )
        val act = viewModel.state.first()

        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_current_date_WHEN_onScreenSwiped_right_THEN_decrements_month() = runTest {
        // Given
        val habitId = 1
        coEvery { repository.getHabitDates(habitId) } returns emptySet()
        viewModel.onIntent(DetailIntent.SetHabitId(habitId)) // Set initial state

        // When
        viewModel.onIntent(DetailIntent.OnScreenSwiped(SwipeDirection.RIGHT))

        // Then
        val exp = DetailState(
            habitId = habitId,
            currentDate = KtLocalDate(2022, 12, 1),
            swipeDirection = SwipeDirection.RIGHT,
            calendarDataUi = CalendarDataUi(
                targetFirstDay = KtLocalDate(2022, 12, 1),
                targetDaysInMonth = 31,
                targetFirstDayOfWeek = 4,
                offset = 3
            )
        )
        val act = viewModel.state.first()

        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_date_not_selected_WHEN_onDateSelected_THEN_adds_date() = runTest {
        // Given
        val habitId = 1
        val date = KtLocalDate(2023, 1, 1)
        coEvery { repository.getHabitDates(habitId) } returns emptySet() andThen setOf(date)
        coEvery { repository.createCurrentDate(habitId, date) } returns Unit
        viewModel.onIntent(DetailIntent.SetHabitId(habitId)) // Set initial state

        // When
        viewModel.onIntent(DetailIntent.OnDateSelected(date))

        // Then
        val exp = DetailState(
            habitId = habitId,
            currentDate = KtLocalDate(2023, 1, 1),
            selectedDates = setOf(date)
        )
        val act = viewModel.state.first()

        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_date_selected_WHEN_onDateSelected_THEN_removes_date() = runTest {
        // Given
        val habitId = 1
        val date = KtLocalDate(2023, 1, 1)
        coEvery { repository.getHabitDates(habitId) } returns setOf(date) andThen emptySet()
        coEvery { repository.deleteDate(habitId, date) } returns Unit
        viewModel.onIntent(DetailIntent.SetHabitId(habitId)) // Set initial state with date

        // When
        viewModel.onIntent(DetailIntent.OnDateSelected(date))

        // Then
        val exp = DetailState(
            habitId = habitId,
            currentDate = KtLocalDate(2023, 1, 1)
        )
        val act = viewModel.state.first()

        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_no_setup_WHEN_onNavigateBack_THEN_emits_route_back() = runTest {
        // Given - no specific setup needed

        // Then
        var act: DetailSideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            act = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(DetailIntent.OnNavigateBack)

        // Then
        val exp = DetailSideEffect.RouteBack
        assertEquals(exp, act)
    }
}