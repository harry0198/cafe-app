package me.harrydrummond.cafeapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import me.harrydrummond.cafeapplication.mocks.FakeUserRepository
import me.harrydrummond.cafeapplication.rules.MainDispatcherRule
import me.harrydrummond.cafeapplication.ui.common.register.Event
import me.harrydrummond.cafeapplication.ui.common.register.RegisterViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Tests the functions within the [RegisterViewModel] function correctly
 */
@RunWith(JUnit4::class)
class RegisterViewModelTest {

    private lateinit var customerRepository: IUserRepository<Customer>
    private lateinit var employeeRepository: IUserRepository<Employee>
    private lateinit var registerViewModel: RegisterViewModel


    // Rules to allow testing
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Initializes the variables before each test
    @Before
    fun setUp() {
        customerRepository = FakeUserRepository()
        employeeRepository = FakeUserRepository()
        registerViewModel = RegisterViewModel(customerRepository, employeeRepository)
    }

    @After
    fun after() {
        // Log user out after each test.
        AuthenticatedUser.getInstance().logout()
    }

    /**
     * Tests that when a username is taken in the customer repository and a user tries to register
     * with the same username that the user is not authenticated and an error message is posted.
     * [RegisterViewModel.register]
     */
    @Test
    fun `CUSTOMER test username taken, error message UI State`() {
        val username = "harry"
        val password = "Example123"
        // Register new user account
        val customer = Customer(-1, null, null, null, username, password, true)

        // Save user to the repository
        customerRepository.save(customer)

        // Act
        registerViewModel.register(username, password, Role.CUSTOMER)
        registerViewModel.uiState.observeForever {}

        // Assert
        val uiState = registerViewModel.uiState.value!!
        TestCase.assertEquals(-1, AuthenticatedUser.getInstance().getUserId()) // User is not authenticated
        TestCase.assertNotNull(uiState.errorMessage) // Error message shoudl be shown
        TestCase.assertEquals(null, uiState.event) // No event posted
    }

    /**
     * Tests that when a username is taken in the customer repository and a user tries to register
     * with the same username that the user is not authenticated and an error message is posted.
     * [RegisterViewModel.register]
     */
    @Test
    fun `EMPLOYEE test username taken, error message UI State`() {
        val username = "harry"
        val password = "Example123"
        // Register new user account
        val employee = Employee(-1, null, null, null, username, password, true)

        // Save user to the repository
        employeeRepository.save(employee)

        // Act
        registerViewModel.register(username, password, Role.EMPLOYEE)
        registerViewModel.uiState.observeForever {}

        // Assert
        val uiState = registerViewModel.uiState.value!!
        TestCase.assertEquals(-1, AuthenticatedUser.getInstance().getUserId()) // User is not authenticated
        TestCase.assertNotNull(uiState.errorMessage) // Error message shoudl be shown
        TestCase.assertEquals(null, uiState.event) // No event posted
    }

    /**
     * Tests that when a username and password are valid, that the user is authenticated and the ui state
     * is updated.
     * [RegisterViewModel.register]
     */
    @Test
    fun `CUSTOMER test valid username and password, Authenticates and Updates UI State`() {
        testValidUsernamePasswordRegisters(Role.CUSTOMER)
    }

    /**
     * Tests that when a username and password are valid, that the user is authenticated and the ui state
     * is updated.
     * [RegisterViewModel.register]
     */
    @Test
    fun `EMPLOYEE test valid username and password, Authenticates and Updates UI State`() {
        testValidUsernamePasswordRegisters(Role.EMPLOYEE)
    }

    private fun testValidUsernamePasswordRegisters(role: Role) {
        val username = "harry"
        val password = "Example123"

        // Act
        registerViewModel.register(username, password, role)
        registerViewModel.uiState.observeForever {}

        // Assert
        val uiState = registerViewModel.uiState.value!!
        TestCase.assertTrue(AuthenticatedUser.getInstance().getUserId() > -1) // User is authenticated
        TestCase.assertEquals(Event.UserWasRegistered, uiState.event) // User was registered event
    }
}