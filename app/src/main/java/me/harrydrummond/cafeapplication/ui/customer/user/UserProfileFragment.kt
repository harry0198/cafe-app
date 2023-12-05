package me.harrydrummond.cafeapplication.ui.customer.user

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.databinding.FragmentAdminAccountsBinding
import me.harrydrummond.cafeapplication.databinding.FragmentUserProfileBinding
import me.harrydrummond.cafeapplication.ui.MainActivity
import me.harrydrummond.cafeapplication.ui.admin.accounts.AdminAccountsViewModel

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private lateinit var viewModel: UserProfileViewModel
    private lateinit var binding: FragmentUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        binding.logoutButton.setOnClickListener {
            AuthenticatedUser.getInstance().logout()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }
    }

}