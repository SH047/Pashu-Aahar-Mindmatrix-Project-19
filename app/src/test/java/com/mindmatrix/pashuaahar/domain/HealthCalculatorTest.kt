package com.mindmatrix.pashuaahar.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HealthCalculatorTest {
    private val calculator = HealthCalculator()

    @Test
    fun `healthy default cow lands in healthy band`() {
        val result = calculator.bodyCondition(CowProfile())

        assertEquals(HealthBand.Healthy, result.band)
    }

    @Test
    fun `missed daily care lowers body condition reading`() {
        val completeCare = calculator.bodyCondition(CowProfile(), careCompletionRatio = 1f)
        val missedCare = calculator.bodyCondition(CowProfile(), careCompletionRatio = 0f)

        assertTrue(missedCare.score < completeCare.score)
    }
}
