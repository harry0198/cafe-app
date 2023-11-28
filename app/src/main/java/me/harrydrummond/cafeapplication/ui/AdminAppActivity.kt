package me.harrydrummond.cafeapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.databinding.ActivityAdminAppBinding
import me.harrydrummond.cafeapplication.databinding.ActivityAppBinding
import me.harrydrummond.cafeapplication.ui.admin.editmenu.EditMenuFragment
import me.harrydrummond.cafeapplication.ui.admin.orders.AdminOrdersFragment
import me.harrydrummond.cafeapplication.ui.customer.cart.CartFragment
import me.harrydrummond.cafeapplication.ui.customer.menu.MenuFragment
import me.harrydrummond.cafeapplication.ui.customer.orders.OrdersFragment

/**
 * AdminAppActivity class.
 * This is the Customer App Page that functions using a bottom navigation and swaps out the
 * view fragments based on what was clicked.
 *
 * @see EditMenuFragment
 * @see AdminOrdersFragment
 * @author Harry Drummond
 */
class AdminAppActivity : AppCompatActivity() {
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
}