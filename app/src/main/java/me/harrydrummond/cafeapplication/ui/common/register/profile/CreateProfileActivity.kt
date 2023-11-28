package me.harrydrummond.cafeapplication.ui.common.register.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.databinding.ActivityCreateProfileBinding
import me.harrydrummond.cafeapplication.databinding.ActivityLoginBinding
import me.harrydrummond.cafeapplication.databinding.ActivityRegisterBinding
import me.harrydrummond.cafeapplication.ui.AppActivity
import me.harrydrummond.cafeapplication.ui.common.login.LoginViewModel
import me.harrydrummond.cafeapplication.ui.common.register.RegisterViewModel

/**
 * CreateProfileActivity class.
 * This is the View for the MVVM pattern. Sends events to the CreateProfileViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see CreateProfileViewModel
 * @see ActivityCreateProfileBinding
 * @author Harry Drummond
 */
class CreateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateProfileBinding
    private lateinit var viewModel: CreateProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(CreateProfileViewModel::class.java)

        setSupportActionBar(binding.cpToolbar)

        handleUIState()
    }

    /**
     * Event handler for the skip button.
     * Sends the user to the AppActivity without updating their profile.
     */
    fun onSkipBtnClicked(view: View) {
        val intent = Intent(this, AppActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finishAffinity()
    }

    /**
     * Event handler for the finish button.
     * Sends the user to the AppActivity and saves their profile.
     * Validates the user input is proper.
     */
    fun onFinishBtnClicked(view: View) {
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val phoneNumber = binding.phoneNumber.text.toString()

        if (firstName.isEmpty()) {
            binding.firstName.error = "Please enter a name"
        }
        if (lastName.isEmpty()) {
            binding.lastName.error = "Please enter a name";
        }
        if (phoneNumber.isEmpty()) {
            binding.phoneNumber.error = "Please enter a phone number"
        }

        viewModel.saveProfileInformation(firstName, lastName, phoneNumber)
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
                    is Event.ProfileSaved -> {
                        val intent = Intent(this, AppActivity::class.java)
                        val toast = Toast.makeText(this, "Profile Information Saved", Toast.LENGTH_SHORT)

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finishAffinity()
                        toast.show()
                    }
                }
            }
        }
    }
}