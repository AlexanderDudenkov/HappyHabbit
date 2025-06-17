package com.dudencov.happyhabit.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dudencov.happyhabit.domain.data.HabitRepository
import com.dudencov.happyhabit.domain.entities.Habit
import com.dudencov.happyhabit.presentation.entities.HabitUi
import com.dudencov.happyhabit.presentation.entities.toHabitUi
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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: HabitRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_GIVEN_repo_empty_habits_WHEN_onResume_THEN_shows_empty_state() = runTest {
        //Given
        coEvery { repository.getAllHabits() } returns flowOf(emptyList())

        //When
        viewModel.onIntent(HomeIntent.OnResume)

        //Then
        val exp = HomeState(
            isWeeklyEnabled = false,
            isEmptyStateVisible = true,
            habitItems = emptyList()
        )
        val act = viewModel.state.first()

        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_repo_non_empty_habits_WHEN_onResume_THEN_shows_habits() = runTest {
        //Given
        val habits = listOf(Habit(1, "Test Habit"))
        coEvery { repository.getAllHabits() } returns flowOf(habits)

        mockkStatic("com.dudencov.happyhabit.presentation.entities.HabitUiKt")
        every { any<Habit>().toHabitUi() } returns HabitUi(1, "Test Habit")

        //When
        viewModel.onIntent(HomeIntent.OnResume)

        //Then
        val exp = HomeState(
            isWeeklyEnabled = true,
            isEmptyStateVisible = false,
            habitItems = listOf(HabitItemUi(HabitUi(1, "Test Habit"), false))
        )
        val act = viewModel.state.first()

        assertEquals(exp, act)

        unmockkStatic("com.dudencov.happyhabit.presentation.entities.HabitUiKt")
    }

    @Test
    fun test_GIVEN_habit_id_WHEN_onHabitClicked_THEN_routes_to_details() = runTest {
        // Given
        val habitId = 1

        // Then
        var act: HomeSideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)){
            act = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(HomeIntent.OnHabitClicked(habitId))

        // Then
        val exp = HomeSideEffect.RouteToDetails(habitId)
        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_no_setup_WHEN_onFabClicked_THEN_routes_to_create_dialog() = runTest {
        // Given - no specific setup needed

        // Then
        var act: HomeSideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            act = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(HomeIntent.OnFabClicked)

        // Then
        val exp = HomeSideEffect.RouteToDialog(null)
        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_habit_id_WHEN_onHabitDeleteClicked_THEN_navigates_to_delete_confirmation() = runTest {
        // Given
        val habitId = 1

        // Then
        var act: HomeSideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            act = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(HomeIntent.OnHabitDeleteClicked(habitId))

        // Then
        val exp = HomeSideEffect.RouteToDeleteConfirmationDialog(habitId)
        assertEquals(exp, act)

        val stateExp = HomeState(
            habitItems = emptyList(),
            isWeeklyEnabled = false,
            isEmptyStateVisible = true
        )
        val stateAct = viewModel.state.first()
        assertEquals(stateExp, stateAct)
    }

    @Test
    fun test_GIVEN_menu_expanded_WHEN_onHabitEditClicked_THEN_collapses_menu_and_routes_to_edit_dialog() = runTest {
        // Given
        val habit = Habit(1, "Test")
        coEvery { repository.getAllHabits() } returns flowOf(listOf(habit))
        viewModel.onIntent(HomeIntent.OnResume) // Initial state
        viewModel.onIntent(HomeIntent.OnHabitItemMenuClicked(1, false)) // Expand menu

        // Then
        var sideEffectAct: HomeSideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            sideEffectAct = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(HomeIntent.OnHabitEditClicked(1))

        // Then
        val stateExp = HomeState(
            isWeeklyEnabled = true,
            isEmptyStateVisible = false,
            habitItems = listOf(HabitItemUi(HabitUi(1, "Test"), false))
        )
        val stateAct = viewModel.state.first()
        assertEquals(stateExp, stateAct)

        val sideEffectExp = HomeSideEffect.RouteToDialog(1)
        assertEquals(sideEffectExp, sideEffectAct)
    }

    @Test
    fun test_GIVEN_menu_collapsed_WHEN_onHabitItemMenuClicked_THEN_expands_menu() = runTest {
        // Given
        val habit = Habit(1, "Test")
        coEvery { repository.getAllHabits() } returns flowOf(listOf(habit))
        viewModel.onIntent(HomeIntent.OnResume) // Initial state with menu collapsed

        // When
        viewModel.onIntent(HomeIntent.OnHabitItemMenuClicked(1, false))

        // Then
        val exp = HomeState(
            isWeeklyEnabled = true,
            isEmptyStateVisible = false,
            habitItems = listOf(HabitItemUi(HabitUi(1, "Test"), true))
        )
        val act = viewModel.state.first()

        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_menu_expanded_WHEN_onHabitItemMenuDismissed_THEN_collapses_menu() = runTest {
        // Given
        val habit = Habit(1, "Test")
        coEvery { repository.getAllHabits() } returns flowOf(listOf(habit))
        viewModel.onIntent(HomeIntent.OnResume) // Initial state
        viewModel.onIntent(HomeIntent.OnHabitItemMenuClicked(1, false)) // Expand menu

        // When
        viewModel.onIntent(HomeIntent.OnHabitItemMenuDismissed(1))

        // Then
        val exp = HomeState(
            isWeeklyEnabled = true,
            isEmptyStateVisible = false,
            habitItems = listOf(HabitItemUi(HabitUi(1, "Test"), false))
        )
        val act = viewModel.state.first()

        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_no_setup_WHEN_onWeeklyProgressClicked_THEN_routes_to_weekly_progress() = runTest {
        // Given - no specific setup needed

        // Then
        var act: HomeSideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            act = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(HomeIntent.OnWeeklyProgressClicked)

        // Then
        val exp = HomeSideEffect.RouteToWeeklyProgress
        assertEquals(exp, act)
    }
}