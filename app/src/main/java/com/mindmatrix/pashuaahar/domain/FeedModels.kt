package com.mindmatrix.pashuaahar.domain

data class FeedIngredient(
    val id: String,
    val name: String,
    val category: String,
    val crudeProteinPercentage: Double,
    val energyKcal: Int,
    val costPerKgInINR: Double,
    val visualHint: String = "",
    val nutrientDensity: Float = 0.5f,
    val healthBenefit: String = "Balanced nutrition"
)

data class FeedRecipe(
    val id: String,
    val recipeName: String,
    val ingredients: Map<FeedIngredient, Double>,
    val totalCost: Double,
    val totalProtein: Double
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
