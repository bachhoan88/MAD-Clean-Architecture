package com.example.weather.presentation.ui.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun BackgroundImage(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentScale: ContentScale = ContentScale.FillWidth,
    description: String? = null,
    alignment: Alignment = Alignment.Center,
    alpha: Float = 1f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize(align = Alignment.BottomCenter)
            .background(color = MaterialTheme.colors.background)
    ) {
        Image(
            painter = painter,
            contentDescription = description,
            contentScale = contentScale,
            modifier = Modifier.matchParentSize(),
            alignment = alignment,
            alpha = alpha
        )
        content()
    }
}
