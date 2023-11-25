package me.harrydrummond.cafeapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.auth.User
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.databinding.ActivityAppBinding
import me.harrydrummond.cafeapplication.ui.cart.CartFragment
import me.harrydrummond.cafeapplication.ui.menu.MenuFragment
import me.harrydrummond.cafeapplication.ui.orders.OrdersFragment

class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding
    var userModel: UserModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(MenuFragment())

        userModel = intent.getParcelableExtra(IntentExtra.USER_MODEL, UserModel::class.java)

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.menu -> replaceFragment(MenuFragment())
                R.id.cart -> replaceFragment(CartFragment())
                R.id.orders -> replaceFragment(OrdersFragment())
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