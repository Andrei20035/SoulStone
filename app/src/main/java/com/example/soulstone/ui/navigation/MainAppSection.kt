package com.example.soulstone.ui.navigation

import ChakraDetailsScreen
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
import com.example.soulstone.ui.components.AppBarViewModel
import com.example.soulstone.ui.components.SoulStoneTopBar
import com.example.soulstone.ui.screens.chinese_birthstones.ChineseBirthstonesScreen
import com.example.soulstone.ui.screens.chinese_sign_details.ChineseSignDetailsScreen
import com.example.soulstone.ui.screens.gemstone_index.GemstoneIndexScreen
import com.example.soulstone.ui.screens.horoscope_monthly_birthstones.HomeScreen
import com.example.soulstone.ui.screens.horoscope_sign_details.HoroscopeSignDetailsScreen
import com.example.soulstone.ui.screens.seven_chakra_stones.SevenChakraScreen
import com.example.soulstone.ui.screens.stone_details.StoneDetailsScreen
import com.example.soulstone.ui.screens.stone_uses.StoneUsesScreen
import com.example.soulstone.ui.screens.stones_for_benefit.StonesForXScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainAppSection(
    onNavigateToAdmin: () -> Unit,
    viewModel: AppBarViewModel = hiltViewModel()
) {
    val mainNavController = rememberNavController()

    Scaffold(
        topBar = {
            SoulStoneTopBar(
                navController = mainNavController,
                viewModel = viewModel,
                onNavigateToAdmin = onNavigateToAdmin
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
                arguments = listOf(navArgument("benefitId") { type = NavType.IntType})
            ) {
                StonesForXScreen(navController = mainNavController)
            }
            composable(AppScreen.SevenChakraStones.route) {
                SevenChakraScreen(navController = mainNavController)
            }
            composable(AppScreen.GemstoneIndex.route) {
                GemstoneIndexScreen(navController = mainNavController)
            }
            composable(
                route = AppScreen.StoneDetails.route,
                arguments = listOf(navArgument("stoneId") { type = NavType.IntType })
            ) {
                StoneDetailsScreen(navController = mainNavController)
            }
            composable(
                route = AppScreen.HoroscopeSignDetails.route,
                arguments = listOf(navArgument("signName") { type = NavType.StringType })
            ) {
                HoroscopeSignDetailsScreen(navController = mainNavController)
            }
            composable(
                route = AppScreen.ChakraDetails.route,
                arguments = listOf(navArgument("keyName") { type = NavType.StringType })
            ) {
                ChakraDetailsScreen(navController = mainNavController)
            }

            composable(
                route = AppScreen.ChineseSignDetails.route,
                arguments = listOf(navArgument("keyName") { type = NavType.StringType })
            ) {
                ChineseSignDetailsScreen(navController = mainNavController)
            }
        }
    }
}