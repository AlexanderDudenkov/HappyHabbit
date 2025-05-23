package com.dudencov.happyhabit.presentation.habitdialog

import android.os.Parcelable
import com.dudencov.happyhabit.R
import com.dudencov.happyhabit.presentation.entities.HabitUi
import kotlinx.parcelize.Parcelize

data class HabitDialogState(
    val title: HabitDialogTitle = HabitDialogTitle.CREATE,
    val habitUi: HabitUi = HabitUi(),
    val saveEnabled: Boolean = false,
    val errorResId: Int? = null,
)

@Parcelize
enum class HabitDialogTitle(val stringResId: Int) : Parcelable {
    CREATE(R.string.habit_dialog_create_habit_title),
    EDIT(R.string.habit_dialog_edit_habit_title)
}