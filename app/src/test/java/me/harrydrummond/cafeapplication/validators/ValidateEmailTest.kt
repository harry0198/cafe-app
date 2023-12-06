package me.harrydrummond.cafeapplication.validators

import junit.framework.TestCase
import me.harrydrummond.cafeapplication.Validators
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Arrays

/**
 * Contains tests for the [Validators.validateEmail] to assert that the functions
 * return correctly.
 */
@RunWith(Parameterized::class)
class ValidateEmailTest(private val testString: String, private val expectedIsValid: Boolean) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return Arrays.asList(
                arrayOf("hello", false),
                arrayOf("world", false),
                arrayOf("test@", false),
                arrayOf("test@42", false),
                arrayOf("test@42.com", true),
                arrayOf("test@42.3", false),
                arrayOf("test@harry.com", true),
                arrayOf("test@harry.13", false),
                arrayOf("23523@home.com", true),
                arrayOf("test.com", false),
            )
        }
    }

    /**
     * Tests that the [Validators.validateEmail] function
     * validates an email correctly.
     */
    @Test
    fun testEmailValidation() {
        // Act
        val result = Validators.validateEmail(testString)

        // Assert
        TestCase.assertEquals(expectedIsValid, result.isValid)
        if (expectedIsValid) {
            TestCase.assertNull(result.message)
        } else {
            TestCase.assertNotNull(result.message)
        }
    }
}