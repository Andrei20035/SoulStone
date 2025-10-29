package com.example.soulstone.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soulstone.screens.components.SoulStoneTopBar
import com.example.soulstone.screens.horoscope_monthly_birthstones.HomeScreen
import com.example.soulstone.screens.navigation.AppScreen
import com.example.soulstone.ui.components.AppBarViewModel
import com.example.soulstone.ui.screens.chinese_birthstones.ChineseBirthstonesScreen

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
                    mainNavController.navigate(AppScreen.Home.route) { /*...*/ }
                },
                onNavigateChinese = {
                    mainNavController.navigate(AppScreen.ChineseBirthstones.route) { /*...*/ }
                },
                onNavigateStoneUses = {
                    mainNavController.navigate(AppScreen.StoneUses.route) { /*...*/ }
                },
                onNavigateChakras = {
                    mainNavController.navigate(AppScreen.SevenChakraStones.route) { /*...*/ }
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
//            composable(AppScreen.StoneUses.route) { StoneUsesScreen() }
//            composable(AppScreen.StoneForX.route) { StoneForXScreen() }
//            composable(AppScreen.SevenChakraStones.route) { SevenChakraStonesScreen() }
//            composable(AppScreen.GemstoneIndex.route) { GemstoneIndexScreen() }

//            // Stone Details screen (with argument)
//            composable(
//                route = AppScreen.StoneDetails.route,
//                arguments = listOf(navArgument("stoneId") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val stoneId = backStackEntry.arguments?.getString("stoneId")
//                StoneDetailsScreen(stoneId = stoneId)
//            }
//
//            // Horoscope Sign Details screen (with argument)
//            composable(
//                route = AppScreen.HoroscopeSignDetails.route,
//                arguments = listOf(navArgument("signId") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val signId = backStackEntry.arguments?.getString("signId")
//                HoroscopeSignDetailsScreen(signId = signId)
//            }
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
//            // Chinese Sign Details screen (with argument)
//            composable(
//                route = AppScreen.ChineseSignDetails.route,
//                arguments = listOf(navArgument("signId") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val signId = backStackEntry.arguments?.getString("signId")
//                ChineseSignDetailsScreen(signId = signId)
//            }
        }
    }
}