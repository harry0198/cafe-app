package me.harrydrummond.cafeapplication.ui.common.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.Validators
import me.harrydrummond.cafeapplication.databinding.ActivityRegisterBinding
import me.harrydrummond.cafeapplication.ui.common.register.profile.CreateProfileActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        setSupportActionBar(binding.toolbar3)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        registerViewModel.progress.observe(this) { progress ->
            when (progress) {
                RegisterAction.REGISTER_SUCCESS -> {
                    Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CreateProfileActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                RegisterAction.PROGRESS -> {
                    binding.progressBar.isVisible = true
                }
                RegisterAction.FAILURE -> {
                    Toast.makeText(this, "An unknown error occurred", Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }

                else -> {
                    binding.progressBar.isVisible = false
                }
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
            registerViewModel.register(email.text.toString(), pass.text.toString())
        }
    }
}