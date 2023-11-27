package me.harrydrummond.cafeapplication.data.model

import java.io.Serializable

data class ProductQuantity(var productId: String = "", var quantity: Int = 0): Serializable {
}