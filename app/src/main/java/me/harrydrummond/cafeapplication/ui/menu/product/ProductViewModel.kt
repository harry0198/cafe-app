package me.harrydrummond.cafeapplication.ui.menu.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.repository.ProductRepository

class ProductViewModel: ViewModel() {

    private val productRepository: ProductRepository = ProductRepository()
    private lateinit var product: ProductModel
    val productName: MutableLiveData<String> = MutableLiveData()
    val productDesc: MutableLiveData<String> = MutableLiveData()
    val productPrice: MutableLiveData<Double> = MutableLiveData()
    val productAvailability: MutableLiveData<Boolean> = MutableLiveData()
    val deleteProductComplete: MutableLiveData<Boolean> = MutableLiveData(false)
    val savedProductState: MutableLiveData<Boolean> = MutableLiveData(false)

    fun initialize(productModel: ProductModel) {
        this.product = productModel
        this.productName.value = product.productName
        this.productDesc.value = product.productDescription
        this.productPrice.value = product.productPrice
        this.productAvailability.value = product.productAvailable
    }

    fun deleteProduct() {
        productRepository.deleteProduct(product).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                deleteProductComplete.value = true
            }

            deleteProductComplete.value = false
        }
    }

    fun saveProductName(name: String) {
        product.productName = name
        productName.value = name

        productRepository.saveProduct(product).addOnCompleteListener { task ->
            savedProductState.value = task.isSuccessful
        }
    }

    fun saveProductDescription(desc: String) {
        product.productDescription = desc
        productDesc.value = desc

        productRepository.saveProduct(product).addOnCompleteListener { task ->
            savedProductState.value = task.isSuccessful
        }
    }

    fun saveProductAvailability(availability: Boolean) {
        product.productAvailable = availability
        productAvailability.value = availability

        productRepository.saveProduct(product).addOnCompleteListener { task ->
            savedProductState.value = task.isSuccessful
        }
    }

    fun saveProductPrice(price: Double) {
        product.productPrice = price
        productPrice.value = price

        productRepository.saveProduct(product).addOnCompleteListener { task ->
            savedProductState.value = task.isSuccessful
        }
    }
}