package me.harrydrummond.cafeapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import me.harrydrummond.cafeapplication.data.repository.ProductRepository

class ProductViewModel(application: Application): AndroidViewModel(application) {

    private val productRepository: ProductRepository = ProductRepository(application)
    private var productId: Long? = null
    val productName: MutableLiveData<String> = MutableLiveData()
    val productDesc: MutableLiveData<String> = MutableLiveData()
    val productPrice: MutableLiveData<Double> = MutableLiveData()


    fun initialize(productId: Long) {
        val product = productRepository.getProductById(productId)
        this.productId = productId
        this.productName.value = product?.productName
        this.productDesc.value = product?.productDescription
        this.productPrice.value = product?.productPrice
    }

    fun saveProductName(name: String) {
        productName.value = name
        productRepository.updateProductName(productId!!, name)
    }

    fun saveProductDescription(desc: String) {
        productDesc.value = desc
        productRepository.updateProductDescription(productId!!, desc)
    }

    fun saveProductPrice(price: Double) {
        productPrice.value = price
    }
}