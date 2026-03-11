package it.faffolao.phdcounterapp.counter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp

@Composable
fun GradientNumber(
    count: Int,
    modifier: Modifier = Modifier
) {
    // colori gradiente
    val topColor = Color(0xFFFFDF75) // Giallo caldo
    val bottomColor = Color(0xFFCEFF7A) // Verde chiaro/lime

    Box(
        modifier = modifier
            .size(width = 300.dp, height = 400.dp)
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                shadow = Shadow(
                    radius = 1.dp,
                    spread = 0.dp,
                    color = Color(0x40000000)
                )
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(topColor, bottomColor)
                ),
                shape = RoundedCornerShape(16.dp),
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedCounter(count = count)
    }
}