package me.harrydrummond.cafeapplication.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.Validators
import me.harrydrummond.cafeapplication.databinding.ActivityLoginBinding
import me.harrydrummond.cafeapplication.model.Role
import me.harrydrummond.cafeapplication.viewmodel.LoginAction
import me.harrydrummond.cafeapplication.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val isEmployee = intent.getBooleanExtra("IS_EMPLOYEE", false)
        loginViewModel.setUserLoginType(if (isEmployee) Role.EMPLOYEE else Role.CUSTOMER)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.toolbar_arrow_back)


        loginViewModel.loginState.observe(this) { progress ->

            when (progress) {
                LoginAction.LOGIN_SUCCESS -> {
                    Toast.makeText(this, "Successfully Logged-in", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, BrowseMenuView::class.java))
                }
                LoginAction.EMAIL_OR_PASS_INVALID -> {
                    binding.btnAuthenticate.isEnabled = true
                    Toast.makeText(this, "Invalid email and password combination.", Toast.LENGTH_SHORT).show()
                }
                LoginAction.PROGRESS -> {
                    binding.btnAuthenticate.isEnabled = false
                }
                else -> {}
            }
        }
    }

    fun onLoginClick(view: View) {
        val email = binding.registerEmail
        val pass = binding.registerPassword

        if (!Validators.validateEmail(email.text.toString())) {
            email.error = "Please enter a valid email"
        }
        if (pass.text.toString().isBlank()) {
            binding.registerPassword.error = "Please enter a password"
        }

        if (binding.registerEmail.error == null && binding.registerPassword.error == null) {
            loginViewModel.login(email.text.toString(), pass.text.toString())
        }
    }
    override fun onStart() {
        super.onStart()
    }
}