package me.harrydrummond.cafeapplication.ui.common.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.Validators
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.databinding.ActivityLoginBinding
import me.harrydrummond.cafeapplication.ui.AdminAppActivity
import me.harrydrummond.cafeapplication.ui.AppActivity

/**
 * LoginActivity class.
 * This is the View for the MVVM pattern. Sends events to the LoginViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see LoginViewModel
 * @see ActivityLoginBinding
 * @author Harry Drummond
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        handleUIState()
    }

    /**
     * Event handler for when the login button is clicked.
     * This is defined as the event handler in the xml view.
     *
     * @param view View of button clicked.
     */
    fun onLoginClick(view: View) {

        val email = binding.registerEmail
        val pass = binding.registerPassword

        viewModel.login(email.text.toString(), pass.text.toString(), Role.CUSTOMER)
    }

    private fun handleUIState() {
        // Observe the ViewModel UIState data
        viewModel.uiState.observe(this) { uiState ->
            // Make progress bar visible
            binding.progressBar.isVisible = uiState.loading
            Validators.apply(uiState.emailValidation, binding.registerEmail)
            Validators.apply(uiState.passwordValidation, binding.registerPassword)

            // If error messages exist, put them in a toast.
            if (uiState.errorMessage != null) {
                Toast.makeText(this, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
            }

            // React to events.
            if (uiState.event != null) {
                val nextActivity = when (uiState.event) {
                    Event.GoToAdminApp -> Intent(this, AdminAppActivity::class.java)
                    else -> Intent(this, AppActivity::class.java)
                }

                Toast.makeText(this, "Successfully Logged-in", Toast.LENGTH_SHORT).show()
                startActivity(nextActivity)
                finishAffinity()
            }
        }
    }
}