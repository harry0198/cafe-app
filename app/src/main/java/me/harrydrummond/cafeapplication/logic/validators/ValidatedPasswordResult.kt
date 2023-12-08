package me.harrydrummond.cafeapplication.logic.validators

data class ValidatedPasswordResult(
    var passEmptyValidation: Boolean = false,
    var passMinLengthValidation: Boolean = false,
    var passMaxLengthValidation: Boolean = false,
    var passDigitValidation: Boolean = false,
    var passCapitalValidation: Boolean = false,
) {
    /**
     * Do all the validations pass?
     */
    fun allDoPass(): Boolean {
        return passEmptyValidation &&
                passMinLengthValidation &&
                passMaxLengthValidation &&
                passDigitValidation &&
                passCapitalValidation
    }
}