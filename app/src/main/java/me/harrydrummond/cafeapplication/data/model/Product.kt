package me.harrydrummond.cafeapplication.data.model

import java.io.Serializable

data class Product(
    var productId: Int,
    var productName: String = "",
    var productPrice: Double = 0.0,
    var productImage: String = "",
    var productDescription: String = "",
    var productAvailable: Boolean = false,
) : Serializable