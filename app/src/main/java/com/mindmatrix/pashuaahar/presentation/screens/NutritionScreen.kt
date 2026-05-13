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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mindmatrix.pashuaahar.presentation.PashuAaharUiState
import com.mindmatrix.pashuaahar.presentation.components.*
import com.mindmatrix.pashuaahar.presentation.theme.BeautifulCard
import com.mindmatrix.pashuaahar.presentation.theme.Cream
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.GradientBackground
import com.mindmatrix.pashuaahar.presentation.theme.Meadow
import com.mindmatrix.pashuaahar.presentation.theme.Sky
import com.mindmatrix.pashuaahar.presentation.theme.SlideInContent
import com.mindmatrix.pashuaahar.presentation.theme.SurfaceMuted
import kotlinx.coroutines.delay

@Composable
fun NutritionScreen(
    state: PashuAaharUiState,
    onCowSelected: (String) -> Unit,
    onToggleFeed: (String, String) -> Unit,
    onModeChange: (Boolean) -> Unit,
    onGrazingChange: (Float) -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    val cowsFedToday = state.cowProfiles.count { cow ->
        val completion = state.feedCompletionStatus[cow.id].orEmpty()
        completion.size >= state.feedPlan.lines.size
    }

    GradientBackground {
        SlideInContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                ScreenHeader(title = "Daily Plan", subtitle = "Guided nutrition for ${state.cowProfile.name}")
                Spacer(modifier = Modifier.height(16.dp))

                // Herd Progress Summary
                BeautifulCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(48.dp).clip(CircleShape).background(FieldGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🐄", style = MaterialTheme.typography.titleLarge)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Herd Feeding Progress", style = MaterialTheme.typography.labelLarge, color = FieldGreen, fontWeight = FontWeight.Bold)
                            Text(text = "$cowsFedToday / ${state.cowProfiles.size} cows fully fed today", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Cow Selector
                AnimatedVisibility(visible = visible, enter = fadeIn() + slideInHorizontally()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.cowProfiles) { cow ->
                            CowAvatar(
                                name = cow.name,
                                avatarStyle = cow.avatarStyle,
                                selected = cow.id == state.selectedCowId,
                                modifier = Modifier
                                    .size(84.dp)
                                    .clickable { onCowSelected(cow.id) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Mode Selector
                BeautifulCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onModeChange(false) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!state.isSuperMixMode) FieldGreen else Color.Transparent,
                                contentColor = if (!state.isSuperMixMode) Color.White else Color.Gray
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Standard")
                        }
                        Button(
                            onClick = { onModeChange(true) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.isSuperMixMode) Sky else Color.Transparent,
                                contentColor = if (state.isSuperMixMode) Color.White else Color.Gray
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Super-Mix")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Grazing Selector
                AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically { 50 }) {
                    BeautifulCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Grazing Time", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                    Text(text = "Natural energy source", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                                Text(text = "${state.grazingHours.toInt()} Hours", style = MaterialTheme.typography.headlineSmall, color = FieldGreen, fontWeight = FontWeight.ExtraBold)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                GrazingSceneIllustration(grazingHours = state.grazingHours)
                            }
                            Slider(
                                value = state.grazingHours,
                                onValueChange = { onGrazingChange(it) },
                                valueRange = 0f..8f,
                                steps = 7,
                                colors = SliderDefaults.colors(thumbColor = FieldGreen, activeTrackColor = FieldGreen)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Ration View
                Text(text = if (state.isSuperMixMode) "Custom Mix Details" else "Standard Ration", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(12.dp))
                
                val displayPlan = state.feedPlan
                
                displayPlan.lines.forEachIndexed { index, line ->
                    var itemVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(visible) {
                        if (visible) {
                            delay(100L * index)
                            itemVisible = true
                        }
                    }
                    AnimatedVisibility(visible = itemVisible, enter = fadeIn() + slideInVertically { 20 }) {
                        val isDone = line.ingredient.id in state.feedCompletionStatus[state.cowProfile.id].orEmpty()
                        FeedLineCard(
                            line = line,
                            isDone = isDone,
                            onToggle = { onToggleFeed(state.cowProfile.id, line.ingredient.id) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Hydration Plan
                BeautifulCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        WaterTroughIllustration(isFull = true)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Hydration Plan", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Sky)
                            Text(text = "Provide 35-40 Litres of clean water daily for max milk yield.", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun FeedLineCard(
    line: com.mindmatrix.pashuaahar.domain.FeedLine,
    isDone: Boolean,
    onToggle: () -> Unit
) {
    BeautifulCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle,
        border = BorderStroke(if (isDone) 2.dp else 1.dp, if (isDone) FieldGreen else SurfaceMuted)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(60.dp), contentAlignment = Alignment.Center) {
                when {
                    line.ingredient.category == "Energy Grasses" -> TallGrassIllustration()
                    line.ingredient.category == "Milk Builders" -> LegumeIllustration()
                    line.ingredient.category == "Market Staples" -> {
                        when {
                            line.ingredient.name.contains("Bran") -> BranIllustration()
                            line.ingredient.name.contains("Cake") -> CakeIllustration()
                            line.ingredient.name.contains("Pellet") -> PelletIllustration()
                            else -> Text("📦", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                    line.ingredient.id == "mineral_mix" -> Text("⚪", style = MaterialTheme.typography.headlineMedium)
                    else -> MixBucketMiniIllustration()
                }
                if (isDone) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(FieldGreen.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✓", color = FieldGreen, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = line.ingredient.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (isDone) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )
                Text(
                    text = "${line.quantityKg} kg",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (isDone) Color.Gray else FieldGreen
                )
            }
            Checkbox(
                checked = isDone,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = FieldGreen)
            )
        }
    }
}
