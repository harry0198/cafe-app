package me.harrydrummond.cafeapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.harrydrummond.cafeapplication.data.model.Cart
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IPaymentRepository
import me.harrydrummond.cafeapplication.mocks.FakeOrderRepository
import me.harrydrummond.cafeapplication.mocks.FakePaymentRepository
import me.harrydrummond.cafeapplication.rules.MainDispatcherRule
import me.harrydrummond.cafeapplication.ui.customer.cart.CartViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Tests for [CartViewModel] class. Ensures the functions
 * produce the expected behaviour.
 */
class CartViewModelTest {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var paymentRepository: IPaymentRepository
    private lateinit var orderRepository: IOrderRepository

    // Rules to allow testing
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Initializes the variables before each test
    @Before
    fun setUp() {
        // Log user out before each test & clear their cart
        Cart.getInstance().clear()
        AuthenticatedUser.getInstance().logout()
        paymentRepository = FakePaymentRepository()
        orderRepository = FakeOrderRepository()
        cartViewModel = CartViewModel(orderRepository, paymentRepository)
    }

    /**
     * Tests that the [CartViewModel.refreshCart] function does refresh a cart properly
     */
    @Test
    fun `Test refresh cart does refresh`() {
        // Arrange
        // add items to cart
        val product1 = Product(1)
        val product2 = Product(2)
        Cart.getInstance().addProduct(product1, 1)
        Cart.getInstance().addProduct(product2, 2)

        // Act
        cartViewModel.refreshCart()
        cartViewModel.uiState.observeForever {  }

        // Assert
        val cartProducts = cartViewModel.uiState.value!!.cartProducts
        TestCase.assertTrue(checkContainsProductAndQuantity(cartProducts, product1, 1))
        TestCase.assertTrue(checkContainsProductAndQuantity(cartProducts, product2, 2))
    }

    /**
     * Tests that the [CartViewModel.updateQuantity] does update the quantity of the given product
     * and does not update any other cart items.
     */
    @Test
    fun `Test updateQuantity does update quantity`() {
        // Arrange
        // add items to cart
        val product1 = Product(1)
        val product2 = Product(2)
        Cart.getInstance().addProduct(product1, 1)
        Cart.getInstance().addProduct(product2, 2)

        // Act
        cartViewModel.updateQuantity(product1, 5)
        cartViewModel.uiState.observeForever {  }

        // Assert
        val cartProducts = cartViewModel.uiState.value!!.cartProducts
        TestCase.assertTrue(checkContainsProductAndQuantity(cartProducts, product1, 5))
        TestCase.assertTrue(checkContainsProductAndQuantity(cartProducts, product2, 2))
    }

    /**
     * Tests that the [CartViewModel.getTotalCost] does fetch the total cost of the cart
     * even with multiple quantities.
     */
    @Test
    fun `Test getTotalCost does get total cost`() {
        // Arrange
        // add items to cart
        val price1 = 10.0
        val price2 = 15.0
        val expectedPrice = 40.0
        val product1 = Product(1, productPrice = price1)
        val product2 = Product(2, productPrice = price2)
        Cart.getInstance().addProduct(product1, 1)
        Cart.getInstance().addProduct(product2, 2)

        // Act
        val totalCost = cartViewModel.getTotalCost()

        // Assert
        TestCase.assertEquals(expectedPrice, totalCost)
    }

    /**
     * Tests that the [CartViewModel.placeOrder] function correctly places an order
     * with the correct products.
     */
    @Test
    fun `Test place order does place order`(){
        // Arrange
        // add items to cart
        val product1 = Product(1)
        val product2 = Product(2)
        Cart.getInstance().addProduct(product1, 1)
        Cart.getInstance().addProduct(product2, 2)

        // Act
        cartViewModel.placeOrder("", "", "") // Inputs are irrelevant, we do not store them anywhere here.
        cartViewModel.uiState.observeForever {  }

        val orders = orderRepository.getOrdersByUserId(AuthenticatedUser.getInstance().getUserId())

        // Assert
        TestCase.assertTrue(orders.isNotEmpty())
        TestCase.assertEquals(1, orders.size)

        val order = orders[0]
        val cartItems = order.products

        TestCase.assertEquals(3, cartItems.size)
        TestCase.assertTrue(cartItems.contains(product1))
        TestCase.assertTrue(cartItems.contains(product2))
    }

    /**
     * Tests that the [CartViewModel.placeOrder] functino does not place an order when there are no
     * items in the cart.
     */
    @Test
    fun `Test place order does not place order if empty`() {
        // Arrange

        // Act
        cartViewModel.placeOrder("", "", "") // Inputs are irrelevant, we do not store them anywhere here.
        cartViewModel.uiState.observeForever {  }

        val orders = orderRepository.getOrdersByUserId(AuthenticatedUser.getInstance().getUserId())

        // Assert
        TestCase.assertTrue(orders.isEmpty())
    }

    private fun checkContainsProductAndQuantity(cartProducts: List<Pair<Int, Product>>, product: Product, quantity: Int): Boolean {
        return cartProducts.find { it.first == quantity && it.second.productId == product.productId } != null
    }

}
