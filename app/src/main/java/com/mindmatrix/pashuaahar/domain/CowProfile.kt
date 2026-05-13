package com.mindmatrix.pashuaahar.domain

data class CowProfile(
    val id: String = "cow_1",
    val name: String = "Lakshmi",
    val breed: String = "Gir",
    val weightKg: Int = 420,
    val ageMonths: Int = 48,
    val dailyMilkLitres: Float = 8f,
    val pregnancyMonth: Int = 0,
    val lactationDay: Int = 90,
    val lactationStage: String = "Early",
    val avatarStyle: Int = 0,
    val bcsScore: Int = 3,
    val offspringCount: Int = 0,
    val lastCalvingDate: String = "",
    val fmdVaccinated: Boolean = false,
    val brucellosisVaccinated: Boolean = false,
    val dewormed: Boolean = false
)

data class UserProfile(
    val farmerName: String = "Shreyas",
    val village: String = "",
    val phoneNumber: String = "",
    val farmName: String = "My dairy farm"
)

enum class HealthBand(val label: String) {
    Low("Needs weight gain"),
    Healthy("Healthy"),
    High("Needs diet review")
}

data class BodyConditionResult(
    val score: Float,
    val band: HealthBand,
    val message: String
)
