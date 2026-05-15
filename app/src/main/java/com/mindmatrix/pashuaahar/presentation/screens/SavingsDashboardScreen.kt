package com.mindmatrix.pashuaahar.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mindmatrix.pashuaahar.data.LocalMarketRepository
import com.mindmatrix.pashuaahar.domain.FeedIngredient
import com.mindmatrix.pashuaahar.domain.FeedRecipe
import com.mindmatrix.pashuaahar.domain.SavingsEngine
import com.mindmatrix.pashuaahar.domain.SavingsResult
import com.mindmatrix.pashuaahar.presentation.components.SavingsHistoryChart
import com.mindmatrix.pashuaahar.presentation.components.ScreenHeader
import com.mindmatrix.pashuaahar.presentation.theme.BeautifulCard
import com.mindmatrix.pashuaahar.presentation.theme.FieldGreen
import com.mindmatrix.pashuaahar.presentation.theme.GradientBackground
import com.mindmatrix.pashuaahar.presentation.theme.Meadow
import com.mindmatrix.pashuaahar.presentation.theme.SlideInContent
import com.mindmatrix.pashuaahar.presentation.theme.Turmeric
import kotlin.math.roundToInt

@Composable
fun SavingsDashboardScreen(
    customIngredients: Map<FeedIngredient, Double>,
    targetProtein: Double,
    onAddIngredient: (FeedIngredient) -> Unit,
    onRemoveIngredient: (String) -> Unit
) {
    val savingsEngine = remember { SavingsEngine() }
    
    // Create current recipe
    val recipe = remember(customIngredients) {
        FeedRecipe(
            id = "current_mix",
            recipeName = "Your Custom Mix",
            ingredients = customIngredients,
            totalCost = customIngredients.entries.sumOf { (ing, qty) -> ing.costPerKgInINR * qty },
            totalProtein = customIngredients.entries.sumOf { (ing, qty) -> (ing.crudeProteinPercentage / 100.0) * qty }
        )
    }
    
    val savingsResult = remember(recipe, targetProtein) {
        savingsEngine.compareFeedCosts(recipe, targetProtein)
    }
    
    val yieldImpact = remember(recipe) {
        val totalQty = customIngredients.values.sum()
        if (totalQty > 0) savingsEngine.calculateYieldImpact(recipe, totalQty) else 0.0
    }

    var showIngredientPicker by remember { mutableStateOf(false) }

    GradientBackground {
        SlideInContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                ScreenHeader(title = "Financial Impact", subtitle = "Homemade vs Market Feed")
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Summary Card
                SummaryCard(savingsResult.monthlyProjectedSavings)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Savings History Chart
                SavingsHistorySection(dailySavings = listOf(140.0, 165.0, 150.0, 180.0, 175.0, 190.0, savingsResult.dailySavings))
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Comparison View
                ComparisonSplitCard(recipe, savingsResult, targetProtein)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Nutritional Fulfillment
                NutritionalFulfillment(recipe.totalProtein, targetProtein)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Recipe Ingredients", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    IconButton(
                        onClick = { showIngredientPicker = true },
                        modifier = Modifier.background(FieldGreen, RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = "Add More", tint = Color.White)
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Ingredient List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(customIngredients.toList()) { (ingredient, quantity) ->
                        IngredientItem(
                            ingredient = ingredient, 
                            quantity = quantity,
                            onRemove = { onRemoveIngredient(ingredient.id) }
                        )
                    }
                    
                    if (yieldImpact > 0.1) {
                        item {
                            YieldImpactNote(yieldImpact)
                        }
                    }
                }
            }
        }
    }

    if (showIngredientPicker) {
        IngredientPickerDialog(
            onDismiss = { showIngredientPicker = false },
            onSelected = { 
                onAddIngredient(it)
                showIngredientPicker = false
            }
        )
    }
}

@Composable
private fun SavingsHistorySection(dailySavings: List<Double>) {
    var isCumulative by remember { mutableStateOf(false) }
    
    BeautifulCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isCumulative) "Monthly Growth" else "Daily Savings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Switch(
                    checked = isCumulative,
                    onCheckedChange = { isCumulative = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = FieldGreen)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            SavingsHistoryChart(
                dailySavings = dailySavings,
                isCumulative = isCumulative
            )
        }
    }
}

@Composable
private fun SummaryCard(monthlySavings: Double) {
    BeautifulCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Total Savings This Month", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "₹${monthlySavings.roundToInt()}",
                style = MaterialTheme.typography.displayMedium,
                color = Meadow,
                fontWeight = FontWeight.ExtraBold
            )
            Text(text = "Projected based on current recipe", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
private fun ComparisonSplitCard(recipe: FeedRecipe, result: SavingsResult, targetProtein: Double) {
    BeautifulCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // Your Custom Mix
            Column(
                modifier = Modifier.weight(1f).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Your Mix", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "₹${recipe.totalCost.roundToInt()}", style = MaterialTheme.typography.headlineMedium, color = FieldGreen, fontWeight = FontWeight.Bold)
                Text(text = "Cost per day", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${(recipe.totalProtein * 1000).roundToInt()}g", style = MaterialTheme.typography.titleMedium)
                Text(text = "Total Protein", style = MaterialTheme.typography.labelSmall)
            }
            
            VerticalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = Color.LightGray)
            
            // Standard Market Feed
            Column(
                modifier = Modifier.weight(1f).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Market Feed", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "₹${result.marketEquivalentCost.roundToInt()}", style = MaterialTheme.typography.headlineMedium, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text(text = "Equivalent cost", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${(targetProtein * 1000).roundToInt()}g", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                Text(text = "Total Protein", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun NutritionalFulfillment(current: Double, target: Double) {
    val progress = if (target > 0) (current / target).toFloat() else 0f
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Nutritional Fulfillment", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "${(progress * 100).roundToInt()}%", style = MaterialTheme.typography.titleMedium, color = if (progress >= 1f) FieldGreen else Turmeric)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
            color = if (progress >= 1f) FieldGreen else Turmeric,
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun IngredientItem(
    ingredient: FeedIngredient, 
    quantity: Double,
    onRemove: () -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(FieldGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = ingredient.name.take(1))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = ingredient.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "₹${ingredient.costPerKgInINR}/kg · ${ingredient.crudeProteinPercentage}% CP", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(text = "${quantity}kg", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = FieldGreen)
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onRemove) {
                Icon(Icons.Rounded.Delete, contentDescription = "Remove", tint = Color.Red.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
private fun YieldImpactNote(yieldImpact: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Meadow.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        Column {
            Text(text = "🚀 Yield Bonus!", style = MaterialTheme.typography.titleSmall, color = FieldGreen, fontWeight = FontWeight.Bold)
            Text(
                text = "Based on your recipe's protein/energy balance, you could see an extra ${"%.1f".format(yieldImpact)}L of milk per day!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IngredientPickerDialog(onDismiss: () -> Unit, onSelected: (FeedIngredient) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        BeautifulCard(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Select Ingredient", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(LocalMarketRepository.allIngredients) { ingredient ->
                        OutlinedCard(
                            onClick = { onSelected(ingredient) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = ingredient.name, fontWeight = FontWeight.Bold)
                                    Text(text = "₹${ingredient.costPerKgInINR}/kg", style = MaterialTheme.typography.labelSmall)
                                }
                                Badge(containerColor = Meadow.copy(alpha = 0.2f), contentColor = FieldGreen) {
                                    Text("${ingredient.crudeProteinPercentage}% CP")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
