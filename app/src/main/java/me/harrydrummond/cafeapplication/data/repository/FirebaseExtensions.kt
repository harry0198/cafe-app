package me.harrydrummond.cafeapplication.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.ProductQuantity
import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.model.UserReview

/**
 * Maps a DocumentSnapshot to a ProductModel
 *
 * @see DocumentSnapshot
 * @see ProductQuantity
 */
fun DocumentSnapshot.toProductModel(): Product? {
    return this.toObject(Product::class.java)
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

/**
 * Maps a document snapshot to a user review
 *
 * @see DocumentSnapshot
 * @see UserReview
 */
fun DocumentSnapshot.toUserReview(): UserReview? {
    return this.toObject(UserReview::class.java)
}

/**
 * Maps a document snapshot to a review
 *
 * @see DocumentSnapshot
 * @see Review
 */
fun DocumentSnapshot.toReview(): Review? {
    return this.toObject(Review::class.java)
}