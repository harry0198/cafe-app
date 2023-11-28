package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.ProductQuantity

/**
 * Contains signatures for performing CRUD operations on a repository. Allows the full and
 * partial loading of a product by ProductQuantity.
 *
 * @see ProductModel
 * @see Task
 * @author Harry Drummond
 */
interface IProductRepository {

    /**
     * Saves a product to the repository.
     *
     * @param product The product to save to the repository
     * @return Task of the success of the save
     *
     * @see ProductModel
     */
    fun saveProduct(product: ProductModel): Task<Void>

    /**
     * Deletes a product from the repository.
     *
     * @param product The product to delete from the repository
     * @return Task of the success of the delete.
     *
     * @see ProductModel
     */
    fun deleteProduct(product: ProductModel): Task<Void>

    /**
     * Fetches all products marked as available.
     *
     * @return Task containing a list of the products
     *
     * @see ProductModel
     */
    fun getAllAvailableProducts(): Task<List<ProductModel>>

    /**
     * Fetches and loads the products by their product quantities.
     *
     * @param prodQuantities List of the ProductQuantity objects to use to fetch.
     * @return Task containing a List of Pairs which define the 1. Quantity 2. ProductModel
     *
     * @see ProductModel
     * @see Pair
     * @see ProductQuantity
     */
    fun getProductsByQuantity(prodQuantities: List<ProductQuantity>): Task<List<Pair<Int, ProductModel>>?>

    /**
     * Fetches and loads a product by its productId
     *
     * @param productId Id of the product to fetch
     * @return Task containing the nullable ProductModel
     */
    fun getProductById(productId: String): Task<ProductModel?>

    /**
     * Fetches all the products in the database
     *
     * @return Task containing a list of all the products in the database
     *
     * @see ProductModel
     */
    fun getAllProducts(): Task<List<ProductModel>>
}