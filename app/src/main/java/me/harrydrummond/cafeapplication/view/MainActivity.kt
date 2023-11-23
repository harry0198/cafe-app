package me.harrydrummond.cafeapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import me.harrydrummond.cafeapplication.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button = findViewById<Button>(R.id.btnLogIn)
        button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {

            }

            startActivity(intent)
        }
    }
}