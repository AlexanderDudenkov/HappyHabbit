package com.dudencov.happyhabit.presentation.notification

import android.content.Intent

sealed interface NotificationSideEffect {
    data object RouteBack : NotificationSideEffect
    data class ShowToast(val message: String) : NotificationSideEffect
    data class OpenSystemSettings(val intent: Intent) : NotificationSideEffect
} 