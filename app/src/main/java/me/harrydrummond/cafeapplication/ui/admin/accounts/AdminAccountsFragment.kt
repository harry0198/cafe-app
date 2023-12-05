package me.harrydrummond.cafeapplication.ui.admin.accounts

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.databinding.FragmentAdminAccountsBinding
import me.harrydrummond.cafeapplication.databinding.FragmentCompleteProfileBinding
import me.harrydrummond.cafeapplication.ui.common.profile.CompleteProfileViewModel
import me.harrydrummond.cafeapplication.ui.common.register.RegisterActivity
import me.harrydrummond.cafeapplication.ui.customer.user.UserProfileFragment
import me.harrydrummond.cafeapplication.ui.customer.user.UserProfileViewModel

class AdminAccountsFragment : Fragment() {

    private lateinit var viewModel: AdminAccountsViewModel
    private lateinit var binding: FragmentAdminAccountsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminAccountsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminAccountsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateAdminAccount.setOnClickListener {
            val intent = Intent(requireContext(), RegisterActivity::class.java).apply {
                putExtra(IntentExtra.ACCOUNT_TYPE, Role.EMPLOYEE)
            }
            startActivity(intent)
        }
    }

}