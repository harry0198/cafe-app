package me.harrydrummond.cafeapplication.data.model

import java.io.Serializable

/**
 * Data class for the review list adapter containing information of the review written
 *
 * @param reviewId Id of the review
 * @param userId Id of the user who wrote the review
 * @param productId Id of the product being reviewed
 * @param review Actual written review.
 */
data class Review(
    val reviewId: Int,
    val userId: Int,
    val productId: Int,
    val review: String = "",
): Serializable
