package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import me.harrydrummond.cafeapplication.data.model.Cart
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.ProductQuantity
import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.model.UserReview

/**
 * Contains signatures for performing CRUD operations on a repository. Allows the full and
 * partial loading of a product by ProductQuantity.
 *
 * @see Product
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
     * @see Product
     */
    fun saveProduct(product: Product): Task<Void>

    /**
     * Deletes a product from the repository.
     *
     * @param product The product to delete from the repository
     * @return Task of the success of the delete.
     *
     * @see Product
     */
    fun deleteProduct(product: Product): Task<Void>

    /**
     * Fetches all products marked as available.
     *
     * @return Task containing a list of the products
     *
     * @see Product
     */
    fun getAllAvailableProducts(): Task<List<Product>>

    /**
     * Fetches and loads the products by their product quantities.
     *
     * @param prodQuantities List of the ProductQuantity objects to use to fetch.
     * @return Task containing a List of Pairs which define the 1. Quantity 2. ProductModel
     *
     * @see Product
     * @see Pair
     * @see ProductQuantity
     */
    fun getProductsByQuantity(prodQuantities: List<ProductQuantity>): Task<List<Pair<Int, Product>>?>

    /**
     * Fetches and loads a product by its productId
     *
     * @param productId Id of the product to fetch
     * @return Task containing the nullable ProductModel
     */
    fun getProductById(productId: String): Task<Product?>

    /**
     * Fetches all the products in the database
     *
     * @return Task containing a list of all the products in the database
     *
     * @see Product
     */
    fun getAllProducts(): Task<List<Product>>

    /**
     * Saves a review to a product in the database
     *
     * @return Task containing the success of the save
     *
     * @see Review
     * @see Product
     */
    fun saveReview(productId: String, review: Review): Task<Any>

    /**
     * Fetches all reviews for a specific product
     */
    fun getUserReviewsForProduct(productId: String, callback: (List<UserReview>?) -> Unit)

    /**
     * Fully loads the ProductModels of the user's cart with their product quantities.
     *
     * @param cart Cart object to fully load.
     * @return Task containing a List of the <Quantity and ProductModel>
     *
     * @see Pair
     * @see Product
     * @see Cart
     */
    fun fullLoadUserCart(cart: Cart): Task<List<Pair<Int, Product>>?>
}