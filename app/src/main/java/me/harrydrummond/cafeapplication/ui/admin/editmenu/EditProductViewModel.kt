package me.harrydrummond.cafeapplication.ui.admin.editmenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.repository.ProductRepository
import me.harrydrummond.cafeapplication.ui.State

class EditProductViewModel: ViewModel() {

    private val repository: ProductRepository = ProductRepository()
    lateinit var productModel: ProductModel
    var uiProcessingState: MutableLiveData<State> = MutableLiveData(State.NONE)
    var productDeleted: MutableLiveData<Boolean> = MutableLiveData(false)

    fun init(productModel: ProductModel) {
        this.productModel = productModel
    }

    fun saveProduct(productName: String, productDesc: String, productPrice: Double, productAvailable: Boolean) {
        uiProcessingState.value = State.PROCESSING

        productModel.productName = productName
        productModel.productDescription = productDesc
        productModel.productPrice = productPrice
        productModel.productAvailable = productAvailable

        repository.saveProduct(productModel).addOnCompleteListener {
            if (it.isSuccessful) {
                uiProcessingState.value = State.SUCCESS
            } else {
                uiProcessingState.value = State.FAILURE
            }
        }
    }

    fun deleteProduct() {
        uiProcessingState.value = State.PROCESSING

        repository.deleteProduct(productModel).addOnCompleteListener {
            if (it.isSuccessful) {
                uiProcessingState.value = State.SUCCESS
                productDeleted.value = true
            } else {
                uiProcessingState.value = State.FAILURE
            }
        }
    }
}