package com.mindmatrix.pashuaahar.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import com.mindmatrix.pashuaahar.domain.CowProfile
import com.mindmatrix.pashuaahar.presentation.components.ConcentrateIllustration
import com.mindmatrix.pashuaahar.presentation.components.DryFodderIllustration
import com.mindmatrix.pashuaahar.presentation.components.HydroponicIllustration
import com.mindmatrix.pashuaahar.presentation.components.LegumeIllustration
import com.mindmatrix.pashuaahar.presentation.components.SuperFeedIllustration
import com.mindmatrix.pashuaahar.presentation.components.TallGrassIllustration
import com.mindmatrix.pashuaahar.presentation.components.TreeFodderIllustration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindmatrix.pashuaahar.presentation.theme.Clay
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.Cream
import com.mindmatrix.pashuaahar.presentation.theme.LeafGreen
import com.mindmatrix.pashuaahar.presentation.theme.Meadow
import com.mindmatrix.pashuaahar.presentation.theme.Sky
import com.mindmatrix.pashuaahar.presentation.theme.Soil
import com.mindmatrix.pashuaahar.presentation.theme.SurfaceMuted
import com.mindmatrix.pashuaahar.presentation.theme.Turmeric

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
    color: Color = LeafGreen
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, color.copy(alpha = 0.22f), RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title.take(1),
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = detail, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun VisualBadge(label: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun HealthMeter(score: Float, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(180.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(180.dp)) {
                val stroke = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
                drawArc(
                    color = SurfaceMuted,
                    startAngle = 145f,
                    sweepAngle = 250f,
                    useCenter = false,
                    style = stroke
                )
                drawArc(
                    brush = Brush.sweepGradient(listOf(Clay, Turmeric, FieldGreen, Sky)),
                    startAngle = 145f,
                    sweepAngle = ((score - 1f) / 4f).coerceIn(0f, 1f) * 250f,
                    useCenter = false,
                    style = stroke
                )
                drawCircle(
                    color = FieldGreen,
                    radius = 6.dp.toPx(),
                    center = Offset(size.width / 2f, size.height / 2f)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = score.toString(), style = MaterialTheme.typography.headlineLarge)
                Text(text = "BCS", style = MaterialTheme.typography.titleLarge)
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PregnancyProgress(month: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Pregnancy", style = MaterialTheme.typography.titleLarge)
            Text(text = "$month / 9 months", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { (month / 9f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = Turmeric,
            trackColor = SurfaceMuted
        )
    }
}

enum class NavGlyph {
    Home,
    Cow,
    Leaf,
    Shield
}

@Composable
fun NavIcon(glyph: NavGlyph, selected: Boolean, modifier: Modifier = Modifier) {
    val color = if (selected) FieldGreen else FieldGreen.copy(alpha = 0.55f)
    Canvas(modifier = modifier.size(28.dp)) {
        val stroke = Stroke(width = 2.6.dp.toPx(), cap = StrokeCap.Round)
        when (glyph) {
            NavGlyph.Home -> {
                drawLine(color, Offset(size.width * 0.18f, size.height * 0.48f), Offset(size.width * 0.5f, size.height * 0.18f), strokeWidth = stroke.width, cap = StrokeCap.Round)
                drawLine(color, Offset(size.width * 0.5f, size.height * 0.18f), Offset(size.width * 0.82f, size.height * 0.48f), strokeWidth = stroke.width, cap = StrokeCap.Round)
                drawRoundRect(color.copy(alpha = 0.2f), topLeft = Offset(size.width * 0.28f, size.height * 0.45f), size = Size(size.width * 0.44f, size.height * 0.36f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(5.dp.toPx()))
                drawRoundRect(color, topLeft = Offset(size.width * 0.42f, size.height * 0.58f), size = Size(size.width * 0.16f, size.height * 0.23f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx()))
            }
            NavGlyph.Cow -> {
                drawOval(color.copy(alpha = 0.18f), topLeft = Offset(size.width * 0.18f, size.height * 0.3f), size = Size(size.width * 0.64f, size.height * 0.44f))
                drawCircle(color, radius = 2.2.dp.toPx(), center = Offset(size.width * 0.4f, size.height * 0.48f))
                drawCircle(color, radius = 2.2.dp.toPx(), center = Offset(size.width * 0.6f, size.height * 0.48f))
                drawLine(color, Offset(size.width * 0.28f, size.height * 0.28f), Offset(size.width * 0.12f, size.height * 0.18f), strokeWidth = stroke.width, cap = StrokeCap.Round)
                drawLine(color, Offset(size.width * 0.72f, size.height * 0.28f), Offset(size.width * 0.88f, size.height * 0.18f), strokeWidth = stroke.width, cap = StrokeCap.Round)
            }
            NavGlyph.Leaf -> {
                drawOval(color.copy(alpha = 0.22f), topLeft = Offset(size.width * 0.18f, size.height * 0.14f), size = Size(size.width * 0.54f, size.height * 0.62f))
                drawLine(color, Offset(size.width * 0.3f, size.height * 0.72f), Offset(size.width * 0.82f, size.height * 0.22f), strokeWidth = stroke.width, cap = StrokeCap.Round)
            }
            NavGlyph.Shield -> {
                drawRoundRect(color.copy(alpha = 0.18f), topLeft = Offset(size.width * 0.22f, size.height * 0.12f), size = Size(size.width * 0.56f, size.height * 0.68f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx()))
                drawLine(color, Offset(size.width * 0.5f, size.height * 0.3f), Offset(size.width * 0.5f, size.height * 0.64f), strokeWidth = stroke.width, cap = StrokeCap.Round)
                drawLine(color, Offset(size.width * 0.34f, size.height * 0.47f), Offset(size.width * 0.66f, size.height * 0.47f), strokeWidth = stroke.width, cap = StrokeCap.Round)
            }
        }
    }
}

@Composable
fun SavingsBarChart(
    marketCost: Float,
    homeCost: Float,
    savings: Float,
    modifier: Modifier = Modifier
) {
    val maxVal = maxOf(marketCost, homeCost)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Bar(
                label = "Market",
                value = marketCost,
                maxValue = maxVal,
                color = Soil.copy(alpha = 0.6f),
                modifier = Modifier.weight(1f)
            )
            Bar(
                label = "Home-made",
                value = homeCost,
                maxValue = maxVal,
                color = FieldGreen,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(FieldGreen.copy(alpha = 0.1f))
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Total Daily Savings: Rs $savings",
                style = MaterialTheme.typography.titleLarge,
                color = FieldGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun Bar(
    label: String,
    value: Float,
    maxValue: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    val heightFactor = if (maxValue > 0) value / maxValue else 0f
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = "Rs ${value.toInt()}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(140.dp * heightFactor)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BarnInteriorIllustration(
    modifier: Modifier = Modifier, 
    cows: List<CowProfile>,
    onCowClick: (String) -> Unit = {}
) {
    Box(modifier = modifier.fillMaxWidth().height(320.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Outdoor Paddock Area
            drawRect(Meadow.copy(alpha = 0.3f), size = size)
            // Circular Fence/Path
            drawCircle(
                color = Soil.copy(alpha = 0.2f),
                radius = size.minDimension * 0.4f,
                style = Stroke(width = 20.dp.toPx())
            )
        }
        
        // Cows arranged in a circle
        val center = Offset(LocalDensity.current.run { 180.dp.toPx() }, LocalDensity.current.run { 160.dp.toPx() })
        val radius = LocalDensity.current.run { 100.dp.toPx() }
        
        Box(modifier = Modifier.fillMaxSize()) {
            cows.forEachIndexed { index, cow ->
                val angle = (index.toFloat() / cows.size) * 2 * Math.PI
                val x = (center.x + radius * Math.cos(angle)).toFloat()
                val y = (center.y + radius * Math.sin(angle)).toFloat()
                
                val density = LocalDensity.current
                CowSprite(
                    name = cow.name, 
                    style = cow.avatarStyle, 
                    modifier = Modifier
                        .size(80.dp)
                        .offset(
                            x = density.run { (x - 40.dp.toPx()).toDp() },
                            y = density.run { (y - 40.dp.toPx()).toDp() }
                        )
                        .clickable { onCowClick(cow.id) }
                )
            }
        }
    }
}

@Composable
fun ScientificCalendarWidget(
    history: Map<String, Float>,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, FieldGreen.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "Feeding History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                // Simplified 7-day view for the widget
                val today = java.util.Calendar.getInstance()
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                
                for (i in -6..0) {
                    val cal = java.util.Calendar.getInstance().apply { add(java.util.Calendar.DAY_OF_YEAR, i) }
                    val dateKey = sdf.format(cal.time)
                    val score = history[dateKey] ?: 0f
                    val dayName = java.text.SimpleDateFormat("EEE", java.util.Locale.US).format(cal.time)
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = dayName.take(1), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    if (score >= 0.8f) FieldGreen 
                                    else if (score > 0.4f) Turmeric 
                                    else SurfaceMuted
                                )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(text = "Scientific consistency improves milk fat.", style = MaterialTheme.typography.bodySmall, color = FieldGreen)
        }
    }
}

@Composable
fun CowSprite(name: String, style: Int, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "sprite")
    val hop by transition.animateFloat(
        initialValue = 0f, targetValue = -5f,
        animationSpec = infiniteRepeatable(tween(500, delayMillis = (Math.random() * 1000).toInt()), RepeatMode.Reverse), label = "hop"
    )

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(50.dp).offset(y = hop.dp)) {
            CowAvatar(name = name, avatarStyle = style, modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.labelSmall, color = Color.White)
        }
    }
}

@Composable
fun ReminderBanner(
    title: String,
    message: String,
    color: Color,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, color.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔔", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
                Text(text = message, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            IconButton(onClick = onDismiss) {
                Text(text = "✕", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            }
        }
    }
}

@Composable
fun NutrientMeter(
    label: String,
    value: Float, // 0f to 1f
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(80.dp)) {
                val stroke = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                drawArc(
                    color = SurfaceMuted,
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = stroke
                )
                drawArc(
                    color = color,
                    startAngle = 135f,
                    sweepAngle = value * 270f,
                    useCenter = false,
                    style = stroke
                )
            }
            Text(
                text = "${(value * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun FodderCard(
    name: String,
    category: String,
    visualHint: String,
    benefit: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "fodder")
    val pulse by transition.animateFloat(
        initialValue = 1f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "pulse"
    )

    OutlinedCard(
        modifier = modifier
            .width(180.dp)
            .height(260.dp)
            .clickable(onClick = onClick)
            .scale(if (selected) pulse else 1f),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(if (selected) 3.dp else 1.dp, if (selected) FieldGreen else SurfaceMuted),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                when {
                    name.contains("Napier") || name.contains("Grass") -> TallGrassIllustration()
                    name.contains("Straw") || name.contains("Bhusa") -> DryFodderIllustration()
                    category == "Milk Builders" -> LegumeIllustration()
                    category == "Nutrient Boosters" -> TreeFodderIllustration()
                    category == "Modern Supplements" -> {
                        if (name.contains("Hydro")) HydroponicIllustration() else SuperFeedIllustration()
                    }
                    category == "Concentrate" -> ConcentrateIllustration()
                    else -> Text(text = visualHint.take(2), style = MaterialTheme.typography.displaySmall)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            VisualBadge(label = category, color = if (category == "Super-Fodder") Sky else Soil)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = benefit,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = FieldGreen,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun GrainMixAnimation(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "mix")
    val dotCount = 12
    val dotAnimations = List(dotCount) { i ->
        transition.animateFloat(
            initialValue = 0f, targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1000, delayMillis = i * 100)), label = "dot_$i"
        )
    }

    Canvas(modifier = modifier.size(120.dp)) {
        val bucketWidth = size.width * 0.6f
        val bucketHeight = size.height * 0.4f
        val bucketX = (size.width - bucketWidth) / 2f
        val bucketY = size.height - bucketHeight - 10.dp.toPx()

        // Bucket
        drawRect(
            color = SurfaceMuted,
            topLeft = Offset(bucketX, bucketY),
            size = Size(bucketWidth, bucketHeight)
        )
        
        // Falling grains
        dotAnimations.forEachIndexed { i, anim ->
            val x = bucketX + (bucketWidth / dotCount) * i
            val y = anim.value * (bucketY + bucketHeight * 0.5f)
            if (y < bucketY + bucketHeight) {
                drawCircle(
                    color = if (i % 2 == 0) Turmeric else Soil,
                    radius = 3.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }
    }
}

@Composable
fun LivelyCowIllustration(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "cow")
    val breath by transition.animateFloat(
        initialValue = 1f, targetValue = 1.03f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse), label = "breath"
    )
    val blink by transition.animateFloat(
        initialValue = 1f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(150, delayMillis = 3000), RepeatMode.Reverse), label = "blink"
    )

    Canvas(modifier = modifier.size(240.dp)) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        
        // Body
        drawOval(
            color = Color.White,
            topLeft = Offset(centerX - 80.dp.toPx() * breath, centerY - 40.dp.toPx()),
            size = Size(160.dp.toPx() * breath, 100.dp.toPx())
        )
        // Spots
        drawCircle(Soil.copy(alpha = 0.2f), radius = 20.dp.toPx(), center = Offset(centerX - 30.dp.toPx(), centerY + 10.dp.toPx()))
        drawCircle(Soil.copy(alpha = 0.15f), radius = 15.dp.toPx(), center = Offset(centerX + 40.dp.toPx(), centerY - 5.dp.toPx()))

        // Head
        drawOval(
            color = Color.White,
            topLeft = Offset(centerX - 45.dp.toPx(), centerY - 90.dp.toPx()),
            size = Size(90.dp.toPx(), 75.dp.toPx())
        )
        // Muzzle
        drawOval(
            color = Clay.copy(alpha = 0.2f),
            topLeft = Offset(centerX - 35.dp.toPx(), centerY - 50.dp.toPx()),
            size = Size(70.dp.toPx(), 45.dp.toPx())
        )
        
        // Eyes
        val eyeY = centerY - 65.dp.toPx()
        drawCircle(Color.Black, radius = 4.dp.toPx() * blink, center = Offset(centerX - 20.dp.toPx(), eyeY))
        drawCircle(Color.Black, radius = 4.dp.toPx() * blink, center = Offset(centerX + 20.dp.toPx(), eyeY))
        
        // Ears
        drawOval(Color.White, topLeft = Offset(centerX - 55.dp.toPx(), centerY - 95.dp.toPx()), size = Size(25.dp.toPx(), 15.dp.toPx()))
        drawOval(Color.White, topLeft = Offset(centerX + 30.dp.toPx(), centerY - 95.dp.toPx()), size = Size(25.dp.toPx(), 15.dp.toPx()))
    }
}

