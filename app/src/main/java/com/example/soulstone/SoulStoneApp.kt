package com.example.soulstone

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soulstone.ui.navigation.AdminSection
import com.example.soulstone.ui.navigation.AppScreen
import com.example.soulstone.ui.navigation.MainAppSection
import com.example.soulstone.util.InactivityManager

object Graph {
    const val MAIN = "main_graph"
    const val ADMIN = "admin_graph"
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SoulStoneAppUI(
    inactivityManager: InactivityManager,
    modifier: Modifier = Modifier
) {
    val appNavController = rememberNavController()

    LaunchedEffect(Unit) {
        inactivityManager.timeoutEvent.collect {
            appNavController.navigate(Graph.MAIN) {
                popUpTo(0) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = appNavController,
        startDestination = Graph.MAIN,
        modifier = modifier
    ) {
        composable(Graph.MAIN) {
            MainAppSection(
                onNavigateToAdmin = {
                    appNavController.navigate(Graph.ADMIN)
                }
            )
        }

        composable(Graph.ADMIN) {
            AdminSection(
                onExitAdmin = {
                    appNavController.popBackStack()
                }
            )
        }
    }
}