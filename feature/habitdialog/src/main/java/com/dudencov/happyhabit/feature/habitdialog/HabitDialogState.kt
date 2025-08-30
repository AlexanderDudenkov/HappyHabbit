package com.dudencov.happyhabit.feature.habitdialog

import android.os.Parcelable
import com.dudencov.happyhabit.core.ui.entities.HabitUi
import kotlinx.parcelize.Parcelize
import com.dudencov.happyhabit.core.ui.R as UiR

data class HabitDialogState(
    val title: HabitDialogTitle = HabitDialogTitle.CREATE,
    val habitUi: HabitUi = HabitUi(),
    val saveEnabled: Boolean = false,
    val errorResId: Int? = null,
)

@Parcelize
enum class HabitDialogTitle(val stringResId: Int) : Parcelable {
    CREATE(UiR.string.habit_dialog_create_habit_title),
    EDIT(UiR.string.habit_dialog_edit_habit_title)
}