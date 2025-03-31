package com.dudencov.happyhabit.presentation.habitdialog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dudencov.happyhabit.domain.data.HabitRepository
import com.dudencov.happyhabit.domain.entities.Habit
import com.dudencov.happyhabit.domain.usecases.HabitValidationUseCase
import com.dudencov.happyhabit.presentation.entities.HabitUi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class HabitDialogViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HabitDialogViewModel
    private lateinit var repository: HabitRepository
    private lateinit var habitValidationUseCase: HabitValidationUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        habitValidationUseCase = mockk(relaxed = true)
        viewModel = HabitDialogViewModel(repository, habitValidationUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_GIVEN_initial_state_WHEN_onChangeTitle_THEN_updates_title() = runTest {
        // Given
        val newTitle = HabitDialogTitle.EDIT

        // When
        viewModel.onIntent(HabitDialogIntent.OnChangeTitle(newTitle))

        // Then
        val exp = HabitDialogState(title = newTitle)
        val act = viewModel.state.first()
        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_habit_id_WHEN_onSetHabitToTextField_THEN_updates_habit_and_disables_save() =
        runTest {
            // Given
            val habitId = 1
            val habit = Habit(id = habitId, name = "Exercise")
            coEvery { repository.getHabit(habitId) } returns habit

            // When
            viewModel.onIntent(HabitDialogIntent.OnSetHabitToTextField(habitId))

            // Then
            val exp = HabitDialogState(habitUi = HabitUi(id = habitId, name = "Exercise"))
            val act = viewModel.state.first()
            assertEquals(exp, act)
        }

    @Test
    fun test_GIVEN_invalid_habit_id_WHEN_onSetHabitToTextField_THEN_state_unchanged() = runTest {
        // Given
        val habitId = 999
        coEvery { repository.getHabit(habitId) } returns null

        // When
        viewModel.onIntent(HabitDialogIntent.OnSetHabitToTextField(habitId))

        // Then
        val exp = HabitDialogState()
        val act = viewModel.state.first()
        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_habit_set_WHEN_onTextChanged_to_new_text_THEN_enables_save() = runTest {
        // Given
        val habitId = 1
        val habit = Habit(id = habitId, name = "Exercise")
        coEvery { repository.getHabit(habitId) } returns habit
        every { habitValidationUseCase(allAny(), allAny()) } returns true
        viewModel.onIntent(HabitDialogIntent.OnSetHabitToTextField(habitId)) // Sets initialHabitName

        // When
        viewModel.onIntent(HabitDialogIntent.OnTextChanged("Run"))

        // Then
        val exp = HabitDialogState(
            habitUi = HabitUi(id = habitId, name = "Run"),
            saveEnabled = true
        )
        val act = viewModel.state.first()
        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_habit_set_WHEN_onTextChanged_to_same_text_THEN_disables_save() = runTest {
        // Given
        val habitId = 1
        val habit = Habit(id = habitId, name = "Exercise")
        coEvery { repository.getHabit(habitId) } returns habit
        viewModel.onIntent(HabitDialogIntent.OnSetHabitToTextField(habitId)) // Sets initialHabitName

        // When
        viewModel.onIntent(HabitDialogIntent.OnTextChanged("Exercise"))

        // Then
        val exp = HabitDialogState(habitUi = HabitUi(id = habitId, name = "Exercise"))
        val act = viewModel.state.first()
        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_empty_text_WHEN_onTextChanged_THEN_disables_save() = runTest {
        // Given
        val newText = ""

        // When
        viewModel.onIntent(HabitDialogIntent.OnTextChanged(newText))

        // Then
        val exp = HabitDialogState()
        val act = viewModel.state.first()
        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_new_habit_WHEN_onSave_THEN_creates_habit_and_emits_dismiss() = runTest {
        // Given
        viewModel.onIntent(HabitDialogIntent.OnTextChanged("Run")) // Sets name, initialHabitName empty
        coEvery { repository.createHabit("Run") } returns Unit

        // Then
        val exp = HabitDialogSideEffect.OnDismiss
        var act: HabitDialogSideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            act = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(HabitDialogIntent.OnSave)

        // Then
        coVerify { repository.createHabit("Run") }

        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_existing_habit_WHEN_onSave_THEN_updates_habit_and_emits_dismiss() = runTest {
        // Given
        val habitId = 1
        val habit = Habit(id = habitId, name = "Exercise")
        coEvery { repository.getHabit(habitId) } returns habit
        viewModel.onIntent(HabitDialogIntent.OnSetHabitToTextField(habitId)) // Sets initialHabitName
        viewModel.onIntent(HabitDialogIntent.OnTextChanged("Run")) // Changes name
        coEvery { repository.updateHabitName(habitId, "Run") } returns Unit

        // Then
        val exp = HabitDialogSideEffect.OnDismiss
        var act: HabitDialogSideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            act = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(HabitDialogIntent.OnSave)

        // Then
        coVerify { repository.updateHabitName(habitId, "Run") }
        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_any_state_WHEN_onCancel_THEN_emits_dismiss() = runTest {
        // Given - no specific setup needed

        // Then
        val exp = HabitDialogSideEffect.OnDismiss
        var act: HabitDialogSideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            act = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(HabitDialogIntent.OnCancel)

        // Then
        assertEquals(exp, act)
    }
}