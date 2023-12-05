package me.harrydrummond.cafeapplication.logic

import me.harrydrummond.cafeapplication.data.model.Product

fun List<Product>.mapDuplicatesToQuantity(): List<Pair<Int, Product>> {
    val productCountMap = mutableMapOf<Product, Int>()

    // Count occurrences of each product
    for (product in this) {
        productCountMap[product] = productCountMap.getOrDefault(product, 0) + 1
    }

    // Map "map" to list of pairs.
    return productCountMap.map { (product, count) -> count to product }
}