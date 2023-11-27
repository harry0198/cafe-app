package me.harrydrummond.cafeapplication.ui.admin.editmenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.repository.ProductRepository

class EditMenuViewModel : ViewModel() {

    private val productRepository: ProductRepository = ProductRepository()
    private var mProductList = listOf<ProductModel>()
    val productList: MutableLiveData<List<ProductModel>> = MutableLiveData(mProductList)

    fun refreshProducts(){
        productRepository.getAllProducts().continueWith { task ->
            if (task.isSuccessful) {
                mProductList = task.result
                productList.value = mProductList
            }
        }
    }

    fun addProduct() {
        val dummyName = "New Product"
        val dummyDescription = "A new product for our menu! Check back here later!"
        val dummyPrice = 3.15
        val dummyImage = ""
        val dummyAvailability = false

        val dummyProduct = ProductModel("",
            dummyName,
            dummyPrice,
            dummyImage,
            dummyDescription,
            dummyAvailability
        )
        productRepository.saveProduct(dummyProduct).addOnCompleteListener {task ->
            if (task.isSuccessful) {
                refreshProducts()
            }
        }
    }
}