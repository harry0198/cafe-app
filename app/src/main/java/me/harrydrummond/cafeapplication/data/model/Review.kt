package me.harrydrummond.cafeapplication.data.model

import java.io.Serializable

/**
 * Data class for the review list adapter containing information of the review written
 */
data class Review(
    val reviewId: Int,
    val userId: Int,
    val productId: Int,
    val review: String = "",
): Serializable
