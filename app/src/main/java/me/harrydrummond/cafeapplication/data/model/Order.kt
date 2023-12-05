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
): Serializable{
    fun mapDuplicatesToQuantity(productList: List<Product>): List<Pair<Int, Product>> {
        val productCountMap = mutableMapOf<Product, Int>()

        // Count occurrences of each product
        for (product in productList) {
            productCountMap[product] = productCountMap.getOrDefault(product, 0) + 1
        }

        // Map "map" to list of pairs.
        return productCountMap.map { (product, count) -> count to product }
    }
}
