package me.harrydrummond.cafeapplication.ui.customer.cart

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.Cart
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.ProductQuantity
import me.harrydrummond.cafeapplication.data.model.Status
import me.harrydrummond.cafeapplication.data.repository.OrderRepository
import me.harrydrummond.cafeapplication.data.repository.ProductRepository
import me.harrydrummond.cafeapplication.data.repository.UserRepository
import me.harrydrummond.cafeapplication.ui.State

class CartViewModel : ViewModel() {

    private val orderRepository = OrderRepository()
    private val userRepository = UserRepository()
    private var mCartList = listOf<Pair<Int, ProductModel>>()
    val cartItems: MutableLiveData<List<Pair<Int, ProductModel>>> = MutableLiveData(mCartList)
    val orderPlaceStatus: MutableLiveData<State> = MutableLiveData(State.NONE)

    fun refreshCart() {
        userRepository.partialLoadUserCart { cart ->
            if (cart != null) {
                userRepository.fullLoadUserCart(cart) { list ->
                    mCartList = list
                    cartItems.value = mCartList
                }
            }
        }
    }

    fun updateQuantity(productId: String, quantity: Int) {
        userRepository.partialLoadUserCart { cart ->
            if (cart != null) {
                cart.updateCartItem(productId, quantity)
                userRepository.saveUserCart(cart).addOnCompleteListener { refreshCart() }
            }
        }
    }

    fun placeOrder() {
        orderPlaceStatus.value = State.PROCESSING
        val mappedProductQuantity = cartItems.value?.map { item ->
            ProductQuantity(item.second.productId, item.first)
        }
        val userId = Firebase.auth.currentUser?.uid

        if (mappedProductQuantity.isNullOrEmpty() || userId == null) {
            return
        }

        val order = Order(
            products = mappedProductQuantity,
            status = Status.NONE,
            userId = userId
        )

        orderRepository.saveOrder(order).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                orderPlaceStatus.value = State.SUCCESS
                clearCart()
            } else {
                orderPlaceStatus.value = State.FAILURE
            }
        }
    }

    private fun clearCart() {
        userRepository.saveUserCart(Cart(mutableListOf()))
        refreshCart()
    }

}