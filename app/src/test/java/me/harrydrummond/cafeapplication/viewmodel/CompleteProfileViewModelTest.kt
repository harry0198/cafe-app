package me.harrydrummond.cafeapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import me.harrydrummond.cafeapplication.mocks.FakeUserRepository
import me.harrydrummond.cafeapplication.rules.MainDispatcherRule
import me.harrydrummond.cafeapplication.ui.common.login.LoginViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CompleteProfileViewModelTest {
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


}