package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.Cart
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.UserModel
import javax.inject.Inject

/**
 * OrderRepository class, is an implementation of the OrderRepository Interface using Firestore.
 * Contains functions to perform CRUD operations on orders.
 *
 * @see Firebase
 * @see IProductRepository
 */
class FirestoreUserRepository(): IUserRepository {
    companion object {
        /**
         * Name of this repository document
         */
        const val DOCUMENT_NAME = "users"

        /**
         * Name of a user's cart collection
         */
        const val CART_COLLECTION_NAME = "cart"
    }

    private val db = Firebase.firestore

    /**
     * @inheritDoc
     */
    override fun getLoggedInUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override fun logoutUser() {
        Firebase.auth.signOut()
    }

    /**
     * @inheritDoc
     */
    override fun saveUser(userId: String, userModel: UserModel): Task<Any> {
        val document = db.collection(DOCUMENT_NAME).document(userId)
        return document.set(userModel).continueWith {}
    }

    /**
     * @inheritDoc
     */
    override fun registerUser(email: String, password: String): Task<Any> {
        return registerUserAuth(email, password).continueWith {}
    }

    /**
     * @inheritDoc
     */
    override fun loginUser(email: String, password: String): Task<Any> {
        return Firebase.auth.signInWithEmailAndPassword(email, password).continueWith {}
    }

    /**
     * @inheritDoc
     */
    override fun getUser(uid: String): Task<UserModel?> {
        val document = db.collection(DOCUMENT_NAME).document(uid)
        return document.get().continueWith { task: Task<DocumentSnapshot> ->
            if (task.isSuccessful) {
                val documentSnapshot = task.result
                documentSnapshot?.toObject(UserModel::class.java)
            } else {
                null
            }
        }
    }

    /**
     * @inheritDoc
     */
    override fun saveUserCart(cart: Cart): Task<Boolean> {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            val db = Firebase.firestore
            val cartRef = db.collection("users").document(userId).collection("cart")

            // Clear existing cart and add new cart items
            return cartRef.get().continueWith { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    for (document in documents) {
                        document.reference.delete()
                    }
                    for (cartProductMap in cart.cartProducts) {
                        cartRef.add(cartProductMap)
                    }
                    true
                } else {
                    false
                }
            }
        }

        return Tasks.forResult(false)
    }

    /**
     * @inheritDoc
     */
    override fun partialLoadUserCart(): Task<Cart?> {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val cartRef = db.collection(DOCUMENT_NAME).document(userId).collection(CART_COLLECTION_NAME)

            return cartRef.get().continueWith { task ->
                if (task.isSuccessful) {
                    val cartProducts = task.result.mapNotNull { it.toProductQuantity() }.toMutableList()
                    Cart(cartProducts)
                } else {
                    null
                }
            }
        } else {
            // If user is not authenticated, return an already completed task with an empty Cart
            return Tasks.forResult(Cart(mutableListOf()))
        }
    }

    private fun registerUserAuth(email: String, password: String): Task<AuthResult> {
        return Firebase.auth.createUserWithEmailAndPassword(email, password)
    }
}