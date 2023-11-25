package me.harrydrummond.cafeapplication.ui.menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.ProductRepository
import me.harrydrummond.cafeapplication.model.ProductModel

class MenuViewModel(application: Application) : AndroidViewModel(application) {
    private val productRepository: ProductRepository = ProductRepository(application)
    val productList: MutableLiveData<MutableList<ProductModel>> = MutableLiveData(productRepository.getProducts())

    val uiButtonState: LiveData<ButtonUIState> = MutableLiveData(
        if (AuthenticatedUser.getInstance().isCustomer())
            ButtonUIState.INVISIBLE
        else
            ButtonUIState.VISIBLE
    )

    fun refreshProducts() {
        productList.value?.clear()
        productList.value?.addAll(productRepository.getProducts())
    }

    fun addProduct(): Long {
        val dummyName = "New Product"
        val dummyDescription = "A new product for our menu! Check back here later!"
        val dummyPrice = 3.15
        val dummyImage = ""
        val dummyAvailability = false

        val dummyProduct = ProductModel(-1, dummyName, dummyPrice, dummyImage, dummyDescription, dummyAvailability)
        val productId = productRepository.addProduct(dummyProduct)

        if (productId == -1L) {
            // Error
            return -1L
        }

        val product = productRepository.getProductById(productId) ?: return -1L
        productList.value?.add(product)
        return productId
    }
}

enum class ButtonUIState {
    VISIBLE,
    INVISIBLE
}