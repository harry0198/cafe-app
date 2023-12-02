package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import me.harrydrummond.cafeapplication.data.model.Cart
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.UserModel

/**
 * Interface defining signatures to perform CRUD operations on a user repository.
 * Allows the user to log in, register, deal with cart operations.
 *
 * @see UserModel
 * @see Task
 */
interface IUserRepository {

    /**
     * Saves the user to the user repository.
     *
     * @param userId Id of user to save
     * @param userModel UserModel object to save to the database
     * @return Task of the operation.
     */
    fun saveUser(userId: String, userModel: UserModel): Task<Any>

    /**
     * Registers a new user account.
     *
     * @param email Email address to register with
     * @param password Password to register with
     * @return Task of the operation
     */
    fun registerUser(email: String, password: String): Task<Any>

    /**
     * Logs the user in using the credentials provided.
     *
     * @param email Email address to log in with.
     * @param password Password to log in with.
     * @return Task of the operation
     */
    fun loginUser(email: String, password: String): Task<Any>

    /**
     * Fetches the currently logged in user's id.
     *
     * @return User id or null if not logged in.
     */
    fun getLoggedInUserId(): String?

    /**
     * Logs out the current user
     */
    fun logoutUser()

    /**
     * Fetches the user from the user repository.
     *
     * @param uid User ID of the user to fetch.
     * @return Task of the operation
     *
     * @see UserModel
     */
    fun getUser(uid: String): Task<UserModel?>

    /**
     * Saves the currently logged-in user's cart.
     * @param cart Cart to save
     * @return Task containing the success of the operation.
     *
     * @see Cart
     */
    fun saveUserCart(cart: Cart): Task<Boolean>


    /**
     * Partially loads the user's cart, retrieving the cart object.
     *
     * @return Task containing the loaded cart or null.
     */
    fun partialLoadUserCart(): Task<Cart?>
}