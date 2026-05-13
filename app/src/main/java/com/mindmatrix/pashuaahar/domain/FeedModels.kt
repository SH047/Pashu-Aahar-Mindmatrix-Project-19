package com.mindmatrix.pashuaahar.domain

data class FeedIngredient(
    val id: String,
    val name: String,
    val category: String,
    val proteinPercent: Float,
    val energyMcalPerKg: Float,
    val pricePerKg: Float,
    val visualHint: String,
    val nutrientDensity: Float = 0.5f, // 0.0 to 1.0
    val healthBenefit: String = "Balanced nutrition"
)

data class FeedBuilderState(
    val selectedIngredients: Map<String, Float> = emptyMap(), // id to quantity ratio
    val totalProtein: Float = 0f,
    val totalEnergy: Float = 0f,
    val yieldBoost: Float = 0f
)

data class FeedLine(
    val ingredient: FeedIngredient,
    val quantityKg: Float,
    val cost: Float
)

data class FeedPlan(
    val lines: List<FeedLine>,
    val totalKg: Float,
    val totalCost: Float,
    val commercialCost: Float,
    val dailySavings: Float,
    val guidance: String
)

data class GrazingGrass(
    val name: String,
    val season: String,
    val proteinPercent: Float,
    val waterNeed: String,
    val recommendation: String
)

data class VaccinationItem(
    val name: String,
    val dueWindow: String,
    val priority: String,
    val note: String
)
