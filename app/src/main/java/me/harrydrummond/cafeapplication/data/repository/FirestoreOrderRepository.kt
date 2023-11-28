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

/**
 * OrderRepository class, is an implementation of the OrderRepository Interface using Firestore.
 * Contains functions to perform CRUD operations on orders.
 *
 * @see Firebase
 * @see FirestoreProductRepository
 * @see IOrderRepository
 */
class FirestoreOrderRepository(private val productRepository: IProductRepository) : IOrderRepository {

    companion object {
        /**
         * Name of the Document in the FireStore
         */
        const val DOCUMENT_NAME = "orders"
    }

    private val db = Firebase.firestore

    /**
     * @inheritDoc
     */
    override fun saveOrder(order: Order): Task<Void> {
        val document: DocumentReference = if (order.orderId.isEmpty()) {
            db.collection(DOCUMENT_NAME).document()
        } else {
            db.collection(DOCUMENT_NAME).document(order.orderId)
        }
        order.orderId = document.id
        return document.set(order)
    }

    /**
     * @inheritDoc
     */
    override fun getOrder(orderId: String): Task<Order?> {
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

    /**
     * @inheritDoc
     */
    override fun getOrders(): Task<List<Order>> {
        return db.collection(DOCUMENT_NAME).get().continueWith { result ->
            val orders = mutableListOf<Order>()
            for (document in result.result) {
                val order = document.toObject(Order::class.java)
                orders.add(order)
            }

            orders
        }
    }

    /**
     * @inheritDoc
     */
    override fun fullLoadOrderProducts(order: Order): Task<List<Pair<Int, ProductModel>>?> {
        return productRepository.getProductsByQuantity(order.products)
    }

    /**
     * @inheritDoc
     */
    override fun getOrdersByUser(): Task<List<Order>> {
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
}