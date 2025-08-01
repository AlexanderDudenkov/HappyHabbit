package com.dudencov.happyhabit.presentation.navigation

sealed class Routes {

    data object Home : Routes() {
        private const val DESTINATION = "home"
        const val HABIT_ID_ARG = "habit_id"
        const val HABIT_ID_RECEIVE_RESULT = "saved_habit_id"
        const val ROUTE_PATTERN = DESTINATION

        fun createRoute() = DESTINATION
    }

    data object Detail : Routes() {
        private const val DESTINATION = "details"
        const val HABIT_ID_ARG: String = "habit_id"
        const val HABIT_NAME_ARG: String = "habit_name"
        const val ROUTE_PATTERN = "$DESTINATION?$HABIT_ID_ARG={$HABIT_ID_ARG}&$HABIT_NAME_ARG={$HABIT_NAME_ARG}"

        fun createRoute(habitId: Int, habitName: String) =
            "$DESTINATION?$HABIT_ID_ARG=$habitId&$HABIT_NAME_ARG=$habitName"
    }

    data object WeeklyProgress : Routes() {
        private const val DESTINATION = "weekly_progress"
        const val ROUTE_PATTERN = DESTINATION
    }

    data object Notification : Routes() {
        private const val DESTINATION = "notification"
        const val ROUTE_PATTERN = DESTINATION
    }

    data object Settings : Routes() {
        private const val DESTINATION = "settings"
        const val ROUTE_PATTERN = DESTINATION
    }

    data object HabitDialog : Routes() {
        private const val DESTINATION = "habit_dialog"
        const val HABIT_ID_ARG: String = "habit_id"
        const val HABIT_ID_SEND_RESULT = "saved_habit_id"
        const val ROUTE_PATTERN = "$DESTINATION?$HABIT_ID_ARG={$HABIT_ID_ARG}"

        fun createRoute(habitId: Int? = null) = "$DESTINATION?$HABIT_ID_ARG=$habitId"
    }

    data object DeleteConfirmationDialog : Routes() {
        private const val DESTINATION = "delete_confirmation_dialog"
        const val HABIT_ID_ARG = "habitId"
        const val ROUTE_PATTERN = "$DESTINATION/{${HABIT_ID_ARG}}"

        fun createRoute(habitId: Int): String {
            return "$DESTINATION/$habitId"
        }
    }
}