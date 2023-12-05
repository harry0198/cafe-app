package me.harrydrummond.cafeapplication.data.model

/**
 * Data class to indicate the many to many join
 * on the table.
 */
data class OrderDetails(
    val orderDetailsId: Int,
    val orderId: Int,
    val productId: Int,
)