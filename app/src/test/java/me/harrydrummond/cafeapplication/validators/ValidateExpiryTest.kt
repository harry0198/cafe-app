package me.harrydrummond.cafeapplication.validators

import junit.framework.TestCase
import me.harrydrummond.cafeapplication.Validators
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Arrays

/**
 * Contains tests for the [Validators.validateExpiry] to assert that the functions
 * return correctly.
 */
@RunWith(Parameterized::class)
class ValidateExpiryTest(private val testString: String, private val expectedIsValid: Boolean) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return Arrays.asList(
                arrayOf("0101", true),
                arrayOf("world", false),
                arrayOf("1212", true),
                arrayOf("1920", false),
                arrayOf("9090", false),
                arrayOf("39323427", false)
            )
        }
    }

    /**
     * Tests that the [Validators.validateExpiry] function
     * validates an email correctly.
     */
    @Test
    fun testExpiryValidation() {
        // Act
        val result = Validators.validateExpiry(testString)

        // Assert
        TestCase.assertEquals(expectedIsValid, result.isValid)
        if (expectedIsValid) {
            TestCase.assertNull(result.message)
        } else {
            TestCase.assertNotNull(result.message)
        }
    }
}