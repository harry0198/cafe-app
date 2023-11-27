package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.ProductQuantity
import me.harrydrummond.cafeapplication.data.model.UserModel

class ProductRepository {

    companion object {
        const val DOCUMENT_NAME = "products"
    }

    private val db = Firebase.firestore

    fun saveProduct(product: ProductModel): Task<Void> {
        val document = if (product.productId == null || product.productId.isEmpty()) {
            db.collection(DOCUMENT_NAME).document()
        } else {
            db.collection(DOCUMENT_NAME).document(product.productId)
        }
        product.productId = document.id
        return document.set(product)
    }

    fun deleteProduct(product: ProductModel): Task<Void> {
        return db.collection(DOCUMENT_NAME).document(product.productId).delete()
    }

    fun getAllAvailableProducts(): Task<List<ProductModel>> {
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

    fun getProductsByQuantity(prodQuantities: List<ProductQuantity>, callback: (List<Pair<Int, ProductModel>>) -> Unit) {
        Tasks.whenAllSuccess<ProductModel>(
            prodQuantities.mapNotNull { prodQuantity ->
                getProductById(prodQuantity.productId)
            }
        ).addOnCompleteListener { getModelTask ->
            val productModels = getModelTask.result ?: emptyList()
            val resultPairs = prodQuantities.zip(productModels) { cartProduct, productModel ->
                Pair(cartProduct.quantity, productModel)
            }
            callback(resultPairs)
        }
    }

    fun getProductById(productId: String): Task<ProductModel?> {
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

    fun getAllProducts(): Task<List<ProductModel>> {
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