package me.harrydrummond.cafeapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.Status
import me.harrydrummond.cafeapplication.databinding.ActivityAdminAppBinding
import me.harrydrummond.cafeapplication.ui.admin.accounts.AdminAccountsFragment
import me.harrydrummond.cafeapplication.ui.admin.editmenu.EditMenuFragment
import me.harrydrummond.cafeapplication.ui.admin.orders.AdminOrdersFragment
import me.harrydrummond.cafeapplication.ui.common.profile.CompleteProfileViewModel

/**
 * AdminAppActivity class.
 * This is the Customer App Page that functions using a bottom navigation and swaps out the
 * view fragments based on what was clicked.
 *
 * @see EditMenuFragment
 * @see AdminOrdersFragment
 * @author Harry Drummond
 */
@AndroidEntryPoint
class AdminAppActivity : AppCompatActivity(), CompleteProfileViewModel.ValidationListener {
    private lateinit var binding: ActivityAdminAppBinding
    private lateinit var viewModel: AdminAppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminAppViewModel::class.java)

        binding = ActivityAdminAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(EditMenuFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.a_menu -> replaceFragment(EditMenuFragment())
                R.id.a_orders -> replaceFragment(AdminOrdersFragment())
                R.id.a_user -> replaceFragment(AdminAccountsFragment())
                else -> {}
            }
            true
        }

        viewModel.orders.observe(this) {
            val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.a_orders)
            val incomingOrders = it.filter { order -> order.status in arrayOf(Status.RECEIVED, Status.NONE) }

            badge.isVisible = incomingOrders.isNotEmpty()
            badge.number = incomingOrders.size
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    /**
     * When the create profile fragment returns a success this is executed.
     */
    override fun onValidationSuccess() {
        val toast = Toast.makeText(this, "Profile Information Saved", Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshOrders()
    }
}