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
import me.harrydrummond.cafeapplication.databinding.ActivityRegisterBinding
import me.harrydrummond.cafeapplication.ui.AppActivity
import me.harrydrummond.cafeapplication.ui.common.register.RegisterViewModel

class CreateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateProfileBinding
    private lateinit var viewModel: CreateProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(CreateProfileViewModel::class.java)

        setSupportActionBar(binding.cpToolbar)

        bindings()
    }

    fun onSkipBtnClicked(view: View) {
        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
    }

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

    private fun showLoadingBar(bool: Boolean) {
        binding.progressBar.isVisible = bool
    }

    private fun bindings() {
        viewModel.saveUIState.observe(this) {
            when(it) {
                State.PROCESSING -> showLoadingBar(true)
                State.COMPLETE_SUCCESS -> {
                    val intent = Intent(this, AppActivity::class.java)
                    val toast = Toast.makeText(this, "Profile Information Saved", Toast.LENGTH_SHORT)

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    toast.show()
                }
                else -> showLoadingBar(false)
            }
        }
    }
}