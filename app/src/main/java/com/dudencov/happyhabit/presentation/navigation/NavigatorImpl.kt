package com.dudencov.happyhabit.presentation.navigation

import androidx.navigation.NavHostController
import com.dudencov.happyhabit.core.navigation.Navigator

class NavigatorImpl(
    private val navController: NavHostController
) : Navigator {
    override fun navigateTo(route: String) {
        navController.navigate(route)
    }

    override fun navigateUp(): Boolean {
        return navController.popBackStack()
    }

    override fun popBackStack() {
        navController.popBackStack()
    }
}