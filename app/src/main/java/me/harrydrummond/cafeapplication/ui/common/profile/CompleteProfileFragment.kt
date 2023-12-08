
package me.harrydrummond.cafeapplication.ui.common.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import me.harrydrummond.cafeapplication.logic.validators.Validators
import me.harrydrummond.cafeapplication.databinding.FragmentCompleteProfileBinding


@AndroidEntryPoint
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
        val fullName = binding.fullName.text.toString()
        val email = binding.email.text.toString()
        val phoneNumber = binding.phoneNumber.text.toString()

        viewModel.saveProfileInformation(fullName, phoneNumber, email)
    }

    private fun handleUIState() {
        viewModel.uiState.observe(requireActivity()) { uiState ->
            val progressBar = requireActivity().findViewById<View>(me.harrydrummond.cafeapplication.R.id.progressBar) as ProgressBar
            progressBar.isVisible = uiState.loading

            binding.fullName.setText(uiState.fullName)
            binding.phoneNumber.setText(uiState.phoneNumber)
            binding.email.setText(uiState.email)

            // Apply validation results
            Validators.apply(uiState.fullNameValidated, binding.fullName)
            Validators.apply(uiState.emailValidated, binding.email)
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