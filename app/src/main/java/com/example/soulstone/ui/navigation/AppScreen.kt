package com.example.soulstone.ui.navigation

sealed class AppScreen(val route: String) {
    object Home : AppScreen("home")
    object ChineseBirthstones : AppScreen("chinese_birthstones")
    object StoneUses : AppScreen("stone_uses")
    object StoneForX : AppScreen("stone_for_x/{benefitId}") {
        fun createRoute(benefitId: Int) = "stone_for_x/$benefitId"

    }
    object StoneDetails : AppScreen("stone_details/{stoneId}") {
        fun createRoute(stoneId: Int) = "stone_details/$stoneId"

    }
    object SevenChakraStones : AppScreen("seven_chakra_stones")
    object HoroscopeSignDetails : AppScreen("horoscope_sign_details/{signName}") {
        fun createRoute(signName: String) = "horoscope_sign_details/$signName"
    }
    object ChakraDetails : AppScreen("chakra_details/{keyName}") {
        fun createRoute(keyName: String) = "chakra_details/$keyName"
    }
    object ChineseSignDetails : AppScreen("chinese_sign_details/{keyName}") {
        fun createRoute(keyName: String) = "chinese_sign_details/$keyName"
    }
    object GemstoneIndex : AppScreen("gemstone_index")
    object Login : AppScreen("login")
    object Dashboard : AppScreen("dashboard")
    object AddStone : AppScreen("add_stone")
}