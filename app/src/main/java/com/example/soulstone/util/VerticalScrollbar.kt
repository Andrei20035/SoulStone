package com.example.soulstone.util

import androidx.compose.foundation.ScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.simpleVerticalScrollbar(
    state: ScrollState,
    width: Dp = 6.dp,
    color: Color = Color(0xFF2B4F84).copy(alpha = 0.5f),
    padding: Dp = 4.dp
): Modifier = drawWithContent {
    drawContent()

    val firstVisibleElementIndex = state.value.toFloat()
    val elementHeight = this.size.height
    val totalContentHeight = elementHeight + state.maxValue

    // Only draw scrollbar if content is larger than the view port
    if (totalContentHeight > elementHeight) {
        val scrollbarHeight = elementHeight * (elementHeight / totalContentHeight)
        val scrollbarOffsetY = firstVisibleElementIndex * (elementHeight / totalContentHeight)

        drawRoundRect(
            color = color,
            topLeft = Offset(this.size.width - width.toPx() - padding.toPx(), scrollbarOffsetY),
            size = Size(width.toPx(), scrollbarHeight),
            cornerRadius = CornerRadius(width.toPx() / 2, width.toPx() / 2)
        )
    }
}