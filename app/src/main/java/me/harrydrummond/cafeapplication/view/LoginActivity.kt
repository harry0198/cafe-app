package me.harrydrummond.cafeapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import me.harrydrummond.cafeapplication.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.toolbar_arrow_back)


        val button = findViewById<Button>(R.id.btnAuthenticate)
        button.setOnClickListener {
            val intent = Intent(this, BrowseMenuView::class.java).apply {

            }

            startActivity(intent)
        }
    }
}