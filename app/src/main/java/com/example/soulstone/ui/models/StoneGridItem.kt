package com.example.soulstone.ui.models

sealed interface StoneGridItem {
    data class StoneData(
        val id: Int,
        val name: String,
        val imageResId: Int
    ) : StoneGridItem

    data class Navigation(
        val type: NavType,
        val enabled: Boolean
    ) : StoneGridItem

    object Empty : StoneGridItem

    enum class NavType { PREVIOUS, NEXT }
}