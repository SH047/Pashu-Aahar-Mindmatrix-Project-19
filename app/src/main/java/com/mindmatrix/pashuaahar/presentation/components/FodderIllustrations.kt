package com.mindmatrix.pashuaahar.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.LeafGreen
import com.mindmatrix.pashuaahar.presentation.theme.Soil
import com.mindmatrix.pashuaahar.presentation.theme.Turmeric
import com.mindmatrix.pashuaahar.presentation.theme.Sky

@Composable
fun TallGrassIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        val color = LeafGreen
        repeat(6) { i ->
            val startX = size.width * (0.2f + i * 0.12f)
            val path = Path().apply {
                moveTo(startX, size.height)
                quadraticBezierTo(
                    startX - 10.dp.toPx(), size.height * 0.4f,
                    startX + 15.dp.toPx(), 10.dp.toPx()
                )
            }
            drawPath(path, color, style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round))
        }
    }
}

@Composable
fun DryFodderIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        val color = Turmeric.copy(alpha = 0.8f)
        repeat(8) { i ->
            val angle = i * 45f
            drawLine(
                color = color,
                start = Offset(size.width / 2, size.height / 2),
                end = Offset(
                    size.width / 2 + 30.dp.toPx() * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat(),
                    size.height / 2 + 30.dp.toPx() * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()
                ),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
        drawCircle(Soil.copy(alpha = 0.3f), radius = 10.dp.toPx())
    }
}

@Composable
fun ConcentrateIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        drawOval(
            color = Soil.copy(alpha = 0.2f),
            topLeft = Offset(10.dp.toPx(), 60.dp.toPx()),
            size = Size(80.dp.toPx(), 30.dp.toPx())
        )
        repeat(15) { i ->
            drawCircle(
                color = Soil,
                radius = 3.dp.toPx(),
                center = Offset(
                    20.dp.toPx() + (i % 5) * 15.dp.toPx(),
                    40.dp.toPx() + (i / 5) * 10.dp.toPx()
                )
            )
        }
    }
}

@Composable
fun SuperFeedIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        drawCircle(Sky.copy(alpha = 0.1f), radius = 45.dp.toPx())
        repeat(5) { i ->
            drawOval(
                color = FieldGreen,
                topLeft = Offset(
                    size.width * 0.3f + (i * 10.dp.toPx()) % 40.dp.toPx(),
                    size.height * 0.3f + (i * 15.dp.toPx()) % 40.dp.toPx()
                ),
                size = Size(15.dp.toPx(), 10.dp.toPx())
            )
        }
    }
}

@Composable
fun LegumeIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        val color = FieldGreen
        repeat(3) { i ->
            val angle = i * 120f
            drawCircle(
                color = color.copy(alpha = 0.6f),
                radius = 12.dp.toPx(),
                center = Offset(
                    size.width / 2 + 15.dp.toPx() * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat(),
                    size.height / 2 + 15.dp.toPx() * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()
                )
            )
        }
        drawLine(color, Offset(size.width / 2, size.height / 2), Offset(size.width / 2, size.height), strokeWidth = 3.dp.toPx())
    }
}

@Composable
fun TreeFodderIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        val color = LeafGreen
        drawLine(Soil, Offset(size.width / 2, size.height), Offset(size.width / 2, size.height * 0.3f), strokeWidth = 6.dp.toPx())
        repeat(4) { i ->
            val y = size.height * (0.3f + i * 0.15f)
            val isLeft = i % 2 == 0
            drawOval(
                color = color,
                topLeft = Offset(if (isLeft) size.width * 0.25f else size.width * 0.55f, y),
                size = Size(20.dp.toPx(), 12.dp.toPx())
            )
        }
    }
}

@Composable
fun HydroponicIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        // Tray
        drawRoundRect(Color.Gray.copy(alpha = 0.3f), topLeft = Offset(10.dp.toPx(), 70.dp.toPx()), size = Size(80.dp.toPx(), 15.dp.toPx()), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()))
        // Sprouts
        repeat(10) { i ->
            val x = 15.dp.toPx() + i * 7.dp.toPx()
            drawLine(LeafGreen, Offset(x, 70.dp.toPx()), Offset(x, 40.dp.toPx()), strokeWidth = 2.dp.toPx())
        }
    }
}

@Composable
fun NutrientBalanceMeter(
    grassPercent: Float,
    legumePercent: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(width = 200.dp, height = 20.dp)) {
        val grassWidth = size.width * grassPercent
        val legumeWidth = size.width * legumePercent
        
        // Background
        drawRoundRect(Color.LightGray.copy(alpha = 0.2f), size = size, cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx()))
        
        // Grass (Energy)
        drawRoundRect(LeafGreen, size = Size(grassWidth, size.height), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx()))
        
        // Legumes (Protein)
        drawRoundRect(
            Sky,
            topLeft = Offset(grassWidth, 0f),
            size = Size(legumeWidth, size.height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx())
        )
        
        // Marker for Golden Ratio (70%)
        drawLine(Color.White, Offset(size.width * 0.7f, 0f), Offset(size.width * 0.7f, size.height), strokeWidth = 2.dp.toPx())
    }
}

