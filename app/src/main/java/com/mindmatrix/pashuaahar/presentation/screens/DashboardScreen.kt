package com.mindmatrix.pashuaahar.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.mindmatrix.pashuaahar.domain.DailyCareActivity
import com.mindmatrix.pashuaahar.presentation.PashuAaharUiState
import androidx.compose.material3.OutlinedTextField
import com.mindmatrix.pashuaahar.presentation.components.BarnInteriorIllustration
import com.mindmatrix.pashuaahar.presentation.components.ReminderBanner
import com.mindmatrix.pashuaahar.presentation.components.ScientificCalendarWidget
import com.mindmatrix.pashuaahar.presentation.components.ScreenHeader
import com.mindmatrix.pashuaahar.presentation.theme.BeautifulCard
import com.mindmatrix.pashuaahar.presentation.theme.Cream
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.GradientBackground
import com.mindmatrix.pashuaahar.presentation.theme.LeafGreen
import com.mindmatrix.pashuaahar.presentation.theme.Meadow
import com.mindmatrix.pashuaahar.presentation.theme.ShimmerBox
import com.mindmatrix.pashuaahar.presentation.theme.Sky
import com.mindmatrix.pashuaahar.presentation.theme.SlideInContent
import com.mindmatrix.pashuaahar.presentation.theme.SurfaceMuted
import com.mindmatrix.pashuaahar.presentation.theme.Turmeric

@Composable
fun DashboardScreen(
    state: PashuAaharUiState,
    onCowSelected: (String) -> Unit,
    onDailyCareToggled: (String, DailyCareActivity) -> Unit,
    onDismissReminder: (String) -> Unit,
    onFarmHeaderClick: () -> Unit
) {
    val selectedCow = state.cowProfile
    var visible by remember { mutableStateOf(false) }
    var yieldInput by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    val reminders = listOf(
        Triple("fmd", "FMD Vaccination", "Your herd is due for FMD vaccine in 12 days. Contact your local vet."),
        Triple("grant", "Dairy Grant 2024", "The government is offering grants for scientific fodder pits. Apply now."),
        Triple("loan", "Kisan Loan", "Low-interest loans available forJersey/Desi breed expansion.")
    ).filter { it.first !in state.dismissedReminderIds }

    GradientBackground {
        SlideInContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Profile & Farm Summary Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FieldGreen)
                        .clickable { onFarmHeaderClick() }
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(54.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                            Text("🏡", style = MaterialTheme.typography.headlineSmall)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = state.userProfile.farmName, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(text = "${state.userProfile.farmerName} · ${state.userProfile.village}", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                }

                // Top Barn Section
                Box(modifier = Modifier.fillMaxWidth().height(320.dp), contentAlignment = Alignment.Center) {
                    BarnInteriorIllustration(cows = state.cowProfiles, onCowClick = onCowSelected)
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    if (state.isLoading) {
                        repeat(3) {
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .padding(bottom = 12.dp)
                            )
                        }
                    }

                    // Reminders Section
                    if (reminders.isNotEmpty() && !state.isLoading) {
                        reminders.forEachIndexed { index, reminder ->
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn() + slideInVertically { -20 * (index + 1) }
                            ) {
                                ReminderBanner(
                                    title = reminder.second,
                                    message = reminder.third,
                                    color = if (reminder.first == "fmd") Turmeric else if (reminder.first == "grant") FieldGreen else Sky,
                                    onDismiss = { onDismissReminder(reminder.first) }
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Advanced Feeding Calendar
                    AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically { 50 }) {
                        ScientificCalendarWidget(history = state.feedingHistory)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Dairy Tools Grid
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        BeautifulCard(
                            modifier = Modifier.weight(1f)
                        ) {
                            Column {
                                Text(text = "🥛 Yield Calc", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = yieldInput,
                                    onValueChange = { yieldInput = it },
                                    placeholder = { Text("Litres", style = MaterialTheme.typography.bodySmall) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                val projected = (yieldInput.toFloatOrNull() ?: 0f) * 45f
                                Text(text = "≈ Rs ${projected.toInt()} / Day", style = MaterialTheme.typography.bodyLarge, color = FieldGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                        BeautifulCard(
                            modifier = Modifier.weight(1f)
                        ) {
                            Column {
                                Text(text = "📈 Growth", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Healthy Herd", color = FieldGreen, fontWeight = FontWeight.Bold)
                                Text(text = "Keep mixing Super-Feed!", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrackerScore(done: Int, total: Int) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(Meadow),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$done/$total", style = MaterialTheme.typography.titleLarge, color = FieldGreen, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun TrackerTile(
    activity: DailyCareActivity,
    checked: Boolean,
    onClick: () -> Unit
) {
    val accent = when (activity) {
        DailyCareActivity.MorningFeed -> Turmeric
        DailyCareActivity.Hydration -> Sky
        DailyCareActivity.Grazing -> LeafGreen
        DailyCareActivity.EveningNightFeed -> FieldGreen
    }
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(106.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(26.dp),
        border = BorderStroke(2.dp, if (checked) FieldGreen else accent.copy(alpha = 0.32f)),
        colors = CardDefaults.outlinedCardColors(containerColor = if (checked) FieldGreen.copy(alpha = 0.12f) else SurfaceMuted.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ActivityIcon(activity = activity, color = if (checked) FieldGreen else accent)
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = activity.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            }
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (checked) FieldGreen else Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = if (checked) "✓" else "", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ActivityIcon(activity: DailyCareActivity, color: Color) {
    Canvas(modifier = Modifier.size(58.dp)) {
        val stroke = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        when (activity) {
            DailyCareActivity.MorningFeed -> {
                drawCircle(color.copy(alpha = 0.22f), radius = size.minDimension * 0.45f)
                drawCircle(color, radius = size.minDimension * 0.16f, center = Offset(size.width * 0.34f, size.height * 0.34f))
                drawRoundRect(color.copy(alpha = 0.55f), topLeft = Offset(size.width * 0.28f, size.height * 0.62f), size = Size(size.width * 0.44f, size.height * 0.18f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx()))
            }
            DailyCareActivity.Hydration -> {
                drawCircle(color.copy(alpha = 0.18f), radius = size.minDimension * 0.45f)
                drawOval(color, topLeft = Offset(size.width * 0.34f, size.height * 0.18f), size = Size(size.width * 0.32f, size.height * 0.56f))
            }
            DailyCareActivity.Grazing -> {
                drawCircle(color.copy(alpha = 0.18f), radius = size.minDimension * 0.45f)
                repeat(4) { index ->
                    val x = size.width * (0.25f + index * 0.14f)
                    drawLine(color, Offset(x, size.height * 0.72f), Offset(x + size.width * 0.08f, size.height * 0.28f), strokeWidth = stroke.width, cap = StrokeCap.Round)
                }
            }
            DailyCareActivity.EveningNightFeed -> {
                drawCircle(color.copy(alpha = 0.18f), radius = size.minDimension * 0.45f)
                drawCircle(color, radius = size.minDimension * 0.22f, center = Offset(size.width * 0.42f, size.height * 0.38f))
                drawCircle(Color.White, radius = size.minDimension * 0.2f, center = Offset(size.width * 0.52f, size.height * 0.33f))
                drawRoundRect(color.copy(alpha = 0.55f), topLeft = Offset(size.width * 0.28f, size.height * 0.66f), size = Size(size.width * 0.44f, size.height * 0.14f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx()))
            }
        }
    }
}
