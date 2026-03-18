package com.ardat.comsubsaverapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun SubSaverNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            // Placeholder — replaced in Phase 2
            PlaceholderScreen("Dashboard")
        }

        composable(Screen.AddSubscription.route) {
            // Placeholder — replaced in Phase 3
            PlaceholderScreen("Add Subscription")
        }

        composable(
            route = Screen.EditSubscription.route,
            arguments = listOf(
                navArgument("subscriptionId") { type = NavType.LongType }
            )
        ) {
            // Placeholder — replaced in Phase 3
            PlaceholderScreen("Edit Subscription")
        }

        composable(Screen.Settings.route) {
            // Placeholder — replaced in Phase 7
            PlaceholderScreen("Settings")
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}
