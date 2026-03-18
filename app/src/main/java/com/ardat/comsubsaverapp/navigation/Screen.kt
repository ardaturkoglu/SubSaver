package com.ardat.comsubsaverapp.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object AddSubscription : Screen("add_subscription")
    data object EditSubscription : Screen("edit_subscription/{subscriptionId}") {
        fun createRoute(subscriptionId: Long) = "edit_subscription/$subscriptionId"
    }
    data object Settings : Screen("settings")
}
