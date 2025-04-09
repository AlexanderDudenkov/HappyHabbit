package com.dudencov.happyhabit.presentation.deleteconfirmation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dudencov.happyhabit.domain.data.HabitRepository
import com.dudencov.happyhabit.presentation.deleteconfirmationdialog.DeleteConfirmationDialogIntent
import com.dudencov.happyhabit.presentation.deleteconfirmationdialog.DeleteConfirmationDialogSideEffect
import com.dudencov.happyhabit.presentation.deleteconfirmationdialog.DeleteConfirmationDialogState
import com.dudencov.happyhabit.presentation.deleteconfirmationdialog.DeleteConfirmationDialogViewModel
import io.mockk.coEvery
import io.mockk.coVerify
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
class DeleteConfirmationDialogViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DeleteConfirmationDialogViewModel
    private lateinit var repository: HabitRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = DeleteConfirmationDialogViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_GIVEN_habit_id_WHEN_onSetHabitId_THEN_updates_state() = runTest {
        // Given
        val habitId = 1

        // When
        viewModel.onIntent(DeleteConfirmationDialogIntent.OnSetHabitId(habitId))

        // Then
        val exp = DeleteConfirmationDialogState(habitId = habitId)
        val act = viewModel.state.first()

        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_habit_id_WHEN_onConfirm_THEN_deletes_habit_and_emits_dismiss() = runTest {
        // Given
        val habitId = 1
        coEvery { repository.deleteHabit(habitId) } returns Unit
        viewModel.onIntent(DeleteConfirmationDialogIntent.OnSetHabitId(habitId))

        // Then
        val exp = DeleteConfirmationDialogSideEffect.OnDismiss
        var act: DeleteConfirmationDialogSideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            act = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(DeleteConfirmationDialogIntent.OnConfirm)

        // Then
        coVerify { repository.deleteHabit(habitId) }
        assertEquals(exp, act)
    }

    @Test
    fun test_GIVEN_any_state_WHEN_onDismiss_THEN_emits_dismiss() = runTest {
        // Given - no specific setup needed

        // Then
        val exp = DeleteConfirmationDialogSideEffect.OnDismiss
        var act: DeleteConfirmationDialogSideEffect? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            act = viewModel.sideEffect.first()
        }

        // When
        viewModel.onIntent(DeleteConfirmationDialogIntent.OnDismiss)

        // Then
        assertEquals(exp, act)
    }
} 