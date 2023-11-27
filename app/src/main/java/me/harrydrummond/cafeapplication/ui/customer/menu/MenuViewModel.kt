package me.harrydrummond.cafeapplication.ui.customer.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import me.harrydrummond.cafeapplication.data.repository.ProductRepository
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.Role

class MenuViewModel : ViewModel() {
    private val productRepository: ProductRepository = ProductRepository()
    private var mProductList = listOf<ProductModel>()
    val productList: MutableLiveData<List<ProductModel>> = MutableLiveData(mProductList)

    fun refreshProducts(){
        productRepository.getAllAvailableProducts().continueWith { task ->
            if (task.isSuccessful) {
                mProductList = task.result
                productList.value = mProductList
            }
        }
    }
}