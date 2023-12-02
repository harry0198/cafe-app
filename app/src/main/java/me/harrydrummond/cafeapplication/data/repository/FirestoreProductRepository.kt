package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.Cart
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.ProductQuantity
import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.data.model.UserReview
import javax.inject.Inject

/**
 * Implementation of the product repository using a firestore database.
 *
 * @see Firebase.firestore
 * @see IProductRepository
 */
class FirestoreProductRepository @Inject constructor(val userRepository: IUserRepository): IProductRepository{

    companion object {
        /**
         * Name of the document in firestore.
         */
        const val DOCUMENT_NAME = "products"
        const val REVIEWS_COLLECTION = "reviews"
    }

    private val db = Firebase.firestore

    /**
     * @inheritDoc
     */
    override fun saveProduct(product: Product): Task<Void> {
        val document = if (product.productId.isEmpty()) {
            db.collection(DOCUMENT_NAME).document()
        } else {
            db.collection(DOCUMENT_NAME).document(product.productId)
        }
        product.productId = document.id
        return document.set(product)
    }

    /**
     * @inheritDoc
     */
    override fun fullLoadUserCart(cart: Cart): Task<List<Pair<Int, Product>>?> {
        return getProductsByQuantity(cart.cartProducts)
    }

    /**
     * @inheritDoc
     */
    override fun deleteProduct(product: Product): Task<Void> {
        return db.collection(DOCUMENT_NAME).document(product.productId).delete()
    }

    /**
     * @inheritDoc
     */
    override fun getAllAvailableProducts(): Task<List<Product>> {
        return getAllProducts().continueWith { result ->
            if (result.isSuccessful) {
                val filteredProducts = result.result.filter {
                    it.productAvailable
                }

                filteredProducts
            } else {
                result.result
            }
        }
    }

    /**
     * @inheritDoc
     */
    override fun getProductsByQuantity(prodQuantities: List<ProductQuantity>): Task<List<Pair<Int, Product>>?> {
        return Tasks.whenAllSuccess<Product>(
            prodQuantities.mapNotNull { prodQuantity ->
                getProductById(prodQuantity.productId)
            }
        ).continueWith { getModelTask ->
            if (getModelTask.isSuccessful) {
                val productModels = getModelTask.result ?: emptyList()
                val resultPairs = prodQuantities.zip(productModels) { cartProduct, productModel ->
                    Pair(cartProduct.quantity, productModel)
                }
                resultPairs
            } else {
                null
            }
        }
    }

    /**
     * @inheritDoc
     */
    override fun getProductById(productId: String): Task<Product?> {
        return Firebase.firestore.collection(DOCUMENT_NAME)
            .document(productId)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    task.result.toProductModel()
                } else {
                    null
                }
            }
    }

    /**
     * @inheritDoc
     */
    override fun getAllProducts(): Task<List<Product>> {
        return db.collection(DOCUMENT_NAME).get().continueWith { result ->
            val products = mutableListOf<Product>()
            for (document in result.result) {
                val product = document.toObject(Product::class.java)
                product.productId = document.id
                products.add(product)
            }

            products
        }
    }

    /**
     * @inheritDoc
     */
    override fun saveReview(productId: String, review: Review): Task<Any> {
        val ref = db.collection(DOCUMENT_NAME).document(productId)
        return ref.collection(REVIEWS_COLLECTION).add(review).continueWith { }
    }

    /**
     * @inheritDoc
     */
    override fun getUserReviewsForProduct(productId: String, callback: (List<UserReview>?) -> Unit) {
        val reviewsCollection =  db.collection(DOCUMENT_NAME).document(productId).collection(REVIEWS_COLLECTION)

        reviewsCollection.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userReviews = mutableListOf<UserReview>()

                val reviewDocuments = task.result?.documents ?: emptyList()

                val userDetailTasks = reviewDocuments.mapNotNull { document ->

                    val review: Review? = document.toReview()
                    if (review == null) {
                        Tasks.forResult(null)
                    } else {
                        // Fetch user details using the getUser function asynchronously
                        userRepository.getUser(review.userId).addOnCompleteListener { userTask ->
                            if (userTask.isSuccessful) {
                                val userModel = userTask.result
                                if (userModel != null) {
                                    val userReview = UserReview(userModel, review)
                                    userReviews.add(userReview)
                                }
                            }
                        }
                    }
                }

                // Wait for all user detail tasks to complete
                Tasks.whenAllSuccess<UserReview>(userDetailTasks).continueWith {
                    callback(userReviews.toList())
                }
            } else {
                callback(null)
            }
        }
    }
}