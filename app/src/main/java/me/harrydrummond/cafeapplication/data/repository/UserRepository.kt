package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.Cart
import me.harrydrummond.cafeapplication.data.model.ProductQuantity
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.UserModel

class UserRepository {
    companion object {
        const val DOCUMENT_NAME = "users"
        const val CART_COLLECTION_NAME = "cart"
    }

    private val db = Firebase.firestore
    private val producRepository: ProductRepository = ProductRepository()

    fun saveUser(firebaseUser: FirebaseUser, userModel: UserModel): Task<Void> {
        val document = db.collection(DOCUMENT_NAME).document(firebaseUser.uid)
        return document.set(userModel)
    }

    fun registerUser(email: String, password: String): Task<AuthResult> {
        return registerUserAuth(email, password)
    }

    private fun registerUserAuth(email: String, password: String): Task<AuthResult> {
        return Firebase.auth.createUserWithEmailAndPassword(email, password)
    }

    fun loginUser(email: String, password: String): Task<AuthResult> {
        return Firebase.auth.signInWithEmailAndPassword(email, password)
    }
    fun getUser(uid: String): Task<UserModel?> {
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

    // Function to save the user's cart to Firestore
    fun saveUserCart(cart: Cart): Task<Boolean> {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            val db = Firebase.firestore
            val cartRef = db.collection("users").document(userId).collection("cart")

            // Convert Cart object to a list of maps for Firestore
            val cartProductsMap = cart.cartProducts.map {
                mapOf(
                    "productId" to it.productId,
                    "quantity" to it.quantity
                )
            }

            // Clear existing cart and add new cart items
            return cartRef.get().continueWith { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    for (document in documents) {
                        document.reference.delete()
                    }
                    for (cartProductMap in cartProductsMap) {
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


    fun fullLoadUserCart(cart: Cart, callback: (List<Pair<Int, ProductModel>>) -> Unit) {
        producRepository.getProductsByQuantity(cart.cartProducts) {
            callback(it)
        }
    }

    fun partialLoadUserCart(callback: (Cart?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val cartRef = db.collection(DOCUMENT_NAME).document(userId).collection(CART_COLLECTION_NAME)

            cartRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val cartProducts = task.result.map { it.toCartProduct() }.filterNotNull().toMutableList()
                    val cart = Cart(cartProducts)

                    // Callback with the result
                    callback(cart)
                } else {
                    callback(null)
                }
            }
        } else {
            // If user is not authenticated, return an already completed task with an empty Cart
            Tasks.forResult(Cart(mutableListOf()))
        }
    }
}