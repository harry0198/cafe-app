package me.harrydrummond.cafeapplication.data.model

/**
 * Essentially a joining table in sql terms.
 */
data class UserReview(
    val user: UserModel,
    val review: Review
)
