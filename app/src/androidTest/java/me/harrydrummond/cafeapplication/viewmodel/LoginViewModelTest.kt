package me.harrydrummond.cafeapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import me.harrydrummond.cafeapplication.ui.common.login.Event
import me.harrydrummond.cafeapplication.ui.common.login.LoginViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests the functions within the loginviewmodel function correctly
 *
 * @see LoginViewModel
 */
//@RunWith(AndroidJUnit4::class)
//class LoginViewModelTest {

//
//
//    @get:Rule // <----
//    var instantExecutorRule = InstantTaskExecutorRule()
//
//    // Initializes the variables before each test
//    @Before
//    fun setUp() {
//        userRepository = FakeUserRepository()
//        loginViewModel = LoginViewModel(userRepository)
//    }
//
//    @Test
//    fun testLoginEmailPasswordCustomerValid() {
//        // Arrange
//        val email = "harry@home.com"
//        val password = "password"
//        // Register new user account
//        userRepository.registerUser(email, password)
//        userRepository.saveUser(email, UserModel(role = Role.CUSTOMER))
//
//        // Act
//        loginViewModel.login(email, password)
//
//        loginViewModel.uiState.observeForever {}
//
//        // Assert
//        val uiState = loginViewModel.uiState.value!!
//
//        assertEquals(Event.GoToCustomerApp, uiState.event)
//    }
//
//    /**
//     * Tests that the email validation returns as invalid when
//     * an empty email is passed.
//     *
//     * @see LoginViewModel.login
//     */
//    @Test
//    fun testLoginEmailValidationEmptyInvalid() {
//        // Arrange
//        val email = ""
//        val password = "password"
//        val expectedIsValid = false
//
//        // Act
//        loginViewModel.login(email, password)
//        loginViewModel.uiState.observeForever {}
//
//        // Assert
//        val actualValidationObject = loginViewModel.uiState.value?.emailValidation
//
//        assertNotNull(actualValidationObject)
//        assertEquals(expectedIsValid, actualValidationObject!!.isValid)
//    }
//
//
//    /**
//     * Tests that the email validation returns as invalid when
//     * a malformed email is passed.
//     *
//     * @see LoginViewModel.login
//     */
//    @Test
//    fun testLoginEmailValidationMalformedInvalid() {
//        // Arrange
//        val email = "harry"
//        val password = "password"
//        val expectedIsValid = false
//
//        // Act
//        loginViewModel.login(email, password)
//        loginViewModel.uiState.observeForever {}
//
//        // Assert
//        val actualValidationObject = loginViewModel.uiState.value?.emailValidation
//
//        assertNotNull(actualValidationObject)
//        assertEquals(expectedIsValid, actualValidationObject!!.isValid)
//    }
//
//    /**
//     * Tests that the email validation returns as invalid when
//     * a malformed email is passed.
//     *
//     * @see LoginViewModel.login
//     */
//    @Test
//    fun testLoginPasswordValidationEmptyInvalid() {
//        // Arrange
//        val email = "harry@example.com"
//        val password = ""
//        val expectedIsValid = false
//
//        // Act
//        loginViewModel.login(email, password)
//        loginViewModel.uiState.observeForever {}
//
//        // Assert
//        val actualValidationObject = loginViewModel.uiState.value?.passwordValidation
//
//        assertNotNull(actualValidationObject)
//        assertEquals(expectedIsValid, actualValidationObject!!.isValid)
//    }
//}