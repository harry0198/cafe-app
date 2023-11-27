package me.harrydrummond.cafeapplication.ui.customer.menu.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.repository.UserRepository
import me.harrydrummond.cafeapplication.ui.State

class ProductViewModel: ViewModel() {

    private lateinit var product: ProductModel
    private val userRepository = UserRepository()
    private val quantity: MutableLiveData<Int> = MutableLiveData(1)
    val saveState = MutableLiveData(State.NONE)

    fun initialize(productModel: ProductModel) {
        this.product = productModel
    }

    fun addToCart() {
        userRepository.partialLoadUserCart { cart ->
            saveState.value = State.SUCCESS
            if (cart != null) {
                cart.addToCart(product.productId, quantity.value?: 0)
                userRepository.saveUserCart(cart).addOnCompleteListener {
                    if (it.isSuccessful) {
                        saveState.value = State.SUCCESS
                    } else {
                        saveState.value = State.FAILURE
                    }
                }
            }
        }
    }
}