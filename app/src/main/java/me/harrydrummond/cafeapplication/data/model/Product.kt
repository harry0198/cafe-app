package me.harrydrummond.cafeapplication.data.model

import java.io.Serializable

/**
 * A data class indicating a product in the database. Contains custom equals and hashcode methods.
 *
 * @param productId Id of the product in the database
 * @param productName Name of the product in the database
 * @param productPrice Price of the product
 * @param productImage Image of the product
 * @param productDescription Description of the product
 * @param productAvailable Is the product available.
 */
data class Product(
    var productId: Int,
    var productName: String = "",
    var productPrice: Double = 0.0,
    var productImage: ByteArray? = null,
    var productDescription: String = "",
    var productAvailable: Boolean = false,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (productId != other.productId) return false
        if (productName != other.productName) return false
        if (productPrice != other.productPrice) return false
        if (!productImage.contentEquals(other.productImage)) return false
        if (productDescription != other.productDescription) return false
        if (productAvailable != other.productAvailable) return false

        return true
    }

    override fun hashCode(): Int {
        var result = productId
        result = 31 * result + productName.hashCode()
        result = 31 * result + productPrice.hashCode()
        result = 31 * result + productImage.contentHashCode()
        result = 31 * result + productDescription.hashCode()
        result = 31 * result + productAvailable.hashCode()
        return result
    }
}