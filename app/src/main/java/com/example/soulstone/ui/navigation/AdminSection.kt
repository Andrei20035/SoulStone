package com.example.soulstone.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soulstone.screens.navigation.AdminScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSection(onNavigateBack: () -> Unit) {
    val adminNavController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Admin") }, /* ... */ )
        }
    ) { innerPadding ->
        NavHost(
            navController = adminNavController,
            startDestination = AdminScreen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
//            composable(AdminScreen.Login.route) { AdminLoginScreen() }
//            composable(AdminScreen.Dashboard.route) { AdminDashboardScreen() }
//            composable(AdminScreen.AddStone.route) { AdminAddStoneScreen() }
//
//            // Edit Stone screen (with argument)
//            composable(
//                route = AdminScreen.EditStone.route,
//                arguments = listOf(navArgument("stoneId") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val stoneId = backStackEntry.arguments?.getString("stoneId")
//                AdminEditStoneScreen(stoneId = stoneId)
//            }
        }
    }
}