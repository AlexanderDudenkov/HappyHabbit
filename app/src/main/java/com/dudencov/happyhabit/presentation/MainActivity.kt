package com.dudencov.happyhabit.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dudencov.happyhabit.presentation.navigation.AppNavHost
import com.dudencov.happyhabit.presentation.theme.HappyHabitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HappyHabitTheme {
                AppNavHost()
            }
        }
    }

}
