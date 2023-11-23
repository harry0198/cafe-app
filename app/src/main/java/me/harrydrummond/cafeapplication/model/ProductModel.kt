package me.harrydrummond.cafeapplication.model

import java.io.Serializable

data class ProductModel(
    val productName: String,
    val productPrice: Double,
    val productImage: String,
    val productDescription: String,
    val productAvailable: Boolean) : Serializable
{}