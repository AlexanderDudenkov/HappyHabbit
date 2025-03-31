package com.dudencov.happyhabit.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import junit.framework.TestCase.assertEquals

@ExperimentalCoroutinesApi
class HabitValidationUseCaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: HabitValidationUseCase

    @Before
    fun setup() {
        useCase = HabitValidationUseCase()
    }

    @Test
    fun test_GIVEN_valid_new_name_WHEN_validated_THEN_returns_true() = runTest {
        // Given
        val newName = "Exercise"
        val oldName = ""

        // When
        val result = useCase(newName, oldName)

        // Then
        assertEquals(true, result)
    }

    @Test
    fun test_GIVEN_name_too_short_WHEN_validated_THEN_returns_false() = runTest {
        // Given
        val newName = "Ex"  // Less than 3 characters
        val oldName = ""

        // When
        val result = useCase(newName, oldName)

        // Then
        assertEquals(false, result)
    }

    @Test
    fun test_GIVEN_same_as_old_name_WHEN_validated_THEN_returns_false() = runTest {
        // Given
        val newName = "Exercise"
        val oldName = "Exercise"

        // When
        val result = useCase(newName, oldName)

        // Then
        assertEquals(false, result)
    }

    @Test
    fun test_GIVEN_name_ends_with_space_WHEN_validated_THEN_returns_false() = runTest {
        // Given
        val newName = "Exercise "
        val oldName = ""

        // When
        val result = useCase(newName, oldName)

        // Then
        assertEquals(false, result)
    }

    @Test
    fun test_GIVEN_valid_name_with_spaces_WHEN_validated_THEN_returns_true() = runTest {
        // Given
        val newName = "Morning Exercise"
        val oldName = ""

        // When
        val result = useCase(newName, oldName)

        // Then
        assertEquals(true, result)
    }

    @Test
    fun test_GIVEN_empty_name_WHEN_validated_THEN_returns_false() = runTest {
        // Given
        val newName = ""
        val oldName = ""

        // When
        val result = useCase(newName, oldName)

        // Then
        assertEquals(false, result)
    }

    @Test
    fun test_GIVEN_only_spaces_WHEN_validated_THEN_returns_false() = runTest {
        // Given
        val newName = "   "
        val oldName = ""

        // When
        val result = useCase(newName, oldName)

        // Then
        assertEquals(false, result)
    }
}