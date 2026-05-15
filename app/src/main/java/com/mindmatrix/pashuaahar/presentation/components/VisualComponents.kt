package com.mindmatrix.pashuaahar.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import com.mindmatrix.pashuaahar.domain.CowProfile
import com.mindmatrix.pashuaahar.presentation.theme.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.*
import java.text.SimpleDateFormat

@Composable
fun ScreenHeader(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
        )
    }
}

@Composable
fun InfoCard(
    title: String,
    value: String,
    detail: String,
    modifier: Modifier = Modifier,
    color: Color = FieldGreen
) {
    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = color.copy(alpha = 0.05f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.22f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelLarge, color = color, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = detail, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Composable
fun VisualBadge(label: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HealthMeter(score: Float, label: String, modifier: Modifier = Modifier) {
    val color = when {
        score < 2.5f -> Clay
        score > 3.8f -> Turmeric
        else -> FieldGreen
    }
    
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(160.dp)) {
                drawArc(
                    brush = Brush.sweepGradient(listOf(Clay, Turmeric, FieldGreen, Sky)),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
                )
                
                val angle = 180f + (score - 1f) / 4f * 180f
                val radius = size.minDimension / 2f - 20.dp.toPx()
                val x = (center.x + radius * Math.cos(Math.toRadians(angle.toDouble()))).toFloat()
                val y = (center.y + radius * Math.sin(Math.toRadians(angle.toDouble()))).toFloat()
                
                drawCircle(color, radius = 8.dp.toPx(), center = Offset(x, y))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 40.dp)) {
                Text(text = score.toString(), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = color)
                Text(text = label, style = MaterialTheme.typography.labelMedium, color = color)
            }
        }
    }
}

@Composable
fun PregnancyProgress(month: Int, modifier: Modifier = Modifier) {
    val progress = month / 9f
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Pregnancy", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "$month / 9 months", style = MaterialTheme.typography.labelLarge, color = FieldGreen)
        }
        Spacer(modifier = Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(7.dp)),
            color = FieldGreen,
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )
    }
}

enum class NavGlyph {
    Home,
    Cow,
    Leaf,
    Shield,
    BarChart
}

