
package me.harrydrummond.cafeapplication.ui.common.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.Validators
import me.harrydrummond.cafeapplication.databinding.FragmentCompleteProfileBinding


class CompleteProfileFragment : Fragment() {

    private lateinit var viewModel: CompleteProfileViewModel
    private lateinit var binding: FragmentCompleteProfileBinding
    private lateinit var validationListener: CompleteProfileViewModel.ValidationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Ensure activity implements validation listener. This fragment
        // does not care what we do with the information after it is validated so it is delegated
        // to the parent activity.
        if (context is CompleteProfileViewModel.ValidationListener) {
            validationListener = context
        } else {
            throw ClassCastException("Parent activity must implement ValidationListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CompleteProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompleteProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateProfile.setOnClickListener {
            onFinishBtnClicked(it)
        }

        handleUIState()
    }

    /**
     * Event handler for the finish button.
     * Sends the user to the AppActivity and saves their profile.
     * Validates the user input is proper.
     */
    private fun onFinishBtnClicked(view: View) {
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val phoneNumber = binding.phoneNumber.text.toString()

        viewModel.saveProfileInformation(firstName, lastName, phoneNumber)
    }

    private fun handleUIState() {
        viewModel.uiState.observe(requireActivity()) { uiState ->
            val progressBar = requireActivity().findViewById<View>(me.harrydrummond.cafeapplication.R.id.progressBar) as ProgressBar
            progressBar.isVisible = uiState.loading
            binding.firstName.setText(uiState.firstName)
            binding.lastName.setText(uiState.lastName)
            binding.phoneNumber.setText(uiState.phoneNumber)

            // Apply validation results
            Validators.apply(uiState.firstNameValidated, binding.firstName)
            Validators.apply(uiState.lastNameValidated, binding.lastName)
            Validators.apply(uiState.phoneNumberValidated, binding.phoneNumber)

            if (uiState.errorMessage != null) {
                Toast.makeText(requireContext(), uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
            }

            if (uiState.event != null) {
                when (uiState.event) {
                    is CompleteProfileViewModel.Event.ProfileSaved -> {
                        validationListener.onValidationSuccess()
                        viewModel.eventHandled()
                    }
                }
            }
        }
    }

}