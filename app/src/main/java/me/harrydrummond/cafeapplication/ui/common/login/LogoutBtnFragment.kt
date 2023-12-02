package me.harrydrummond.cafeapplication.ui.common.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.databinding.FragmentLogoutBtnBinding
import me.harrydrummond.cafeapplication.databinding.FragmentOrdersBinding
import me.harrydrummond.cafeapplication.ui.MainActivity

/**
 * Logout button fragment.
 * Logs user out when this button is pressed and sends them to the main activity.
 */
class LogoutBtnFragment : Fragment() {

    private lateinit var viewModel: LogoutBtnViewModel
    private lateinit var binding: FragmentLogoutBtnBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogoutBtnBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LogoutBtnViewModel::class.java)

        binding.button.setOnClickListener {
            onBtnLogoutPressed(it)
        }
    }

    /**
     * Event handler for the logout button
     */
    private fun onBtnLogoutPressed(view: View) {
        // Logout user and send to main activity
        viewModel.logoutUser()
        val intent = Intent(requireContext(), MainActivity::class.java)
        val toast = Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT)

        toast.show()
        startActivity(intent)
        finishAffinity(requireActivity())
    }

}