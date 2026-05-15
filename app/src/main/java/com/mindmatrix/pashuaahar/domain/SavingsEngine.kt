package com.mindmatrix.pashuaahar.domain

import com.mindmatrix.pashuaahar.data.LocalMarketRepository
import kotlin.math.max

data class SavingsResult(
    val homemadeCost: Double,
    val marketEquivalentCost: Double,
    val dailySavings: Double,
    val monthlyProjectedSavings: Double
)

class SavingsEngine {

    /**
     * Compares the cost of a homemade recipe against the market equivalent.
     * The market equivalent is calculated as the cost to reach the same protein amount
     * using standard market pellets.
     */
    fun compareFeedCosts(
        homemadeRecipe: FeedRecipe,
        targetProteinAmount: Double
    ): SavingsResult {
        val homemadeCost = homemadeRecipe.totalCost
        
        // Market Reference: Standard Market Pellet (₹28/kg, 20% CP)
        val marketRef = LocalMarketRepository.commercialReference
        val marketProteinPerKg = marketRef.crudeProteinPercentage / 100.0 // 0.20
        
        // How many kg of market pellet needed for target protein?
        val kgNeeded = if (marketProteinPerKg > 0) targetProteinAmount / marketProteinPerKg else 0.0
        val marketEquivalentCost = kgNeeded * marketRef.costPerKgInINR
        
        val dailySavings = max(0.0, marketEquivalentCost - homemadeCost)
        
        return SavingsResult(
            homemadeCost = homemadeCost,
            marketEquivalentCost = marketEquivalentCost,
            dailySavings = dailySavings,
            monthlyProjectedSavings = dailySavings * 30
        )
    }

    /**
     * Estimates potential milk yield increase if the Homemade mix has a higher 
     * Protein/Energy ratio than the standard feed.
     * Logic: For every 5% increase in Protein efficiency vs market, estimate 0.5L yield boost.
     */
    fun calculateYieldImpact(
        homemadeRecipe: FeedRecipe,
        totalQuantityKg: Double
    ): Double {
        val marketRef = LocalMarketRepository.commercialReference
        
        // Homemade ratios
        val homemadeProteinDensity = homemadeRecipe.totalProtein / totalQuantityKg
        val homemadeEnergyTotal = homemadeRecipe.ingredients.entries.sumOf { (ing, qty) -> 
            ing.energyKcal.toDouble() * qty 
        }
        val homemadeEnergyDensity = homemadeEnergyTotal / totalQuantityKg
        
        val homemadeRatio = if (homemadeEnergyDensity > 0) homemadeProteinDensity / homemadeEnergyDensity else 0.0
        
        // Market ratio
        val marketRatio = if (marketRef.energyKcal > 0) (marketRef.crudeProteinPercentage / 100.0) / marketRef.energyKcal.toDouble() else 0.0
        
        if (homemadeRatio <= marketRatio) return 0.0
        
        // Improvement factor
        val improvement = (homemadeRatio - marketRatio) / marketRatio
        
        // 0.5L boost per 5% (0.05) improvement
        return (improvement / 0.05) * 0.5
    }
}
