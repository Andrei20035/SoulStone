package com.example.soulstone.ui.models

sealed interface StoneGridItem {
    data class StoneData(
        val id: Int,
        val name: String,
        val imageResId: Int = 0,
        val imageFileName: String? = null
    ) : StoneGridItem

    data class Navigation(
        val type: NavType,
        val enabled: Boolean
    ) : StoneGridItem

    object Empty : StoneGridItem

    enum class NavType { PREVIOUS, NEXT }
}