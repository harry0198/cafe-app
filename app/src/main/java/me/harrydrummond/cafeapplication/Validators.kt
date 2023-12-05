package me.harrydrummond.cafeapplication

import android.telephony.PhoneNumberUtils
import android.widget.EditText
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Validation functions for user inputs.
 * Contains functions for email, password, phone number, empty and funtions to apply
 * the returned ValidatedResult to an EditText field.
 */
object Validators {

    /**
     * Validates an email string is of correct formatting
     *
     * @param email to validate
     * @return ValidatedResult containing if is valid and error message.
     */
    fun validateEmail(email: String): ValidatedResult {
        val emailPattern = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern: Pattern = Pattern.compile(emailPattern)
        val matcher: Matcher = pattern.matcher(email)
        val isValid = matcher.matches()

        return ValidatedResult(isValid, if (isValid) null else "Invalid email address format")
    }

    fun validateUsername(username: String): ValidatedResult {
        return validateNotEmpty(username)
    }

    /**
     * Validates a password string adheres to security practices
     *
     * @param password to validate
     * @return ValidatedResult containing if is valid and error message.
     */
    fun validatePassword(password: String): ValidatedResult {
        return validateNotEmpty(password)
    }


    /**
     * Validates a phone number string is of correct formatting
     *
     * @param phoneNumber to validate
     * @return ValidatedResult containing if is valid and error message.
     */
    fun validatePhoneNumber(phoneNumber: String): ValidatedResult {
        val isValid = PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)

        return ValidatedResult(isValid, if (isValid) null else "Invalid phone number format")
    }

    /**
     * Validates an input string is not null
     *
     * @param str to validate
     * @return ValidatedResult containing if is valid and error message.
     */
    fun validateNotEmpty(str: String?): ValidatedResult {
        val isValid = !str.isNullOrEmpty()

        return ValidatedResult(isValid, if (isValid) null else "Cannot be empty")
    }

    /**
     * Applies a validated result to an edit text field. If validated result is invalid,
     * then the error message is added to the field. Otherwise, it is removed.
     *
     * @param validatedResult to apply to the field
     * @param field Edit text field to add the result to.
     */
    fun apply(validatedResult: ValidatedResult, field: EditText) {
        if (validatedResult.isValid || validatedResult.message == null) {
            field.error = null
        } else {
            field.error = validatedResult.message
        }
    }
}