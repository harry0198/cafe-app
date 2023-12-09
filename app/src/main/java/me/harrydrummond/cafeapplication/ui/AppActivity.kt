package me.harrydrummond.cafeapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.Cart
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Status
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.databinding.ActivityAppBinding
import me.harrydrummond.cafeapplication.databinding.FragmentMenuBinding
import me.harrydrummond.cafeapplication.ui.common.productview.ProductListViewAdapter
import me.harrydrummond.cafeapplication.ui.common.profile.CompleteProfileViewModel
import me.harrydrummond.cafeapplication.ui.customer.cart.CartFragment
import me.harrydrummond.cafeapplication.ui.customer.menu.MenuFragment
import me.harrydrummond.cafeapplication.ui.customer.menu.MenuViewModel
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
@AndroidEntryPoint
class AppActivity : AppCompatActivity(), CompleteProfileViewModel.ValidationListener {

    private lateinit var binding: ActivityAppBinding
    private lateinit var viewModel: CustomerAppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomerAppViewModel::class.java)
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

        // On cart update.
        Cart.getInstance().products.observe(this) { products ->
            val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.c_cart)
            badge.isVisible = products.isNotEmpty();
            badge.number = products.size
        }

        // On orders update
        viewModel.orders.observe(this) {
            val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.c_orders);
            val incomingOrders = it.filter { order -> order.status != Status.COLLECTED }

            badge.isVisible = incomingOrders.isNotEmpty()
            badge.number = incomingOrders.size
        }
    }



    override fun onResume() {
        super.onResume()
        viewModel.refreshOrders()

        // Give user their notifications.
        viewModel.showNotifications()
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