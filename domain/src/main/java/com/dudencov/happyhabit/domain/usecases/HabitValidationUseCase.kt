package com.dudencov.happyhabit.domain.usecases

import javax.inject.Inject

class HabitValidationUseCase @Inject constructor() {

    operator fun invoke(newName: String, oldName: String = ""): Boolean {
        return newName.length >= 3
                && newName != oldName
                && newName.last() != ' '
    }
}