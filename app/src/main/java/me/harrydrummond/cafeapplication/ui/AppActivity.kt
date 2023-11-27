package me.harrydrummond.cafeapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.data.repository.UserRepository
import me.harrydrummond.cafeapplication.databinding.ActivityAppBinding
import me.harrydrummond.cafeapplication.ui.customer.cart.CartFragment
import me.harrydrummond.cafeapplication.ui.customer.menu.MenuFragment
import me.harrydrummond.cafeapplication.ui.customer.orders.OrdersFragment

class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(MenuFragment())

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

    fun navigateToMenu() {
        replaceFragment(MenuFragment())
    }

    fun navigateToCart() {
        replaceFragment(CartFragment())
    }

    fun navigateToOrders() {
        replaceFragment(OrdersFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}