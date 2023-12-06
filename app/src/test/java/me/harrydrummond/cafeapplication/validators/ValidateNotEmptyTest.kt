package me.harrydrummond.cafeapplication.validators

import junit.framework.TestCase
import me.harrydrummond.cafeapplication.Validators
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Arrays

/**
 * Contains tests for the [Validators] to assert that the functions
 * return correctly.
 */
@RunWith(Parameterized::class)
class ValidateNotEmptyTest(private val testString: String, private val expectedIsValid: Boolean) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() : Collection<Array<Any>> {
            return Arrays.asList(
                arrayOf("", false),
                arrayOf("iumhye", true),
                arrayOf("yidyhushfmdisoaj,sd", true),
                arrayOf("a", true),
            )
        }
    }

    /**
     * Tests that the [Validators.validateNotEmpty] function
     * validates an empty string correctly.
     */
    @Test
    fun `Test not empty validates, EMPTY correctly`() {
        // Act
        val result = Validators.validateNotEmpty(testString)

        // Assert
        TestCase.assertEquals(expectedIsValid, result.isValid)
        if (expectedIsValid) {
            TestCase.assertNull(result.message)
        } else {
            TestCase.assertNotNull(result.message)
        }
    }
}