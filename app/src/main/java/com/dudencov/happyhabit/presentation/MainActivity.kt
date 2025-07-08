package com.dudencov.happyhabit.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dudencov.happyhabit.data.notifications.NotificationPermissionHelper
import com.dudencov.happyhabit.presentation.navigation.AppNavHost
import com.dudencov.happyhabit.presentation.theme.HappyHabitTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var notificationPermissionHelper: NotificationPermissionHelper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    private var navController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !notificationPermissionHelper.hasNotificationPermission()) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            HappyHabitTheme {
                navController = rememberNavController()
                AppNavHost(navController!!)
            }
        }

        if (Build.TYPE=="debug") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                        .detectUnsafeIntentLaunch()
                        .build()
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == "com.example.happyhabit.ACTION_SHOW_REMINDER") {
            navController?.apply {
                popBackStack(graph.startDestinationId, false)
                navigate("home")
            }
        }
    }

}
