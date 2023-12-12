package me.harrydrummond.cafeapplication.mocks

import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.IProductRepository

/**
 * Fake implementation of the product repostitory. for testing.
 */
class FakeProductRepository : IProductRepository {

    private val products: MutableList<Product> = mutableListOf()

    /**
     * @inheritDoc
     */
    override fun getAllAvailableProducts(): List<Product> {
        return products.filter { it.productAvailable }
    }

    /**
     * @inheritDoc
     */
    override fun getAllProducts(): List<Product> {
        return products
    }

    /**
     * @inheritDoc
     */
    override fun save(type: Product): Int {
        val id = (1..100).random()
        val product = type.copy(productId = id)
        products.add(product)
        return id
    }

    /**
     * @inheritDoc
     */
    override fun update(type: Product): Boolean {
        val payment = getById(type.productId)?: return false
        products.remove(payment)
        products.add(payment)
        return true
    }

    /**
     * @inheritDoc
     */
    override fun getById(id: Int): Product? {
        return products.find { it.productId == id }
    }

    /**
     * @inheritDoc
     */
    override fun delete(type: Product): Boolean {
        return products.remove(type)
    }
}