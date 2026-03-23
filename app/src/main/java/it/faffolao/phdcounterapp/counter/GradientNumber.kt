package it.faffolao.phdcounterapp.counter

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.Morph
import it.faffolao.phdcounterapp.MorphPolygonShape

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GradientNumber(
    count: Int,
    onCounterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shapes = remember {
        listOf(
            MaterialShapes.Circle,
            MaterialShapes.Square,
            MaterialShapes.Arch,
            MaterialShapes.SoftBurst,
            MaterialShapes.Pentagon,
            MaterialShapes.Gem
        )
    }

    val shapeIndex = (if (count < 0) 0 else count) % shapes.size

    var fromShape by remember { mutableStateOf(shapes[0]) }
    var toShape by remember { mutableStateOf(shapes[shapeIndex]) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(shapeIndex) {
        val targetShape = shapes[shapeIndex]
        if (targetShape != toShape) {
            fromShape = toShape
            toShape = targetShape
            progress.snapTo(0f)
            progress.animateTo(1f, tween(durationMillis = 600))
        }
    }

    val morph = remember(fromShape, toShape) {
        Morph(fromShape, toShape)
    }

    val morphShape = remember(morph, progress.value) {
        MorphPolygonShape(morph, progress.value)
    }

    // selezione dei colori Material 3 basata sul valore del contatore
    val targetBackgroundColor = if (count > 0) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.errorContainer
    }

    val targetContentColor = if (count > 0) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onErrorContainer
    }

    // animazione del cambio di colori quando il valore del contatore è <= 0
    val backgroundColor by animateColorAsState(
        targetValue = targetBackgroundColor,
        animationSpec = tween(durationMillis = 400),
        label = "BackgroundColorAnimation"
    )

    val contentColor by animateColorAsState(
        targetValue = targetContentColor,
        animationSpec = tween(durationMillis = 400),
        label = "ContentColorAnimation"
    )

    Box(
        modifier = modifier
            .size(size = 300.dp)

            .background(
                color = backgroundColor,
                shape = morphShape,
            )
            .clip(morphShape)
            .clickable { onCounterClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Credits",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor.copy(alpha = 0.7f), // Colore coerente con lo sfondo
                modifier = Modifier.padding(bottom = 8.dp)
            )
            AnimatedCounter(count = count, contentColor = contentColor)
        }
    }
}