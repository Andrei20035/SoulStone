package com.example.soulstone.screens.navigation

sealed class AppScreen(val route: String) {
    object Home : AppScreen("home")
    object ChineseBirthstones : AppScreen("chinese_birthstones")
    object StoneUses : AppScreen("stone_uses")
    object StoneForX : AppScreen("stone_for_x")
    object StoneDetails : AppScreen("stone_details")
    object SevenChakraStones : AppScreen("seven_chakra_stones")
    object HoroscopeSignDetails : AppScreen("horoscope_sign_details")
    object ChakraDetails : AppScreen("chakra_details")
    object ChineseSignDetails : AppScreen("chinese_sign_details")
    object GemstoneIndex : AppScreen("gemstone_index")
    object Login : AppScreen("login")
    object Dashboard : AppScreen("dashboard")
    object AddStone : AppScreen("add_stone")
}