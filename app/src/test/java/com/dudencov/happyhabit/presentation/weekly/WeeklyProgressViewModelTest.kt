package com.dudencov.happyhabit.presentation.weekly

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dudencov.happyhabit.domain.data.HabitRepository
import com.dudencov.happyhabit.domain.entities.Habit
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.datetime.LocalDate as KtLocalDate

@ExperimentalCoroutinesApi
class WeeklyProgressViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WeeklyProgressViewModel
    private lateinit var repository: HabitRepository

    private val testDispatcher = UnconfinedTestDispatcher()
    private val fixedDate = KtLocalDate(2023, 1, 2) // Monday, January 2, 2023
    private val currentWeek = KtLocalDate(2023, 1, 2)..KtLocalDate(2023, 1, 8) // Mon-Sun

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        // Mock Clock.System.todayIn() for getCurrentWeek()
        mockkStatic("kotlinx.datetime.ClockKt")
        every { Clock.System.todayIn(TimeZone.currentSystemDefault()) } returns fixedDate
        // Mock getCurrentWeek() - adjust file name if different
        mockkStatic("com.dudencov.happyhabit.presentation.utils.LocalDateUtilsKt")
        every { com.dudencov.happyhabit.presentation.utils.getCurrentWeek() } returns currentWeek
        viewModel = WeeklyProgressViewModel(repository)
    }

    @After
    fun tearDown() {
        unmockkStatic("kotlinx.datetime.ClockKt")
        unmockkStatic("com.dudencov.happyhabit.presentation.utils.LocalDateUtilsKt")
        Dispatchers.resetMain()
    }

    @Test
    fun test_GIVEN_initial_state_WHEN_onCreate_THEN_updates_habits() = runTest {
        // Given
        val habit = Habit(id = 1, name = "Exercise")
        val selectedDates = setOf(KtLocalDate(2023, 1, 2), KtLocalDate(2023, 1, 4)) // Mon, Wed
        coEvery { repository.getAllHabitsWithDates(currentWeek) } returns flowOf(mapOf(habit to selectedDates))

        // When
        viewModel.onIntent(WeeklyProgressIntent.OnCreate)

        // Then
        val expectedDays = listOf(
            WeeklyDayUi(isSelected = true),  // Monday
            WeeklyDayUi(), // Tuesday
            WeeklyDayUi(isSelected = true),  // Wednesday
            WeeklyDayUi(), // Thursday
            WeeklyDayUi(), // Friday
            WeeklyDayUi(), // Saturday
            WeeklyDayUi()  // Sunday
        )
        val exp = WeeklyProgressState(
            habits = listOf(
                WeeklyHabitUi(
                    id = 1,
                    name = "Exercise",
                    days = expectedDays
                )
            )
        )
        val act = viewModel.state.first()
        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_empty_habits_WHEN_onCreate_THEN_updates_to_empty_habits() = runTest {
        // Given
        coEvery { repository.getAllHabitsWithDates(currentWeek) } returns flowOf(emptyMap())

        // When
        viewModel.onIntent(WeeklyProgressIntent.OnCreate)

        // Then
        val exp = WeeklyProgressState() // habits defaults to emptyList()
        val act = viewModel.state.first()
        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_no_setup_WHEN_onNavigateBack_THEN_emits_route_back() = runTest {
        // Given - no specific setup needed

        // Then
        var act: WeeklySideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            act = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(WeeklyProgressIntent.OnNavigateBack)

        // Then
        val exp = WeeklySideEffect.RouteBack
        assertEquals(exp, act)
    }
}