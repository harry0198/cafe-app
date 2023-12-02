package me.harrydrummond.cafeapplication.data.model

import java.io.Serializable

/**
 * Data class for the review list adapter containing information on the username of the user
 * and review written.
 */
data class Review(
    val userId: String = "",
    val review: String = "",
): Serializable
