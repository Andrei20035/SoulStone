package com.example.soulstone.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.soulstone.R

val myCustomFontFamily = FontFamily(
    Font(R.font.harabara, FontWeight.Normal),
    Font(R.font.souvnrl, FontWeight.Normal)
)

private val defaultTypography = Typography()

val Typography = Typography(
    bodyLarge = defaultTypography.bodyLarge.copy(
        fontFamily = myCustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    titleLarge = defaultTypography.titleLarge.copy(
        fontFamily = myCustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    labelSmall = defaultTypography.labelSmall.copy(
        fontFamily = myCustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

