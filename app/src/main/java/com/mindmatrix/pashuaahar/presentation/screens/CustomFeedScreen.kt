package com.mindmatrix.pashuaahar.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mindmatrix.pashuaahar.domain.FeedIngredient
import com.mindmatrix.pashuaahar.presentation.components.*
import com.mindmatrix.pashuaahar.presentation.theme.BeautifulCard
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.GradientBackground
import com.mindmatrix.pashuaahar.presentation.theme.PulseButton
import com.mindmatrix.pashuaahar.presentation.theme.Sky
import com.mindmatrix.pashuaahar.presentation.theme.SlideInContent
import com.mindmatrix.pashuaahar.presentation.theme.Turmeric

@Composable
fun CustomFeedScreen(
    ingredients: List<FeedIngredient>,
    onRecipeSaved: (Map<String, Float>) -> Unit
) {
    var selectedIds by remember { mutableStateOf(setOf<String>()) }
    var selectedCategory by remember { mutableStateOf("Energy Grasses") }
    
    val categories = listOf("Energy Grasses", "Milk Builders", "Nutrient Boosters", "Modern Supplements")
    
    // Derived state for nutrients
    val selectedIngredients = ingredients.filter { it.id in selectedIds }
    val avgProtein = if (selectedIngredients.isEmpty()) 0f else selectedIngredients.sumOf { it.crudeProteinPercentage }.toFloat() / (selectedIngredients.size * 30f)
    val avgEnergy = if (selectedIngredients.isEmpty()) 0f else selectedIngredients.sumOf { it.energyKcal.toDouble() }.toFloat() / (selectedIngredients.size * 3500f)
    
    val grassCount = selectedIngredients.count { it.category == "Energy Grasses" }
    val boosterCount = selectedIngredients.count { it.category == "Milk Builders" || it.category == "Nutrient Boosters" }
    val totalMain = (grassCount + boosterCount).coerceAtLeast(1)
    
    val grassPercent = grassCount.toFloat() / totalMain
    val boosterPercent = boosterCount.toFloat() / totalMain

    GradientBackground {
        SlideInContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                ScreenHeader(
                    title = "Scientific Feed",
                    subtitle = "Follow the 70/30 Golden Ratio for health"
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Golden Ratio Indicator
                BeautifulCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Golden Ratio Balance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        NutrientBalanceMeter(grassPercent = grassPercent, legumePercent = boosterPercent)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Energy Grass: ${(grassPercent * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = FieldGreen)
                            Text(text = "Builders: ${(boosterPercent * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = Sky)
                        }
                        if (grassPercent in 0.6f..0.8f && boosterPercent in 0.2f..0.4f) {
                            Text(text = "✓ Perfect Scientific Balance!", color = FieldGreen, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.ExtraBold)
                        } else if (selectedIds.isNotEmpty()) {
                            Text(text = "Aim for 70% Grass / 30% Builders", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Category Tabs
                ScrollableTabRow(
                    selectedTabIndex = categories.indexOf(selectedCategory),
                    containerColor = Color.Transparent,
                    contentColor = FieldGreen,
                    edgePadding = 0.dp,
                    divider = {}
                ) {
                    categories.forEach { category ->
                        Tab(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            text = { Text(category) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Horizontal Grid of Illustrated Cards
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(ingredients.filter { it.category == selectedCategory }) { fodder ->
                        FodderCard(
                            name = fodder.name,
                            category = fodder.category,
                            visualHint = fodder.visualHint,
                            benefit = fodder.healthBenefit,
                            selected = fodder.id in selectedIds,
                            onClick = {
                                selectedIds = if (fodder.id in selectedIds) {
                                    selectedIds - fodder.id
                                } else {
                                    selectedIds + fodder.id
                                }
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Scientific Note for Category
                val categoryNote = when(selectedCategory) {
                    "Energy Grasses" -> "These provide essential fiber for digestion and energy for milk volume."
                    "Milk Builders" -> "Packed with protein and calcium for better milk quality (SNF/Fat)."
                    "Nutrient Boosters" -> "Natural multivitamins. Tip: Feed Subabul in moderation (<30%)."
                    else -> "Modern supplements like Azolla boost yield with minimal land use."
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(FieldGreen.copy(alpha = 0.1f))
                        .padding(16.dp)
                ) {
                    Text(text = "💡 Scientfic Note: $categoryNote", style = MaterialTheme.typography.bodyMedium, color = FieldGreen)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(text = "Recipe Impact", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NutrientMeter(label = "Milk Yield", value = avgProtein.coerceIn(0f, 1f), color = Sky)
                    NutrientMeter(label = "Cow Vitality", value = avgEnergy.coerceIn(0f, 1f), color = Turmeric)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                PulseButton(
                    text = "Apply Custom Mix",
                    onClick = { 
                        onRecipeSaved(selectedIds.associateWith { 1f }) 
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
