package apex.play.time.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import apex.play.time.ui.theme.back1
import apex.play.time.ui.theme.back2
import apex.play.time.ui.theme.back3
import apex.play.time.ui.theme.red

@Composable
fun ErrorScreen (
    modifier: Modifier = Modifier,
    error: String
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
        Text(
            modifier = modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Center),
            text = error,
            fontSize = 30.sp,
            color= red,
            textAlign = TextAlign.Center
        )
    }
}