@Composable
fun GrazingSceneIllustration(grazingHours: Float, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(140.dp)) {
        // Sky
        drawRect(Sky.copy(alpha = 0.1f))
        
        // Ground
        val groundY = size.height * 0.7f
        drawRect(Soil.copy(alpha = 0.15f), topLeft = Offset(0f, groundY))
        
        // Grass (depletes as hours increase)
        val grassHeight = (1f - (grazingHours / 8f)).coerceIn(0.2f, 1f) * 20.dp.toPx()
        repeat(12) { i ->
            val x = size.width * (i.toFloat() / 12)
            drawLine(
                color = LeafGreen.copy(alpha = 0.8f),
                start = Offset(x, groundY),
                end = Offset(x + 5.dp.toPx(), groundY - grassHeight),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
        
        // Simple cow body standing
        drawOval(
            color = Color.White,
            topLeft = Offset(size.width * 0.3f, groundY - 35.dp.toPx()),
            size = Size(60.dp.toPx(), 35.dp.toPx())
        )
        drawCircle(Color.White, radius = 10.dp.toPx(), center = Offset(size.width * 0.3f, groundY - 30.dp.toPx()))
    }
}

@Composable
fun WaterTroughIllustration(isFull: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        val troughPath = Path().apply {
            moveTo(10.dp.toPx(), 40.dp.toPx())
            lineTo(90.dp.toPx(), 40.dp.toPx())
            lineTo(80.dp.toPx(), 80.dp.toPx())
            lineTo(20.dp.toPx(), 80.dp.toPx())
            close()
        }
        drawPath(troughPath, Color.Gray.copy(alpha = 0.4f))
        if (isFull) {
            val waterPath = Path().apply {
                moveTo(15.dp.toPx(), 50.dp.toPx())
                lineTo(85.dp.toPx(), 50.dp.toPx())
                lineTo(77.dp.toPx(), 75.dp.toPx())
                lineTo(23.dp.toPx(), 75.dp.toPx())
                close()
            }
            drawPath(waterPath, Sky.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun MixBucketMiniIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(40.dp)) {
        drawRoundRect(Color.LightGray, size = size, cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()))
        drawCircle(FieldGreen, radius = 8.dp.toPx(), center = Offset(size.width/2, size.height/2))
        drawCircle(Sky, radius = 4.dp.toPx(), center = Offset(size.width/2 - 5.dp.toPx(), size.height/2 + 5.dp.toPx()))
    }
}

@Composable
fun BranIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        drawOval(Color.LightGray.copy(alpha = 0.3f), topLeft = Offset(10.dp.toPx(), 65.dp.toPx()), size = Size(80.dp.toPx(), 25.dp.toPx()))
        repeat(12) { i ->
            drawCircle(Color.Gray, radius = 2.dp.toPx(), center = Offset(20.dp.toPx() + i * 5.dp.toPx(), 60.dp.toPx() - (i % 3) * 4.dp.toPx()))
        }
    }
}

@Composable
fun PelletIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        repeat(10) { i ->
            drawRoundRect(
                Soil.copy(alpha = 0.8f),
                topLeft = Offset(20.dp.toPx() + (i % 4) * 15.dp.toPx(), 30.dp.toPx() + (i / 4) * 15.dp.toPx()),
                size = Size(10.dp.toPx(), 6.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
            )
        }
    }
}

@Composable
fun CakeIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        drawRoundRect(Soil.copy(alpha = 0.6f), topLeft = Offset(15.dp.toPx(), 25.dp.toPx()), size = Size(70.dp.toPx(), 50.dp.toPx()), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx()))
        drawLine(Color.Black.copy(alpha = 0.1f), Offset(15.dp.toPx(), 50.dp.toPx()), Offset(85.dp.toPx(), 50.dp.toPx()), strokeWidth = 1.dp.toPx())
        drawLine(Color.Black.copy(alpha = 0.1f), Offset(50.dp.toPx(), 25.dp.toPx()), Offset(50.dp.toPx(), 75.dp.toPx()), strokeWidth = 1.dp.toPx())
    }
}

@Composable
fun DynamicFeedTrough(
    hasRoughage: Boolean,
    hasConcentrate: Boolean,
    hasSuperFeed: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(width = 240.dp, height = 120.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val troughPath = Path().apply {
                moveTo(0f, 40.dp.toPx())
                lineTo(size.width, 40.dp.toPx())
                lineTo(size.width * 0.85f, size.height)
                lineTo(size.width * 0.15f, size.height)
                close()
            }
            drawPath(troughPath, Color.LightGray.copy(alpha = 0.5f))
            
            if (hasRoughage) {
                drawRect(
                    color = LeafGreen.copy(alpha = 0.3f),
                    topLeft = Offset(size.width * 0.2f, 60.dp.toPx()),
                    size = Size(size.width * 0.6f, size.height * 0.4f)
                )
            }
            if (hasConcentrate) {
                repeat(20) { i ->
                    drawCircle(
                        color = Soil.copy(alpha = 0.6f),
                        radius = 4.dp.toPx(),
                        center = Offset(
                            size.width * 0.3f + (i * 12.dp.toPx()) % (size.width * 0.4f),
                            size.height * 0.7f + (i % 3) * 5.dp.toPx()
                        )
                    )
                }
            }
            if (hasSuperFeed) {
                drawCircle(
                    color = Sky.copy(alpha = 0.2f),
                    radius = 30.dp.toPx(),
                    center = Offset(size.width / 2, size.height / 2 + 10.dp.toPx())
                )
            }
        }
    }
}
