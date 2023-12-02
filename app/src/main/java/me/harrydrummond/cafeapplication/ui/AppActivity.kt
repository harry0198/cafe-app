package me.harrydrummond.cafeapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreUserRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import me.harrydrummond.cafeapplication.databinding.ActivityAppBinding
import me.harrydrummond.cafeapplication.databinding.ActivityOrderDetailsBinding
import me.harrydrummond.cafeapplication.ui.common.order.CartItemListViewAdapter
import me.harrydrummond.cafeapplication.ui.common.profile.CompleteProfileViewModel
import me.harrydrummond.cafeapplication.ui.customer.cart.CartFragment
import me.harrydrummond.cafeapplication.ui.customer.menu.MenuFragment
import me.harrydrummond.cafeapplication.ui.customer.orders.OrderDetailsViewModel
import me.harrydrummond.cafeapplication.ui.customer.orders.OrdersFragment
import me.harrydrummond.cafeapplication.ui.customer.user.UserProfileFragment

/**
 * AppActivity class.
 * This is the Customer App Page that functions using a bottom navigation and swaps out the
 * view fragments based on what was clicked.
 *
 * @see MenuFragment
 * @see CartFragment
 * @see OrdersFragment
 * @author Harry Drummond
 */
class AppActivity : AppCompatActivity(), CompleteProfileViewModel.ValidationListener {

    private lateinit var binding: ActivityAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(MenuFragment())

        // When a new item is selected, switch out the fragment
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.c_menu -> navigateToMenu()
                R.id.c_cart -> navigateToCart()
                R.id.c_orders -> navigateToOrders()
                R.id.c_account -> navigateToUserProfile()
                else -> {}
            }
            true
        }
    }

    /**
     * When the create profile fragment returns a success this is called.
     */
    override fun onValidationSuccess() {
        val toast = Toast.makeText(this, "Profile Information Saved", Toast.LENGTH_SHORT)
        toast.show()
    }

    /**
     * Changes fragment viewing to user profile
     */
    fun navigateToUserProfile() {
        replaceFragment(UserProfileFragment())
    }

    /**
     * Changes fragment viewing to menu
     */
    fun navigateToMenu() {
        replaceFragment(MenuFragment())
    }

    /**
     * Changes fragment viewing to cart
     */
    fun navigateToCart() {
        replaceFragment(CartFragment())
    }

    /**
     * Changes fragment viewing to orders
     */
    fun navigateToOrders() {
        replaceFragment(OrdersFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        // Replaces fragment in view
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}