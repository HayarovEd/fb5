package com.edurda77.fb5.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.edurda77.fb5.ui.theme.Pink80
import com.edurda77.fb5.ui.theme.back1
import com.edurda77.fb5.ui.theme.back2
import com.edurda77.fb5.ui.theme.back3

@Composable
fun LoadingScreen (
    modifier: Modifier = Modifier,
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(back1, back2, back3, back2, back1),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = gradientBrush),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = modifier.size(100.dp),
            color = Pink80
        )
    }
}