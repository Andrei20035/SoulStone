package com.example.soulstone.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.soulstone.screens.components.SoulStoneTopBar
import com.example.soulstone.screens.horoscope_monthly_birthstones.HomeScreen
import com.example.soulstone.ui.components.AppBarViewModel
import com.example.soulstone.ui.screens.chinese_birthstones.ChineseBirthstonesScreen
import com.example.soulstone.ui.screens.chinese_sign_details.ChineseSignDetailsScreen
import com.example.soulstone.ui.screens.horoscope_sign_details.HoroscopeSignDetailsScreen
import com.example.soulstone.ui.screens.seven_chakra_stones.SevenChakraScreen
import com.example.soulstone.ui.screens.stone_details.StoneDetailsScreen
import com.example.soulstone.ui.screens.stone_uses.StoneUsesScreen
import com.example.soulstone.ui.screens.stones_for_benefit.StonesForXScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainAppSection(
    onNavigateToAdmin: () -> Unit
) {
    val mainNavController = rememberNavController()
    val appBarViewModel: AppBarViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            SoulStoneTopBar(
                navController = mainNavController,
                viewModel = appBarViewModel,
                onNavigateToAdmin = onNavigateToAdmin,
                onNavigateHome = {
                    mainNavController.navigate(AppScreen.Home.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateChinese = {
                    mainNavController.navigate(AppScreen.ChineseBirthstones.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateStoneUses = {
                    mainNavController.navigate(AppScreen.StoneUses.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateChakras = {
                    mainNavController.navigate(AppScreen.SevenChakraStones.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = AppScreen.Home.route,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            composable(AppScreen.Home.route) {
                HomeScreen(navController = mainNavController)
            }
            composable(AppScreen.ChineseBirthstones.route) {
                ChineseBirthstonesScreen(navController = mainNavController)
            }
            composable(AppScreen.StoneUses.route) {
                StoneUsesScreen(navController = mainNavController)
            }
            composable(
                route = AppScreen.StoneForX.route,
                arguments = listOf(navArgument("benefitId") {
                    type = NavType.IntType
                })
            ) {
                StonesForXScreen(navController = mainNavController)
            }
            composable(AppScreen.SevenChakraStones.route) {
                SevenChakraScreen(navController = mainNavController)
            }
//            composable(AppScreen.GemstoneIndex.route) { GemstoneIndexScreen() }

            // Stone Details screen (with argument)
            composable(
                route = "${AppScreen.StoneDetails.route}/{stoneId}",
                arguments = listOf(navArgument("stoneId") { type = NavType.IntType })
            ) {
                StoneDetailsScreen(navController = mainNavController)
            }
//
//            // Horoscope Sign Details screen (with argument)
            composable(
                route = AppScreen.HoroscopeSignDetails.route,
                arguments = listOf(navArgument("signName") { type = NavType.StringType })
            ) {
                HoroscopeSignDetailsScreen(navController = mainNavController)
            }
//
//            // Chakra Details screen (with argument)
//            composable(
//                route = AppScreen.ChakraDetails.route,
//                arguments = listOf(navArgument("chakraId") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val chakraId = backStackEntry.arguments?.getString("chakraId")
//                ChakraDetailsScreen(chakraId = chakraId)
//            }
//
            composable(
                route = AppScreen.ChineseSignDetails.route,
                arguments = listOf(navArgument("keyName") { type = NavType.StringType })
            ) {
                ChineseSignDetailsScreen(navController = mainNavController)
            }
        }
    }
}