package me.harrydrummond.cafeapplication.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.Validators
import me.harrydrummond.cafeapplication.databinding.ActivityRegisterBinding
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.ui.AppActivity

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
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.toolbar_arrow_back)

        registerViewModel.progress.observe(this) { progress ->
            when (progress) {
                RegisterAction.REGISTER_SUCCESS -> {

                    registerViewModel.getUser().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, AppActivity::class.java)
                            intent.putExtra("USER_MODEL", task.result)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Account heeree", Toast.LENGTH_SHORT).show()

                        }

                    }
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