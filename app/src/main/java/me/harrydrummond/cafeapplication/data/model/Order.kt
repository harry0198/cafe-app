package me.harrydrummond.cafeapplication.data.model

import java.io.Serializable
import java.util.Date

/**
 * Data class to indicate an order's basic
 * @param orderId Id of the order
 * @param timestamp Time that the order was placed
 * @param status Status of the order
 * @param userId Id of the user placing order
 * @param products Products in the order.
 */
data class Order(
    val orderId: Int,
    val timestamp: Date,
    val status: Status,
    val userId: Int,
    val products: MutableList<Product>
): Serializable
