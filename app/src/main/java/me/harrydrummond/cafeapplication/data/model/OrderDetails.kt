package me.harrydrummond.cafeapplication.data.model

/**
 * Data class to indicate the many to many join
 * on the table.
 *
 * @param orderDetailsId Id of the join table
 * @param orderId Id of the order
 * @param productId Id of the product
 */
data class OrderDetails(
    val orderDetailsId: Int,
    val orderId: Int,
    val productId: Int,
)