package me.harrydrummond.cafeapplication.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.ProductQuantity

/**
 * Maps a DocumentSnapshot to a ProductModel
 *
 * @see DocumentSnapshot
 * @see ProductQuantity
 */
fun DocumentSnapshot.toProductModel(): ProductModel? {
    return this.toObject(ProductModel::class.java)
}

/**
 * Maps a Document Snapshot to a ProductQuantity
 *
 * @see DocumentSnapshot
 * @see ProductQuantity
 */
fun DocumentSnapshot.toProductQuantity(): ProductQuantity? {
    return this.toObject(ProductQuantity::class.java)
}