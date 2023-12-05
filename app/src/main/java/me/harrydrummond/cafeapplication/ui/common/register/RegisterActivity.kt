package me.harrydrummond.cafeapplication.ui.common.register

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
import me.harrydrummond.cafeapplication.databinding.ActivityRegisterBinding
import me.harrydrummond.cafeapplication.ui.AdminAppActivity
import me.harrydrummond.cafeapplication.ui.common.login.LoginViewModel
import me.harrydrummond.cafeapplication.ui.common.profile.CreateProfileActivity


/**
 * RegisterActivity class.
 * This is the View for the MVVM pattern. Sends events to the RegisterViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see RegisterViewModel
 * @see ActivityRegisterBinding
 * @author Harry Drummond
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var accountType: Role

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        setSupportActionBar(binding.toolbar3)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // This is not this class' problem if it has not been supplied with an account type.
        // It is mandatory
        this.accountType = intent.getParcelableExtra(IntentExtra.ACCOUNT_TYPE, Role::class.java)!!

        handleUIState()
    }

    /**
     * Event handler for the register button.
     * Validates the user inputs, and marks the fields as errors if they have any.
     *
     * @param view Button view
     */
    fun onRegisterClick(view: View) {
        val email = binding.registerEmail
        val pass = binding.registerPassword

        viewModel.register(email.text.toString(), pass.text.toString(), accountType)
    }

    private fun handleUIState() {
        viewModel.uiState.observe(this) { uiState ->
            binding.progressBar.isVisible = uiState.loading
            if (uiState.errorMessage != null) {
                Toast.makeText(this, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
            }
            if (uiState.event != null) {
                when (uiState.event) {
                    Event.UserWasRegistered -> {
                        when (accountType) {
                            Role.EMPLOYEE -> {
                                val intent = Intent(this, AdminAppActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                            else ->{
                                val intent = Intent(this, CreateProfileActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}