package com.mindmatrix.pashuaahar.domain

import com.mindmatrix.pashuaahar.data.SeedData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NutritionCalculatorTest {
    private val calculator = NutritionCalculator()

    @Test
    fun `feed plan includes all core ingredient groups`() {
        val plan = calculator.calculate(CowProfile(), SeedData.ingredients)

        // Updated to 4 lines: Super Napier, Berseem, Moringa, Mineral
        assertEquals(4, plan.lines.size)
        assertTrue(plan.totalKg > 0f)
        assertTrue(plan.totalCost > 0f)
    }

    @Test
    fun `late pregnancy increases ration size`() {
        val normal = calculator.calculate(CowProfile(pregnancyMonth = 0), SeedData.ingredients)
        val pregnant = calculator.calculate(CowProfile(pregnancyMonth = 7), SeedData.ingredients)

        assertTrue(pregnant.totalKg > normal.totalKg)
    }
}
