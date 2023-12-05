package me.harrydrummond.cafeapplication

data class ValidatedResult(
    val isValid: Boolean,
    val message: String?,
)
