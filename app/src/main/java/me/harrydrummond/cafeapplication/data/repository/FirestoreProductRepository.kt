package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.ProductQuantity

/**
 * Implementation of the product repository using a firestore database.
 *
 * @see Firebase.firestore
 * @see IProductRepository
 */
class FirestoreProductRepository: IProductRepository{

    companion object {
        /**
         * Name of the document in firestore.
         */
        const val DOCUMENT_NAME = "products"
    }

    private val db = Firebase.firestore

    /**
     * @inheritDoc
     */
    override fun saveProduct(product: ProductModel): Task<Void> {
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
    override fun deleteProduct(product: ProductModel): Task<Void> {
        return db.collection(DOCUMENT_NAME).document(product.productId).delete()
    }

    /**
     * @inheritDoc
     */
    override fun getAllAvailableProducts(): Task<List<ProductModel>> {
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
    override fun getProductsByQuantity(prodQuantities: List<ProductQuantity>): Task<List<Pair<Int, ProductModel>>?> {
        return Tasks.whenAllSuccess<ProductModel>(
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
    override fun getProductById(productId: String): Task<ProductModel?> {
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
    override fun getAllProducts(): Task<List<ProductModel>> {
        return db.collection(DOCUMENT_NAME).get().continueWith { result ->
            val products = mutableListOf<ProductModel>()
            for (document in result.result) {
                val product = document.toObject(ProductModel::class.java)
                product.productId = document.id
                products.add(product)
            }

            products
        }
    }
}