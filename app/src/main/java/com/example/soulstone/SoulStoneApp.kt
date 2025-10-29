package com.example.soulstone

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soulstone.ui.navigation.AdminSection
import com.example.soulstone.ui.navigation.MainAppSection

object Graph {
    const val MAIN = "main_graph"
    const val ADMIN = "admin_graph"
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SoulStoneAppUI(modifier: Modifier = Modifier) {
    val appNavController = rememberNavController()

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