@Composable
fun FarmBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxWidth().height(200.dp)) {
        // Hills
        drawOval(
            color = Meadow.copy(alpha = 0.4f),
            topLeft = Offset(-100.dp.toPx(), size.height - 120.dp.toPx()),
            size = Size(size.width + 200.dp.toPx(), 240.dp.toPx())
        )
        drawOval(
            color = FieldGreen.copy(alpha = 0.15f),
            topLeft = Offset(-50.dp.toPx(), size.height - 80.dp.toPx()),
            size = Size(size.width + 100.dp.toPx(), 180.dp.toPx())
        )
    }
}

@Composable
fun CowAvatar(
    name: String,
    avatarStyle: Int,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    val palette = listOf(FieldGreen, Turmeric, Sky, Clay, LeafGreen, Soil)
    val accent = palette[((avatarStyle % palette.size) + palette.size) % palette.size]
    
    val transition = rememberInfiniteTransition(label = "avatar")
    val breath by transition.animateFloat(
        initialValue = 1f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(2500), RepeatMode.Reverse), label = "breath"
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(accent.copy(alpha = 0.16f))
            .border(if (selected) 3.dp else 1.dp, accent.copy(alpha = if (selected) 0.9f else 0.28f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(82.dp * if (selected) breath else 1f)) {
            drawOval(
                color = Cream,
                topLeft = Offset(size.width * 0.18f, size.height * 0.26f),
                size = Size(size.width * 0.64f, size.height * 0.52f)
            )
            drawCircle(
                color = accent.copy(alpha = 0.24f),
                radius = size.minDimension * 0.13f,
                center = Offset(size.width * 0.34f, size.height * 0.42f)
            )
            drawCircle(
                color = accent.copy(alpha = 0.18f),
                radius = size.minDimension * 0.1f,
                center = Offset(size.width * 0.62f, size.height * 0.58f)
            )
            drawLine(accent, Offset(size.width * 0.3f, size.height * 0.3f), Offset(size.width * 0.12f, size.height * 0.14f), strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)
            drawLine(accent, Offset(size.width * 0.7f, size.height * 0.3f), Offset(size.width * 0.88f, size.height * 0.14f), strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)
            drawCircle(Color(0xFF263126), radius = 3.dp.toPx(), center = Offset(size.width * 0.42f, size.height * 0.49f))
            drawCircle(Color(0xFF263126), radius = 3.dp.toPx(), center = Offset(size.width * 0.58f, size.height * 0.49f))
            drawOval(accent.copy(alpha = 0.35f), topLeft = Offset(size.width * 0.38f, size.height * 0.59f), size = Size(size.width * 0.24f, size.height * 0.12f))
        }
        Text(
            text = name.take(1).uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = accent,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp)
        )
    }
}
