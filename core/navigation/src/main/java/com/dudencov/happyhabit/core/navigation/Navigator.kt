package com.dudencov.happyhabit.core.navigation

interface Navigator {
    fun navigateTo(route: String)
    fun navigateUp(): Boolean
    fun popBackStack()
    fun getStringArgument(key: String): String?
    fun getIntArgument(key: String): Int?
}