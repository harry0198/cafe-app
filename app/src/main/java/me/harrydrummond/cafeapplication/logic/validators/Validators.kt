package me.harrydrummond.cafeapplication.logic.validators

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

    /**
     * Validates a username string is correct
     *
     * @param username Username to validate
     * @return validatedresult cnotaining if is valid and error message
     */
    fun validateUsername(username: String): ValidatedResult {
        return validateNotEmpty(username)
    }

    /**
     * Validates a password string adheres to security practices
     *
     * @param password to validate
     * @return ValidatedPasswordResult containing validation passes and failures.
     */
    fun validatePassword(password: String): ValidatedPasswordResult {
        val passwordValidationResult = ValidatedPasswordResult()
        passwordValidationResult.passEmptyValidation = validateNotEmpty(password).isValid
        passwordValidationResult.passDigitValidation = hasDigit(password)
        passwordValidationResult.passCapitalValidation = hasCapitalLetter(password)
        passwordValidationResult.passMinLengthValidation = password.length > PasswordConstants.MIN_PASSWORD_LENGTH
        passwordValidationResult.passMaxLengthValidation = password.length < PasswordConstants.MAX_PASSWORD_LENGTH

        return passwordValidationResult
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
     * Validates an input double is of correct formatting for a price (2 decimal places required)
     *
     * @param price to validate formatting
     * @return ValidatedResult containing if is valid and error message.
     */
    fun validatePrice(price: Double): ValidatedResult {
        val priceRegex = Regex("^\\d+(\\.\\d{1,2})?$")
        val isValid = priceRegex.matches(price.toString())

        return ValidatedResult(isValid, if (isValid) null else "Incorrect price formatting. 2 Decimal Places required")
    }

    /**
     * Validates card number is valid
     *
     * @param cardNo to validate
     * @return Validated result containing if is valida nd erro mesasge.
     */
    fun validateCardNo(cardNo: String): ValidatedResult {
        val isValid = cardNo.length == 16

        return ValidatedResult(isValid, if (isValid) null else "Card Number length should be 16 numbers")
    }

    /**
     * Validates expiry number is valid
     *
     * @param expiry to validate
     * @return Validated result containing if is valid and error message
     */
    fun validateExpiry(expiry: String): ValidatedResult {
        val lengthValid = expiry.length == 4

        if (!lengthValid) {
            return ValidatedResult(false, "Expiry length should be 4 numbers")
        }

        val expiryPattern = ("(?:0[1-9]|1[0-2])[0-9]{2}")
        val pattern: Pattern = Pattern.compile(expiryPattern)
        val matcher: Matcher = pattern.matcher(expiry)
        val dateCorrect = matcher.matches()

        return ValidatedResult(dateCorrect, if (dateCorrect) null else "Not a valid expiry date")
    }

    /**
     * Validates cvv number is valid
     *
     * @param cvv to validate
     * @return Validated result containing if is valid and error message
     */
    fun validateCVV(cvv: String): ValidatedResult {
        val isValid = cvv.length == 3

        return ValidatedResult(isValid, if (isValid) null else "CVV length should be 3 numbers")
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

    private fun hasDigit(string: String): Boolean {
        for (char in string) {
            if (char.isDigit()) {
                return true
            }
        }
        return false
    }

    private fun hasCapitalLetter(string: String): Boolean {
        for (char in string) {
            if (char.isUpperCase()) {
                return true
            }
        }
        return false
    }
}