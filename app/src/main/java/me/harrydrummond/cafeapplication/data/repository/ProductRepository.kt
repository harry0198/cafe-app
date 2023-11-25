package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.ProductModel
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

    fun getProductById(id: String): Task<ProductModel> {
        val document = db.collection(DOCUMENT_NAME).document(id)
        return document.get().continueWith { task: Task<DocumentSnapshot> ->
            if (task.isSuccessful) {
                val documentSnapshot = task.result
                val product = documentSnapshot?.toObject(ProductModel::class.java)
                product?.productId = document.id
                product
            } else {
                null
            }
        }
    }

}