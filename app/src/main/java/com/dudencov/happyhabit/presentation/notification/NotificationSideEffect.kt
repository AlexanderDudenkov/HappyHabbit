package com.dudencov.happyhabit.presentation.notification

sealed interface NotificationSideEffect {
    data object RouteBack : NotificationSideEffect
    data class ShowToast(val message: String) : NotificationSideEffect
} 