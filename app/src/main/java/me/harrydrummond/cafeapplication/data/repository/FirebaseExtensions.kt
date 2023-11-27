package me.harrydrummond.cafeapplication.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.ProductQuantity

fun DocumentSnapshot.toProductModel(): ProductModel? {
    return this.toObject(ProductModel::class.java)
}

fun DocumentSnapshot.toCartProduct(): ProductQuantity? {
    return this.toObject(ProductQuantity::class.java)
}