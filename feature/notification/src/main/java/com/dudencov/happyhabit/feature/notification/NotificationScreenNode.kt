package com.dudencov.happyhabit.feature.notification

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dudencov.happyhabit.core.navigation.Navigator

@Composable
fun NotificationScreenNode(navigator: Navigator, viewModel: NotificationViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    HandleLifecycle(viewModel)
    HandleSideEffects(viewModel, navigator, context)

    NotificationScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun HandleLifecycle(viewModel: NotificationViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.onIntent(NotificationIntent.OnCreate)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun HandleSideEffects(
    viewModel: NotificationViewModel,
    navigator: Navigator,
    context: Context
) {
    val sideEffect = viewModel.sideEffect

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                NotificationSideEffect.RouteBack -> navigator.navigateUp()
                is NotificationSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
} 