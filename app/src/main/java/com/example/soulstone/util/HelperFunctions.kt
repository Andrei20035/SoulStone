package com.example.soulstone.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

fun Context.getDrawableIdByName(imageName: String): Int {
    return this.resources.getIdentifier(
        imageName,
        "drawable",
        this.packageName
    )
}

@Composable
fun DescriptionTextStyle() = androidx.compose.ui.text.TextStyle(
    fontSize = 30.sp,
    lineHeight = 36.sp,
    color = Color.Black
)

fun String?.toSqlList(): List<String> {
    return this?.split(",")
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
        ?: emptyList()
}