@Composable
fun NavIcon(glyph: NavGlyph, selected: Boolean, modifier: Modifier = Modifier) {
    val color = if (selected) FieldGreen else FieldGreen.copy(alpha = 0.55f)
    Canvas(modifier = modifier.size(28.dp)) {
        val strokeWidthVal = 2.6.dp.toPx()
        when (glyph) {
            NavGlyph.Home -> {
                drawLine(color, Offset(size.width * 0.18f, size.height * 0.48f), Offset(size.width * 0.5f, size.height * 0.18f), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                drawLine(color, Offset(size.width * 0.5f, size.height * 0.18f), Offset(size.width * 0.82f, size.height * 0.48f), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                drawRoundRect(color.copy(alpha = 0.2f), topLeft = Offset(size.width * 0.28f, size.height * 0.45f), size = Size(size.width * 0.44f, size.height * 0.36f), cornerRadius = CornerRadius(5.dp.toPx()))
                drawRoundRect(color, topLeft = Offset(size.width * 0.42f, size.height * 0.58f), size = Size(size.width * 0.16f, size.height * 0.23f), cornerRadius = CornerRadius(3.dp.toPx()))
            }
            NavGlyph.Cow -> {
                drawOval(color.copy(alpha = 0.18f), topLeft = Offset(size.width * 0.18f, size.height * 0.3f), size = Size(size.width * 0.64f, size.height * 0.44f))
                drawCircle(color, radius = 2.2.dp.toPx(), center = Offset(size.width * 0.4f, size.height * 0.48f))
                drawCircle(color, radius = 2.2.dp.toPx(), center = Offset(size.width * 0.6f, size.height * 0.48f))
                drawLine(color, Offset(size.width * 0.28f, size.height * 0.28f), Offset(size.width * 0.12f, size.height * 0.18f), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                drawLine(color, Offset(size.width * 0.72f, size.height * 0.28f), Offset(size.width * 0.88f, size.height * 0.18f), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
            }
            NavGlyph.Leaf -> {
                drawOval(color.copy(alpha = 0.22f), topLeft = Offset(size.width * 0.18f, size.height * 0.14f), size = Size(size.width * 0.54f, size.height * 0.62f))
                drawLine(color, Offset(size.width * 0.3f, size.height * 0.72f), Offset(size.width * 0.82f, size.height * 0.22f), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
            }
            NavGlyph.Shield -> {
                drawRoundRect(color.copy(alpha = 0.18f), topLeft = Offset(size.width * 0.22f, size.height * 0.12f), size = Size(size.width * 0.56f, size.height * 0.68f), cornerRadius = CornerRadius(8.dp.toPx()))
                drawLine(color, Offset(size.width * 0.5f, size.height * 0.3f), Offset(size.width * 0.5f, size.height * 0.64f), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
                drawLine(color, Offset(size.width * 0.34f, size.height * 0.47f), Offset(size.width * 0.66f, size.height * 0.47f), strokeWidth = strokeWidthVal, cap = StrokeCap.Round)
            }
            NavGlyph.BarChart -> {
                drawRect(color.copy(alpha = 0.6f), topLeft = Offset(size.width * 0.15f, size.height * 0.6f), size = Size(size.width * 0.18f, size.height * 0.25f))
                drawRect(color, topLeft = Offset(size.width * 0.41f, size.height * 0.35f), size = Size(size.width * 0.18f, size.height * 0.5f))
                drawRect(color.copy(alpha = 0.6f), topLeft = Offset(size.width * 0.67f, size.height * 0.15f), size = Size(size.width * 0.18f, size.height * 0.7f))
            }
        }
    }
}

@Composable
fun SavingsBarChart(marketCost: Float, homeCost: Float, savings: Float, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Bar(label = "Market Feed", value = marketCost, max = marketCost, color = Color.Gray)
        Spacer(modifier = Modifier.height(14.dp))
        Bar(label = "Homemade Mix", value = homeCost, max = marketCost, color = FieldGreen)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Rs $savings daily savings!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = FieldGreen)
    }
}

@Composable
private fun Bar(label: String, value: Float, max: Float, color: Color, modifier: Modifier = Modifier) {
    val widthPercent = (value / max).coerceIn(0f, 1f)
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, style = MaterialTheme.typography.labelLarge)
            Text(text = "Rs $value", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color.copy(alpha = 0.12f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(widthPercent)
                    .fillMaxHeight()
                    .background(color)
            )
        }
    }
}

@Composable
fun BarnInteriorIllustration(modifier: Modifier = Modifier, cowProfiles: List<CowProfile>, onCowClick: (String) -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = modifier.fillMaxWidth().height(320.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val wallColor = Cream.copy(alpha = 0.4f)
            drawRect(wallColor)
            
            // Large circular pen
            drawCircle(
                color = FieldGreen.copy(alpha = 0.15f),
                radius = size.minDimension * 0.45f,
                style = Stroke(width = 4.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f))
            )
        }
        
        // Cows in a circle
        val radius = 100.dp
        
        cowProfiles.forEachIndexed { index, cow ->
            val angle = (index * (360f / cowProfiles.size.coerceAtLeast(1)))
            val x = (kotlin.math.cos(Math.toRadians(angle.toDouble())) * radius.value).dp
            val y = (kotlin.math.sin(Math.toRadians(angle.toDouble())) * radius.value).dp
            
            Column(
                modifier = Modifier
                    .offset(x = x, y = y - bounce.dp) // Synchronized bouncing!
                    .clickable { onCowClick(cow.id) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LivelyCowIllustration(modifier = Modifier.size(100.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(FieldGreen)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = cow.name, style = MaterialTheme.typography.labelSmall, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ScientificCalendarWidget(data: Map<String, Float>, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, FieldGreen.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Feeding Continuity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                (0..6).forEach { i ->
                    val date = Calendar.getInstance().apply { add(Calendar.DATE, -i) }.time
                    val key = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
                    val score = data[key] ?: 0f
                    val dayLabel = SimpleDateFormat("E", Locale.US).format(date).take(1)
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = dayLabel, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(FieldGreen.copy(alpha = if (score > 0.8f) 0.8f else if (score > 0.3f) 0.3f else 0.05f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (score > 0.8f) Text("✓", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CowSprite(name: String, avatarStyle: Int, modifier: Modifier = Modifier) {
    val color = when (avatarStyle % 4) {
        0 -> FieldGreen
        1 -> Sky
        2 -> Turmeric
        else -> Clay
    }
    Box(modifier = modifier.clip(CircleShape).background(color.copy(alpha = 0.1f)).border(2.dp, color, CircleShape), contentAlignment = Alignment.Center) {
        Text(text = name.take(1).uppercase(), style = MaterialTheme.typography.headlineLarge, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ReminderBanner(title: String, message: String, color: Color, onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(color), contentAlignment = Alignment.Center) {
                Text("🔔", color = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
                Text(text = message, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDismiss) {
                Text("✕", color = color, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun NutrientMeter(label: String, value: Float, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
            CircularProgressIndicator(
                progress = { value },
                modifier = Modifier.fillMaxSize(),
                color = color,
                strokeWidth = 8.dp,
                trackColor = color.copy(alpha = 0.1f)
            )
            Text(text = "${(value * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun FodderCard(name: String, category: String, visualHint: String, benefit: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val color = if (selected) FieldGreen else Color.Gray
    OutlinedCard(
        onClick = onClick,
        modifier = modifier.width(160.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(if (selected) 2.5.dp else 1.dp, color.copy(alpha = if (selected) 0.8f else 0.2f)),
        colors = CardDefaults.outlinedCardColors(containerColor = if (selected) FieldGreen.copy(alpha = 0.04f) else Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = visualHint, style = androidx.compose.ui.text.TextStyle(fontSize = 32.sp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = category, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = benefit, style = MaterialTheme.typography.bodySmall, maxLines = 2, color = Color.DarkGray)
        }
    }
}

@Composable
fun GrainMixAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(4000, easing = LinearEasing), repeatMode = RepeatMode.Restart)
    )
    
    Box(modifier = modifier.size(180.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(Color.LightGray.copy(alpha = 0.2f), style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)))
            repeat(8) { i ->
                val angle = (i * 45f + rotation) * (Math.PI / 180f).toFloat()
                val radius = 60.dp.toPx()
                val x = center.x + radius * Math.cos(angle.toDouble()).toFloat()
                val y = center.y + radius * Math.sin(angle.toDouble()).toFloat()
                drawCircle(if (i % 2 == 0) FieldGreen else Turmeric, radius = 6.dp.toPx(), center = Offset(x, y))
            }
        }
        Text("🥣", style = androidx.compose.ui.text.TextStyle(fontSize = 64.sp))
    }
}

@Composable
fun LivelyCowIllustration(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val headTilt by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(animation = tween(2500, easing = LinearOutSlowInEasing), repeatMode = RepeatMode.Reverse)
    )
    
    Canvas(modifier = modifier) {
        val bodyColor = Color.White
        val spotColor = Color(0xFF2D2D2D)
        val earColor = Color(0xFFFFCDD2)
        
        // Body (Softer, rounder)
        drawOval(bodyColor, topLeft = Offset(size.width * 0.15f, size.height * 0.35f), size = Size(size.width * 0.7f, size.height * 0.5f))
        
        // Cuter Spots
        drawCircle(spotColor, radius = size.minDimension * 0.08f, center = Offset(size.width * 0.45f, size.height * 0.55f))
        drawCircle(spotColor, radius = size.minDimension * 0.06f, center = Offset(size.width * 0.75f, size.height * 0.6f))
        drawCircle(spotColor, radius = size.minDimension * 0.04f, center = Offset(size.width * 0.65f, size.height * 0.45f))
        
        // Head with tilt
        withTransform({
            rotate(headTilt, pivot = Offset(size.width * 0.4f, size.height * 0.5f))
        }) {
            // Ears
            drawOval(earColor, topLeft = Offset(size.width * 0.2f, size.height * 0.22f), size = Size(size.width * 0.12f, size.width * 0.18f))
            drawOval(earColor, topLeft = Offset(size.width * 0.68f, size.height * 0.22f), size = Size(size.width * 0.12f, size.width * 0.18f))
            
            // Face
            drawOval(bodyColor, topLeft = Offset(size.width * 0.25f, size.height * 0.2f), size = Size(size.width * 0.5f, size.height * 0.45f))
            
            // Big cute eyes
            drawCircle(Color.Black, radius = size.minDimension * 0.025f, center = Offset(size.width * 0.4f, size.height * 0.38f))
            drawCircle(Color.Black, radius = size.minDimension * 0.025f, center = Offset(size.width * 0.6f, size.height * 0.38f))
            
            // Muzzle
            drawOval(earColor.copy(alpha = 0.8f), topLeft = Offset(size.width * 0.35f, size.height * 0.45f), size = Size(size.width * 0.3f, size.height * 0.18f))
            // Nostrils
            drawCircle(Color.Gray.copy(alpha = 0.4f), radius = 2.dp.toPx(), center = Offset(size.width * 0.44f, size.height * 0.54f))
            drawCircle(Color.Gray.copy(alpha = 0.4f), radius = 2.dp.toPx(), center = Offset(size.width * 0.56f, size.height * 0.54f))
        }
        
        // Stumpy cute legs
        val legW = size.width * 0.08f
        val legH = size.height * 0.15f
        drawRoundRect(bodyColor, topLeft = Offset(size.width * 0.3f, size.height * 0.78f), size = Size(legW, legH), cornerRadius = CornerRadius(4.dp.toPx()))
        drawRoundRect(bodyColor, topLeft = Offset(size.width * 0.62f, size.height * 0.78f), size = Size(legW, legH), cornerRadius = CornerRadius(4.dp.toPx()))
    }
}

@Composable
fun FarmBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(Brush.verticalGradient(listOf(Sky.copy(alpha = 0.1f), Color.Transparent)))
        val path = Path().apply {
            moveTo(0f, size.height)
            quadraticBezierTo(size.width * 0.25f, size.height * 0.85f, size.width * 0.5f, size.height * 0.92f)
            quadraticBezierTo(size.width * 0.75f, size.height * 0.98f, size.width, size.height * 0.88f)
            lineTo(size.width, size.height)
            close()
        }
        drawPath(path, FieldGreen.copy(alpha = 0.05f))
    }
}

@Composable
fun CowAvatar(name: String, avatarStyle: Int, modifier: Modifier = Modifier, selected: Boolean = false) {
    val accent = when (avatarStyle % 4) {
        0 -> FieldGreen
        1 -> Sky
        2 -> Turmeric
        else -> Clay
    }
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(if (selected) accent.copy(alpha = 0.15f) else Color.White)
            .border(if (selected) 3.dp else 1.dp, if (selected) accent else Color.LightGray.copy(alpha = 0.4f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(if (selected) 48.dp else 42.dp)) {
                LivelyCowIllustration(modifier = Modifier.fillMaxSize())
            }
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                color = accent,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
fun BarnHeaderBackground(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().height(120.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Soft gradient base
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(FieldGreen.copy(alpha = 0.12f), Color.Transparent)
                )
            )
            
            // Barn silhouette in the background
            val barnColor = FieldGreen.copy(alpha = 0.08f)
            val barnScale = 0.9f
            val barnW = 140.dp.toPx() * barnScale
            val barnH = 90.dp.toPx() * barnScale
            val bx = size.width - barnW - 40.dp.toPx()
            val by = size.height - barnH
            
            drawRect(barnColor, Offset(bx, by + 28.dp.toPx()), Size(barnW, barnH - 28.dp.toPx()))
            val roof = Path().apply {
                moveTo(bx - 18.dp.toPx(), by + 28.dp.toPx())
                lineTo(bx + barnW / 2, by)
                lineTo(bx + barnW + 18.dp.toPx(), by + 28.dp.toPx())
                close()
            }
            drawPath(roof, barnColor)
            
            // Subtle "planks" on the barn
            repeat(4) { i ->
                val px = bx + (barnW / 5) * (i + 1)
                drawLine(
                    color = Color.White.copy(alpha = 0.2f),
                    start = Offset(px, by + 28.dp.toPx()),
                    end = Offset(px, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    }
}

@Composable
fun SavingsHistoryChart(
    dailySavings: List<Double>,
    isCumulative: Boolean,
    modifier: Modifier = Modifier
) {
    val labels = remember {
        val dateFormat = SimpleDateFormat("EEE", Locale.US)
        val calendar = Calendar.getInstance()
        (0..6).map { 
            val label = dateFormat.format(calendar.time)
            calendar.add(Calendar.DATE, -1)
            label
        }.reversed()
    }

    val displayData = remember(dailySavings, isCumulative) {
        if (isCumulative) {
            dailySavings.runningFold(0.0) { acc, d -> acc + d }.drop(1)
        } else {
            dailySavings
        }
    }

    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setScaleEnabled(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                setDrawBarShadow(false)
                
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(labels)
                    textColor = FieldGreen.toArgb()
                }
                
                axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = Color.LightGray.toArgb()
                    textColor = Color.Gray.toArgb()
                    axisMinimum = 0f
                }
                
                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            val entries = displayData.mapIndexed { index, value ->
                BarEntry(index.toFloat(), value.toFloat())
            }
            
            val dataSet = BarDataSet(entries, if (isCumulative) "Cumulative Savings" else "Daily Savings").apply {
                color = FieldGreen.toArgb()
                setDrawValues(false)
                
                // Note: Gradient requires a chart instance or a bitmap, 
                // for simplicity we use the color here.
            }
            
            chart.data = BarData(dataSet).apply {
                barWidth = 0.6f
            }
            chart.animateY(800)
            chart.invalidate()
        },
        modifier = modifier.fillMaxWidth().height(200.dp)
    )
}
