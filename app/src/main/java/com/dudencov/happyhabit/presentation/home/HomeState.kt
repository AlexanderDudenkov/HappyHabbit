package com.dudencov.happyhabit.presentation.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

val previewStub = List(11) { Habit("$it", "habit $it") }

data class HomeState(
    val habits: List<Habit> = emptyList(),
)

@Parcelize
data class Habit(
    val id: String = "",
    val name: String = ""
) : Parcelable