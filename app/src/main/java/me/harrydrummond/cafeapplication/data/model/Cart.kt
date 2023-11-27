package me.harrydrummond.cafeapplication.data.model

import java.io.Serializable

data class Cart(val cartProducts: MutableList<ProductQuantity>): Serializable {

    fun addToCart(productId: String, quantity: Int) {
        for (product in cartProducts) {
            if (product.productId == productId) {
                product.quantity+=quantity
                return
            }
        }

        val product = ProductQuantity(productId, quantity)
        cartProducts.add(product)
    }

    fun updateCartItem(productId: String, quantity: Int) {
        for ((index, product) in cartProducts.withIndex()) {
            if (product.productId == productId) {
                product.quantity=quantity

                if (product.quantity <= 0) {
                    cartProducts.removeAt(index)
                }
                return
            }
        }
    }

}
