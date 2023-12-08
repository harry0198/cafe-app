package me.harrydrummond.cafeapplication.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Singleton class that holds a user's cart contents.
 * A cart holds many products.
 */
class Cart private constructor() {

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    companion object {
        // Singleton instance
        private var instance: Cart? = null

        // Thread-safe getInstance using the double-checked locking pattern
        fun getInstance(): Cart {
            return instance ?: synchronized(this) {
                instance ?: Cart().also { instance = it }
            }
        }
    }

    /**
     * Get all products in the cart
     *
     * @return List of Products in the cart
     * @see Product
     * @see List
     */
    fun getProducts(): List<Product> {
        return products.value ?: emptyList()
    }

    /**
     * Add an amount of products to the cart
     *
     * @param product Product to add
     * @param quantity Quantity to add
     */
    fun addProduct(product: Product, quantity: Int) {
        val list = getProducts().toMutableList()
        for (i in 0..<quantity) {
            list.add(product)
        }
        _products.postValue(list)
    }

    /**
     * Clear products from the cart matching the given product
     *
     * @param product Product to clear
     */
    fun clearProductsMatching(product: Product) {
        val prods = getProducts().toMutableList()
        prods.removeAll { it.productId == product.productId}
        _products.value = prods
    }

    /**
     * Clear the entire cart contents.
     */
    fun clear() {
        _products.postValue(emptyList())
    }

}