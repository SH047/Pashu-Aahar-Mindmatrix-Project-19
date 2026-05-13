package com.mindmatrix.pashuaahar.domain

import kotlin.math.round

class HealthCalculator {
    fun bodyCondition(profile: CowProfile, careCompletionRatio: Float = 1f): BodyConditionResult {
        val ageFactor = (profile.ageMonths.coerceIn(18, 96) - 18) / 78f
        val milkFactor = profile.dailyMilkLitres / 18f
        val expectedWeight = 300f + (ageFactor * 180f) + (milkFactor * 35f)
        val missedCarePenalty = (1f - careCompletionRatio.coerceIn(0f, 1f)) * 0.7f
        val baseScore = if (profile.bcsScore in 1..5) {
            profile.bcsScore.toFloat()
        } else {
            profile.weightKg / expectedWeight * 3f
        }
        val score = (baseScore - missedCarePenalty).coerceIn(1f, 5f)
        val rounded = round(score * 10f) / 10f
        val band = when {
            rounded < 2.5f -> HealthBand.Low
            rounded > 3.8f -> HealthBand.High
            else -> HealthBand.Healthy
        }
        val message = when (band) {
            HealthBand.Low -> "Increase dry matter slowly, complete the daily care routine, and check for parasites or dental issues."
            HealthBand.Healthy -> "Body condition is in a practical field range for dairy cattle."
            HealthBand.High -> "Reduce excess concentrate and track milk yield for the next 7 days."
        }

        return BodyConditionResult(
            score = rounded,
            band = band,
            message = message
        )
    }
}
