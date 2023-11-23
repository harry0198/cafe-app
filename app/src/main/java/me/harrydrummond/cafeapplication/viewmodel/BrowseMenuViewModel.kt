package me.harrydrummond.cafeapplication.viewmodel

import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.model.ProductModel

class BrowseMenuViewModel: ViewModel() {
    fun getProducts(): Array<ProductModel> {
        val product = ProductModel("Flat White", 3.25, "", "A coffee", true)
        val product2 = ProductModel("Cappuccino", 3.49, "", "A coffee", true)

        return arrayOf(product, product2)
    }
}