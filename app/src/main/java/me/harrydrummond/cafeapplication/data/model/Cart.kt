package me.harrydrummond.cafeapplication.data.model

/**
 * Singleton class that holds a user's cart contents.
 * A cart holds many products.
 */
class Cart private constructor() {

    private var products: MutableList<Product> = mutableListOf()

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
        return products
    }

    /**
     * Add an amount of products to the cart
     *
     * @param product Product to add
     * @param quantity Quantity to add
     */
    fun addProduct(product: Product, quantity: Int) {
        for (i in 0..<quantity) {
            products.add(product)
        }
    }

    /**
     * Clear products from the cart matching the given product
     *
     * @param product Product to clear
     */
    fun clearProductsMatching(product: Product) {
        products.removeAll { it.productId == product.productId}
    }

    /**
     * Clear the entire cart contents.
     */
    fun clear() {
        products.clear()
    }

}