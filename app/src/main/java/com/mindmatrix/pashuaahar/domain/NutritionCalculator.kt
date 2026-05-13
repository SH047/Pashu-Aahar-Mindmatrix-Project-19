package com.mindmatrix.pashuaahar.domain

import kotlin.math.max
import kotlin.math.round

class NutritionCalculator {
    fun calculate(
        profile: CowProfile,
        ingredients: List<FeedIngredient>,
        isSuperMix: Boolean = false,
        customMixIds: Set<String> = emptySet(),
        grazingHours: Float = 0f
    ): FeedPlan {
        val milkSupportKg = profile.dailyMilkLitres * 0.42f
        val maintenanceKg = max(3.2f, profile.weightKg * 0.009f)
        val pregnancyBoostKg = if (profile.pregnancyMonth >= 6) 1.2f else 0f
        val targetKg = maintenanceKg + milkSupportKg + pregnancyBoostKg

        val lines = if (isSuperMix && customMixIds.isNotEmpty()) {
            val selected = ingredients.filter { it.id in customMixIds }
            val count = selected.size.coerceAtLeast(1)
            selected.map { ingredient ->
                FeedLine(ingredient, roundOne(targetKg / count), 0f)
            }
        } else {
            // Standard Ration using Market Staples
            val riceBran = ingredients.first { it.id == "rice_bran" }
            val cottonseed = ingredients.first { it.id == "cottonseed_cake_staple" }
            val kmf = ingredients.first { it.id == "kmf_pellets" }
            val mineral = ingredients.first { it.id == "mineral_mix" }

            // Grazing reduces the required filler bulk
            val grazingReduction = (grazingHours / 8f) * 2f
            
            listOf(
                FeedLine(riceBran, roundOne(max(0.5f, targetKg * 0.4f - grazingReduction)), 0f),
                FeedLine(cottonseed, roundOne(targetKg * 0.25f), 0f),
                FeedLine(kmf, roundOne(targetKg * 0.3f), 0f),
                FeedLine(mineral, roundOne(max(0.1f, targetKg * 0.05f)), 0f)
            )
        }.map { line ->
            line.copy(cost = roundOne(line.quantityKg * line.ingredient.pricePerKg))
        }

        val totalKg = roundOne(lines.sumOf { it.quantityKg.toDouble() }.toFloat())
        val totalCost = roundOne(lines.sumOf { it.cost.toDouble() }.toFloat())
        val commercialCost = roundOne(totalKg * 42f)
        val dailySavings = roundOne(commercialCost - totalCost)
        val guidance = when {
            isSuperMix -> "Super-Mix: Scientifically optimized with your selected local super-fodders."
            profile.dailyMilkLitres >= 12f -> "Standard: High yield needs high protein. Don't forget to soak your cakes for 4-6 hours!"
            else -> "Standard: Balanced everyday ration using reliable local market staples."
        }

        return FeedPlan(
            lines = lines,
            totalKg = totalKg,
            totalCost = totalCost,
            commercialCost = commercialCost,
            dailySavings = dailySavings,
            guidance = guidance
        )
    }

    fun calculateBalance(selectedIngredients: List<FeedIngredient>): Float {
        if (selectedIngredients.isEmpty()) return 0f
        val grassRatio = selectedIngredients.count { it.category == "Energy Grasses" }.toFloat() / selectedIngredients.size
        val legumeRatio = selectedIngredients.count { it.category == "Milk Builders" || it.category == "Nutrient Boosters" }.toFloat() / selectedIngredients.size
        
        // Target: 0.7 Grass / 0.3 Legume. Calculate score based on proximity to this ratio.
        val deviation = Math.abs(grassRatio - 0.7f) + Math.abs(legumeRatio - 0.3f)
        return (1f - deviation).coerceIn(0f, 1f)
    }

    private fun roundOne(value: Float): Float = round(value * 10f) / 10f
}
