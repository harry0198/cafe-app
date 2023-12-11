package me.harrydrummond.cafeapplication.validators

import junit.framework.TestCase
import me.harrydrummond.cafeapplication.logic.validators.Validators
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Arrays


/**
 * Contains tests for the [Validators.validatePrice] to assert that the functions
 * return correctly.
 */
@RunWith(Parameterized::class)
class ValidatePriceTest(private val testString: Double, private val expectedIsValid: Boolean) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(1.0, true),
                arrayOf(15.6, true),
                arrayOf(.0, true),
                arrayOf(3507283.5, true),
                arrayOf(3, true),
                arrayOf(3.1, true),
                arrayOf(124, true),
                arrayOf(0.15, true),
                arrayOf(0.1764342, false),
                arrayOf(15325332.45872341, false),
                arrayOf(3289352.59, true)
            )
        }
    }

    /**
     * Tests that the [Validators.validatePrice] function
     * validates a price correctly.
     */
    @Test
    fun testEmailValidation() {
        // Act
        val result = Validators.validatePrice(testString)

        // Assert
        TestCase.assertEquals(expectedIsValid, result.isValid)
        if (expectedIsValid) {
            TestCase.assertNull(result.message)
        } else {
            TestCase.assertNotNull(result.message)
        }
    }
}