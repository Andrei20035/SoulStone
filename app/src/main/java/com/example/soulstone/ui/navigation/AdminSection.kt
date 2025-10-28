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

// This composable lives in your Graph.ADMIN route
@Composable
fun AdminSection(
    onExitAdmin: () -> Unit // Callback to go back to Graph.MAIN
) {
    val adminNavController = rememberNavController()

    NavHost(
        navController = adminNavController,
        startDestination = "admin_login" // Start of the admin flow
    ) {

//        composable("admin_login") {
//            // Your LoginScreen
//            LoginScreen(
//                onLoginSuccess = {
//                    // When login is good, go to the dashboard
//                    adminNavController.navigate("admin_dashboard") {
//                        // Clear the login screen from the back stack
//                        popUpTo("admin_login") { inclusive = true }
//                    }
//                },
//                // You might also want a back button here
//                onNavigateBack = onExitAdmin
//            )
//        }
//
//        composable("admin_dashboard") {
//            // This is your original AdminSection
//            AdminSection(
//                onNavigateBack = onExitAdmin // Use this to exit the admin graph
//            )
//        }

        // ... you can add other admin screens here
    }
}