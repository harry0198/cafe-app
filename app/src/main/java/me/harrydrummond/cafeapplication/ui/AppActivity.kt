package me.harrydrummond.cafeapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.databinding.ActivityAppBinding
import me.harrydrummond.cafeapplication.databinding.ActivityOrderDetailsBinding
import me.harrydrummond.cafeapplication.ui.common.order.CartItemListViewAdapter
import me.harrydrummond.cafeapplication.ui.customer.cart.CartFragment
import me.harrydrummond.cafeapplication.ui.customer.menu.MenuFragment
import me.harrydrummond.cafeapplication.ui.customer.orders.OrderDetailsViewModel
import me.harrydrummond.cafeapplication.ui.customer.orders.OrdersFragment

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
class AppActivity : AppCompatActivity() {

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
                else -> {}
            }
            true
        }
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