package com.dudencov.happyhabit.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithSelectedDaysDto(
    @Embedded val habit: HabitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val dates: List<SelectedDateEntity> = emptyList()
)