package com.example.soulstone.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soulstone.data.pojos.StoneListItem
import com.example.soulstone.ui.models.StoneListUiItem
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

data class ZodiacCenterData(val imageFileName: String, val imageResId: Int = 0)

@Composable
fun ZodiacStoneWheelViewer(
    centerData: ZodiacCenterData,
    stonesList: List<StoneListUiItem>,
    onStoneClick: (Int) -> Unit,
    @DrawableRes backgroundImageRes: Int? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(8.dp)
    ) {
        if (backgroundImageRes != null) {
            Image(
                painter = painterResource(id = backgroundImageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        CircularStoneLayout(
            centerContent = {
                ZodiacCenterView(data = centerData)
            },
            stonesContent = {
                stonesList.forEach { stone ->
                    StoneItemView(data = stone, onClick = { onStoneClick(stone.id) })
                }
            }
        )
    }
}

@Composable
private fun CircularStoneLayout(
    centerContent: @Composable () -> Unit,
    stonesContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Layout(
        contents = listOf(centerContent, stonesContent),
        modifier = modifier.fillMaxSize()
    ) { (centerMeasurables, stoneMeasurables), constraints ->

        val layoutSize = min(constraints.maxWidth, constraints.maxHeight)
        val layoutCenterX = layoutSize / 2
        val layoutCenterY = layoutSize / 2

        val centerConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val centerPlaceable = centerMeasurables.first().measure(centerConstraints)

        val stoneMaxDimension = (layoutSize / 7.5f).toInt()
        val stoneConstraints = Constraints(
            minWidth = 0,
            maxWidth = stoneMaxDimension,
            minHeight = 0,
            maxHeight = (stoneMaxDimension * 1.5f).toInt()
        )

        val stonePlaceables = stoneMeasurables.map { it.measure(stoneConstraints) }

        val layoutRadius = (layoutSize / 2) * 0.60f

        layout(layoutSize, layoutSize) {
            centerPlaceable.place(
                x = layoutCenterX - (centerPlaceable.width / 2),
                y = layoutCenterY - (centerPlaceable.height / 2)
            )

            val totalStones = stonePlaceables.size
            if (totalStones > 0) {
                val angleStep = 360f / totalStones
                var currentAngle = -90.0

                stonePlaceables.forEach { placeable ->
                    val angleRad = Math.toRadians(currentAngle)
                    val stoneCenterX = layoutCenterX + (layoutRadius * cos(angleRad))
                    val stoneCenterY = layoutCenterY + (layoutRadius * sin(angleRad))

                    placeable.place(
                        x = (stoneCenterX - (placeable.width / 2)).roundToInt(),
                        y = (stoneCenterY - (placeable.height / 2)).roundToInt()
                    )

                    currentAngle += angleStep
                }
            }
        }
    }
}

@Composable
fun ZodiacCenterView(data: ZodiacCenterData) {
    Box(
        modifier = Modifier
            .size(280.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        UniversalImage(
            imageResId = data.imageResId,
            imageFileName = data.imageFileName,
            contentDescription = data.imageFileName,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun StoneItemView(data: StoneListUiItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            UniversalImage(
                imageResId = data.imageResId,
                imageFileName = data.imageFileName,
                contentDescription = data.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

        }
        Text(
            text = data.name,
            color = Color(0xFF2B4F84),
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            maxLines = 2,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}