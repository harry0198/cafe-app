package me.harrydrummond.cafeapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.harrydrummond.cafeapplication.R
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
class AdminAppActivity : AppCompatActivity(), CompleteProfileViewModel.ValidationListener {
    private lateinit var binding: ActivityAdminAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}