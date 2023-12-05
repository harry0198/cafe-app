package me.harrydrummond.cafeapplication.data.model

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

    fun getProducts(): List<Product> {
        return products
    }

    fun addProduct(product: Product, quantity: Int) {
        for (i in 0..<quantity) {
            products.add(product)
        }
    }

    fun removeProduct(product: Product, quantity: Int) {
        for (i in 0..quantity) {
            products.remove(product)
        }
    }

    fun clearProductsMatching(product: Product) {
        products.removeAll { it.productId == product.productId}
    }

    fun clear() {
        products.clear()
    }

}