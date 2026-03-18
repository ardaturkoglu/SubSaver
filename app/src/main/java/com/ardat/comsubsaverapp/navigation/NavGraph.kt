package com.ardat.comsubsaverapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ardat.comsubsaverapp.SubSaverApp
import com.ardat.comsubsaverapp.feature.dashboard.DashboardScreen
import com.ardat.comsubsaverapp.feature.dashboard.DashboardViewModel
import com.ardat.comsubsaverapp.feature.dashboard.DashboardViewModelFactory
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
            val app = LocalContext.current.applicationContext as SubSaverApp
            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModelFactory(app.subscriptionRepository)
            )
            DashboardScreen(
                viewModel = dashboardViewModel,
                onAddClick = { navController.navigate(Screen.AddSubscription.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onSubscriptionClick = { id ->
                    navController.navigate(Screen.EditSubscription.createRoute(id))
                }
            )
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
