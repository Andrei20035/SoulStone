package com.example.soulstone.screens.navigation

sealed class AdminScreen(val route: String) {
    object Login : AdminScreen("login")
    object Dashboard : AdminScreen("dashboard")
    object AddStone : AdminScreen("add_stone")
}