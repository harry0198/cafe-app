package me.harrydrummond.cafeapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import me.harrydrummond.cafeapplication.databinding.ActivityMainBinding
import me.harrydrummond.cafeapplication.ui.common.login.LoginActivity
import me.harrydrummond.cafeapplication.ui.common.register.RegisterActivity
import me.harrydrummond.cafeapplication.ui.customer.cart.CartFragment
import me.harrydrummond.cafeapplication.ui.customer.menu.MenuFragment
import me.harrydrummond.cafeapplication.ui.customer.orders.OrdersFragment

/**
 * MainActivity class.
 * This is the first screen that users see when opening the app.
 *
 * @author Harry Drummond
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * Event handler for the login button
     */
    fun onLoginButtonClicked(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    /**
     * Event handler for the register button
     */
    fun onRegisterButtonClicked(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}