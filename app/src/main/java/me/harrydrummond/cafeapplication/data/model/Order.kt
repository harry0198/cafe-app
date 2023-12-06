package me.harrydrummond.cafeapplication.data.model

import java.io.Serializable
import java.util.Date

/**
 * Data class to indicate an order's basic
 */
data class Order(
    val orderId: Int,
    val timestamp: Date,
    val status: Status,
    val userId: Int,
    val products: MutableList<Product>
): Serializable
