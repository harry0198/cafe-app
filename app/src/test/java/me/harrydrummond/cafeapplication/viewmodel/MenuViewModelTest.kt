package me.harrydrummond.cafeapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.mocks.FakeProductRepository
import me.harrydrummond.cafeapplication.rules.MainDispatcherRule
import me.harrydrummond.cafeapplication.ui.customer.menu.MenuViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


/**
 * Tests the menu view model business logic works as expected
 *
 * @see MenuViewModel
 */
@RunWith(JUnit4::class)
class MenuViewModelTest {

    private lateinit var menuViewModel: MenuViewModel
    private lateinit var productRepository: IProductRepository


    // Rules to allow testing
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Initializes the variables before each test
    @Before
    fun setUp() {
        productRepository = FakeProductRepository()
        menuViewModel = MenuViewModel(productRepository)
    }

    @After
    fun after() {
        // Log user out after each test.
        AuthenticatedUser.getInstance().logout()
    }

    /**
     * Tests that the [MenuViewModel.refreshProducts] function does only refresh the available products
     * found in the product repository.
     */
    @Test
    fun `Refresh products does refresh available products only`() {
        // Arrange
        var product1 = Product(1, productAvailable = true)
        var product2 = Product(2, productAvailable = true)
        val product3 = Product(3, productAvailable = false)
        val product1Id = productRepository.save(product1)
        val product2Id = productRepository.save(product2)
        productRepository.save(product3)

        product1 = product1.copy(productId = product1Id)
        product2 = product2.copy(productId = product2Id)

        val expectedProducts= listOf(product1, product2)

        // Act
        menuViewModel.refreshProducts()
        menuViewModel.uiState.observeForever {  }

        // Assert
        val uiState = menuViewModel.uiState.value!!
        TestCase.assertEquals(expectedProducts.size, uiState.products.size)
        TestCase.assertEquals(expectedProducts, uiState.products)
    }
}