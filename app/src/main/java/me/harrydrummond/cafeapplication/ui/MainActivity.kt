package me.harrydrummond.cafeapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import me.harrydrummond.cafeapplication.databinding.ActivityMainBinding
import me.harrydrummond.cafeapplication.ui.register.RegisterActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onLoginButtonClicked(view: View) {
//        val intent = Intent(this, LoginActivity::class.java)
//        intent.putExtra("IS_EMPLOYEE", binding.employeeSwitch.isChecked)
//        startActivity(intent)
        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
    }

    fun onRegisterButtonClicked(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("IS_EMPLOYEE", binding.employeeSwitch.isChecked)
        startActivity(intent)
    }
}