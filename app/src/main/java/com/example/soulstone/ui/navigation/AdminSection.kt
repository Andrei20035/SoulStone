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
import com.example.soulstone.ui.screens.add_stone.AddStoneScreen
import com.example.soulstone.ui.screens.admin_dashboard.AdminDashboardScreen
import com.example.soulstone.ui.screens.admin_login.AdminLoginScreen

@Composable
fun AdminSection(
    onExitAdmin: () -> Unit
) {
    val adminNavController = rememberNavController()

    NavHost(
        navController = adminNavController,
        startDestination = "admin_login"
    ) {

        composable(AppScreen.Login.route) {
            AdminLoginScreen(
                navController = adminNavController,
                onNavigateBack = onExitAdmin
            )
        }
        composable(AppScreen.Dashboard.route) {
            AdminDashboardScreen(
                navController = adminNavController,
                onExitAdmin = onExitAdmin
            )
        }
        composable(AppScreen.AddStone.route) {
            AddStoneScreen(
                navController = adminNavController,
            )
        }
    }
}