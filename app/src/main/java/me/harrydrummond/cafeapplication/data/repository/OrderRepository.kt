package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.ProductQuantity

class OrderRepository {

    companion object {
        const val DOCUMENT_NAME = "orders"
    }

    private val db = Firebase.firestore
    private val productRepository = ProductRepository()

    fun saveOrder(order: Order): Task<Void> {
        val document: DocumentReference = if (order.orderId.isEmpty()) {
            db.collection(DOCUMENT_NAME).document()
        } else {
            db.collection(DOCUMENT_NAME).document(order.orderId)
        }
        order.orderId = document.id
        return document.set(order)
    }

    fun getOrder(orderId: String): Task<Order?> {
        val document: DocumentReference = db.collection(DOCUMENT_NAME).document(orderId)
        return document.get().continueWith { task: Task<DocumentSnapshot> ->
            if (task.isSuccessful) {
                val documentSnapshot = task.result
                documentSnapshot?.toObject(Order::class.java)
            } else {
                null
            }
        }
    }

    fun getOrders(): Task<List<Order>> {
        return db.collection(DOCUMENT_NAME).get().continueWith { result ->
            val orders = mutableListOf<Order>()
            for (document in result.result) {
                val order = document.toObject(Order::class.java)
                orders.add(order)
            }

            orders
        }
    }

    fun fullLoadOrderProducts(order: Order, callback: (List<Pair<Int, ProductModel>>) -> Unit) {
        productRepository.getProductsByQuantity(order.products) {
            callback(it)
        }
    }

    fun getOrdersByUser(): Task<List<Order>> {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            val ordersCollection = db.collection(DOCUMENT_NAME)

            return ordersCollection.whereEqualTo("userId", userId).get().continueWith {
                if (it.isSuccessful) {
                    it.result.toObjects(Order::class.java).filterNotNull().toList()
                } else {
                    listOf()
                }
            }
        } else {
            return Tasks.forResult(listOf())
        }
    }

    fun onOrderUpdate(
        orderId: String,
        onUpdate: (Map<String, Any>?) -> Unit
    ) {
        val db = Firebase.firestore
        val documentRef: DocumentReference = db.collection(DOCUMENT_NAME).document(orderId)

        documentRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result

                    if (document != null && document.exists()) {
                        // Document exists, trigger the callback with the updated data
                        val data = document.data
                        onUpdate(data)

                        return@addOnCompleteListener
                    }
                }
                onUpdate(null)
            }
    }
}