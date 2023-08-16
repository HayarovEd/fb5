package com.edurda77.fb5.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edurda77.fb5.R
import com.edurda77.fb5.ui.theme.back1
import com.edurda77.fb5.ui.theme.back2
import com.edurda77.fb5.ui.theme.back3

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MockScreen(
    modifier: Modifier = Modifier,
    content: String,
    onClick: () -> Unit
) {
    val visible: MutableState<Boolean> = remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(back1, back2, back3, back2, back1),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
            .padding(10.dp)
    ) {
        AnimatedVisibility(
            modifier = modifier.align(alignment = Alignment.Center),
            visible = visible.value,
            enter = slideInVertically {
                with(density) { -40.dp.roundToPx() }
            } + expandVertically(
                expandFrom = Alignment.Top
            ) + scaleIn(
                initialScale = 0.0f
            ),
            exit = slideOutVertically() + shrinkVertically() + scaleOut()
        )
        {
            Box {
                Image(
                    modifier = modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.Center),
                    painter = painterResource(id = R.drawable.ball),
                    contentDescription = "",
                )
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.Center),
                    text = content,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Button(
            modifier = modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter),
            onClick = {
                if (visible.value) {
                    visible.value = false
                } else {
                    visible.value = true
                    onClick.invoke()
                }
            }) {
            Text(text = if (visible.value) "try again" else "predict an event")
        }
    }

}