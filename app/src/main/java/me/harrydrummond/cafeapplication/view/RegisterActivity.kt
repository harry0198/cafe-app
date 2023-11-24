package me.harrydrummond.cafeapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.Validators
import me.harrydrummond.cafeapplication.databinding.ActivityLoginBinding
import me.harrydrummond.cafeapplication.databinding.ActivityRegisterBinding
import me.harrydrummond.cafeapplication.model.Role
import me.harrydrummond.cafeapplication.viewmodel.LoginViewModel
import me.harrydrummond.cafeapplication.viewmodel.RegisterAction
import me.harrydrummond.cafeapplication.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        val isEmployee = intent.getBooleanExtra("IS_EMPLOYEE", false)
        registerViewModel.setUserLoginType(if (isEmployee) Role.EMPLOYEE else Role.CUSTOMER)

        setSupportActionBar(binding.toolbar3)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.toolbar_arrow_back)

        registerViewModel.progress.observe(this) { progress ->
            when (progress) {
                RegisterAction.REGISTER_SUCCESS -> {
                    Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()

                    startActivity(
                        Intent(
                            this,
                            BrowseMenuView::class.java
                        )
                    )
                }
                RegisterAction.EMAIL_IN_USE -> {
                    binding.registerEmail.error = "This email already has an account"
                }
                RegisterAction.FAILURE -> Toast.makeText(this, "An unknown error occurred", Toast.LENGTH_SHORT).show()
                else -> null
            }
        }
    }

    fun onRegisterClick(view: View) {
        val email = binding.registerEmail
        val pass = binding.registerPassword

        if (!Validators.validateEmail(email.text.toString())) {
            email.error = "Please enter a valid email"
        }
        if (pass.text.toString().isBlank()) {
            binding.registerPassword.error = "Please enter a password"
        }

        if (binding.registerEmail.error == null && binding.registerPassword.error == null) {
            registerViewModel.register(email.text.toString(), pass.text.toString(), "", 4)
        }
    }
}