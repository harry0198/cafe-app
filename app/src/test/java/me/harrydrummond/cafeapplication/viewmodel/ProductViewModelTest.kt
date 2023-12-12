package me.harrydrummond.cafeapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.harrydrummond.cafeapplication.data.model.Cart
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.rules.MainDispatcherRule
import me.harrydrummond.cafeapplication.ui.customer.menu.product.ProductViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Tests the product view model business logic works as expected
 *
 * @see ProductViewModel
 */
@RunWith(JUnit4::class)
class ProductViewModelTest {

    private lateinit var productViewModel: ProductViewModel


    // Rules to allow testing
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Initializes the variables before each test
    @Before
    fun setUp() {
        productViewModel = ProductViewModel()
    }

    @After
    fun after() {
        // Log user out after each test.
        AuthenticatedUser.getInstance().logout()
    }

    /**
     * Tests that the [ProductViewModel.incrementQuantity] function increments properly
     */
    @Test
    fun `Increment quantity does increment`() {
        // Arrange
        val expectedQuantity = 2

        // Act
        productViewModel.incrementQuantity()
        productViewModel.uiState.observeForever {}

        // Assert
        val uiState = productViewModel.uiState.value!!
        TestCase.assertEquals(expectedQuantity, uiState.quantity)
    }

    /**
     * Tests that the [ProductViewModel.decrementQuantity] function does not decrement if the
     * current quantity is 1.
     */
    @Test
    fun `decrement quantity does not decrement below 1`() {
        // Arrange
        val expectedQuantity = 1

        // Act
        productViewModel.decrementQuantity()
        productViewModel.uiState.observeForever {  }

        // Assert
        val uiState = productViewModel.uiState.value!!
        TestCase.assertEquals(expectedQuantity, uiState.quantity)
    }

    /**
     * Tests that the [ProductViewModel.decrementQuantity] function does decrement if the current
     * quantity is above 1
     */
    @Test
    fun `decrement quantity does decrement if above 1`() {
        // Arrange
        val expectedQuantity = 2

        // Act
        // increment twicee
        productViewModel.incrementQuantity()
        productViewModel.incrementQuantity()
        // Decrement once
        productViewModel.decrementQuantity()
        productViewModel.uiState.observeForever {  }

        // Assert
        val uiState = productViewModel.uiState.value!!
        TestCase.assertEquals(expectedQuantity, uiState.quantity)
    }

    /**
     * Tests that the [ProductViewModel.initialize] function does initialize the given product
     * properly.
     */
    @Test
    fun `Initialize does initialize product`() {
        // Arrange
        val product = Product(1)

        // Act
        productViewModel.initialize(product)

        // Assert
        TestCase.assertEquals(product.productId, productViewModel.product.productId)
    }

    /**
     * Tests that the [ProductViewModel.addToCart] does add to cart and the correct amount is
     * added to the cart.
     */
    @Test
    fun `Add to cart does add correct quantity to cart`() {
        // Arrange
        val expectedNumProducts = 2
        val product = Product(1)
        productViewModel.initialize(product)

        // Act
        productViewModel.incrementQuantity()
        productViewModel.addToCart()

        // Assert
        val cart = Cart.getInstance()
        TestCase.assertEquals(expectedNumProducts, cart.getProducts().size)
        TestCase.assertEquals(cart.getProducts()[0].productId, product.productId)
        TestCase.assertEquals(cart.getProducts()[1].productId, product.productId)

    }
}