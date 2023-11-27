package me.harrydrummond.cafeapplication.ui.common.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.Validators
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.databinding.ActivityLoginBinding
import me.harrydrummond.cafeapplication.ui.AdminAppActivity
import me.harrydrummond.cafeapplication.ui.AppActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.toolbar_arrow_back)

        loginViewModel.loginState.observe(this) { progress ->
            when (progress) {
                LoginAction.LOGIN_SUCCESS -> {
                    loginViewModel.getUser().addOnCompleteListener { task ->
                        binding.progressBar2.isVisible = false
                        if (task.isSuccessful) {
                            val userModel = task.result
                            val intent: Intent = if (userModel?.role == Role.EMPLOYEE) {
                                Intent(this, AdminAppActivity::class.java)
                            } else {
                                Intent(this, AppActivity::class.java)
                            }

                            Toast.makeText(this, "Successfully Logged-in", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                            finishAffinity()
                        }
                    }
                }
                LoginAction.EMAIL_OR_PASS_INVALID -> {
                    Toast.makeText(this, "Invalid email and password combination.", Toast.LENGTH_SHORT).show()
                    binding.progressBar2.isVisible = false
                }
                LoginAction.PROGRESS -> {
                    binding.progressBar2.isVisible = true
                }
                else -> {
                    binding.progressBar2.isVisible = false
                }
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