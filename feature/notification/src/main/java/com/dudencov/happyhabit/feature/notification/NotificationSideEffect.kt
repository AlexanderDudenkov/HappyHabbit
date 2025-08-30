package com.dudencov.happyhabit.feature.notification

sealed interface NotificationSideEffect {
    data object RouteBack : NotificationSideEffect
    data class ShowToast(val message: String) : NotificationSideEffect
} 