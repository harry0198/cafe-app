package me.harrydrummond.cafeapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import me.harrydrummond.cafeapplication.mocks.FakeUserRepository
import me.harrydrummond.cafeapplication.rules.MainDispatcherRule
import me.harrydrummond.cafeapplication.ui.common.login.Event
import me.harrydrummond.cafeapplication.ui.common.login.LoginViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Tests the functions within the [LoginViewModel] function correctly
 */
@RunWith(JUnit4::class)
class LoginViewModelTest {

    private lateinit var customerRepository: IUserRepository<Customer>
    private lateinit var employeeRepository: IUserRepository<Employee>
    private lateinit var loginViewModel: LoginViewModel


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
        loginViewModel = LoginViewModel(customerRepository, employeeRepository)
    }

    @After
    fun after() {
        // Log user out after each test.
        AuthenticatedUser.getInstance().logout()
    }

    /**
     * Tests the [LoginViewModel.login] function authenticates the customer user and
     * posts the event to the ui state.
     */
    @Test
    fun `CUSTOMER Test login with correct username and password, Authenticates user and Posts Event`() {
        // Arrange
        val username = "harry"
        val password = "password"
        // Register new user account
        val customer = Customer(-1, null, null, null, username, password, true)
        val savedId = customerRepository.save(customer)

        // Act
        loginViewModel.login(username, password, Role.CUSTOMER)

        // Get current user by id
        val currentUser = customerRepository.getById(savedId)

        loginViewModel.uiState.observeForever {}

        // Assert
        val uiState = loginViewModel.uiState.value!!
        assertEquals(currentUser?.id, AuthenticatedUser.getInstance().getUserId())
        assertEquals(Event.GoToCustomerApp, uiState.event)
    }

    /**
     * Tests the [LoginViewModel.login] function does not authenticate existing user but
     * disabled account
     */
    @Test
    fun `CUSTOMER Test login with correct username and password not active, Does not Authenticate`() {
        // Arrange
        val username = "harry"
        val password = "password"
        // Register new user account
        val customer = Customer(-1, null, null, null, username, password, false)
        customerRepository.save(customer)

        // Act
        loginViewModel.login(username, password, Role.CUSTOMER)

        // Assert
        assertEquals(-1, AuthenticatedUser.getInstance().getUserId())
    }

    /**
     * Tests that when a username is passed as empty in [LoginViewModel.login] then the user
     * is not logged in and a validation object is posted to the ui state.
     */
    @Test
    fun `CUSTOMER Test login with empty username, Does not Authenticate and Post Event`() {
        // Arrange
        val username = ""
        val password = "password"
        val expectedIsValid = false

        // Act
        loginViewModel.login(username, password, Role.CUSTOMER)
        loginViewModel.uiState.observeForever {}

        // Assert
        val actualValidationObject = loginViewModel.uiState.value?.usernameValidation

        assertEquals(AuthenticatedUser.getInstance().getUserId(), -1) // Assert no user is logged in.
        assertNotNull(actualValidationObject)
        assertEquals(expectedIsValid, actualValidationObject!!.isValid)
    }


    /**
     * Tests that the password validation returns as invalid when
     * an empty password is passed [LoginViewModel.login].
     *
     * @see LoginViewModel.login
     */
    @Test
    fun `CUSTOMER Test login with empty password, Does not Authenticate and Post Event`() {
        // Arrange
        val username = "harry"
        val password = ""
        val expectedIsValid = false

        // Act
        loginViewModel.login(username, password, Role.CUSTOMER)
        loginViewModel.uiState.observeForever {}

        // Assert
        val actualValidationObject = loginViewModel.uiState.value?.passwordValidation

        assertNotNull(actualValidationObject)
        assertEquals(expectedIsValid, actualValidationObject!!.isValid)
    }

    /**
     * Tests the [LoginViewModel.login] function authenticates the employee user and
     * posts the event to the ui state.
     */
    @Test
    fun `EMPLOYEE Test login with correct username and password, Authenticates user and Posts Event`() {
        // Arrange
        val username = "harry"
        val password = "password"
        // Register new user account
        val employee = Employee(-1, null, null, null, username, password, true)
        val savedId = employeeRepository.save(employee)

        // Act
        loginViewModel.login(username, password, Role.EMPLOYEE)

        // Get current user by id
        val currentUser = employeeRepository.getById(savedId)

        loginViewModel.uiState.observeForever {}

        // Assert
        val uiState = loginViewModel.uiState.value!!
        assertEquals(currentUser?.id, AuthenticatedUser.getInstance().getUserId())
        assertEquals(Event.GoToAdminApp, uiState.event)
    }

    /**
     * Tests the [LoginViewModel.login] function does not authenticate existing user but
     * disabled account
     */
    @Test
    fun `EMPLOYEE Test login with correct username and password not active, Does not Authenticate`() {
        // Arrange
        val username = "harry"
        val password = "password"
        // Register new user account
        val employee = Employee(-1, null, null, null, username, password, false)
        employeeRepository.save(employee)

        // Act
        loginViewModel.login(username, password, Role.EMPLOYEE)

        // Assert
        assertEquals(-1, AuthenticatedUser.getInstance().getUserId())
    }

    /**
     * Tests that when a username is passed as empty in [LoginViewModel.login] then the user
     * is not logged in and a validation object is posted to the ui state.
     */
    @Test
    fun `EMPLOYEE Test login with empty username, Does not Authenticate and Post Event`() {
        // Arrange
        val username = ""
        val password = "password"
        val expectedIsValid = false

        // Act
        loginViewModel.login(username, password, Role.EMPLOYEE)
        loginViewModel.uiState.observeForever {}

        // Assert
        val actualValidationObject = loginViewModel.uiState.value?.usernameValidation

        assertEquals(AuthenticatedUser.getInstance().getUserId(), -1) // Assert no user is logged in.
        assertNotNull(actualValidationObject)
        assertEquals(expectedIsValid, actualValidationObject!!.isValid)
    }


    /**
     * Tests that the password validation returns as invalid when
     * an empty password is passed [LoginViewModel.login].
     *
     * @see LoginViewModel.login
     */
    @Test
    fun `EMPLOYEE Test login with empty password, Does not Authenticate and Post Event`() {
        // Arrange
        val username = "harry"
        val password = ""
        val expectedIsValid = false

        // Act
        loginViewModel.login(username, password, Role.EMPLOYEE)
        loginViewModel.uiState.observeForever {}

        // Assert
        val actualValidationObject = loginViewModel.uiState.value?.passwordValidation

        assertNotNull(actualValidationObject)
        assertEquals(expectedIsValid, actualValidationObject!!.isValid)
    }
}