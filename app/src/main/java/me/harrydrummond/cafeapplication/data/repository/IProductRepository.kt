package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.data.model.Product

/**
 * Contains signatures for performing CRUD operations on a repository.
 * Contains helper methods; getAllAvailableProducts and getAllProducts
 *
 * @see Product
 * @author Harry Drummond
 */
interface IProductRepository : CrudRepository <Product> {

    /**
     * Gets all available products
     *
     * @return List of Products available
     *
     * @see Product
     */
    fun getAllAvailableProducts(): List<Product>

    /**
     * Gets all products in database
     *
     * @return List of products
     *
     * @see Product
     */
    fun getAllProducts(): List<Product>
}