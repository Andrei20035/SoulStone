package com.example.soulstone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import java.io.File

@Composable
fun UniversalImage(
    imageResId: Int = 0,
    imageFileName: String? = null,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val context = LocalContext.current

    val model = remember(imageResId, imageFileName) {
        if (imageResId != 0) {
            imageResId
        } else if (!imageFileName.isNullOrBlank()) {
            File(context.filesDir, imageFileName)
        } else {
            null
        }
    }

    if (model != null) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(model)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        // Fallback
        Box(modifier = modifier.background(Color.LightGray))
    }
}