package me.harrydrummond.cafeapplication.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.model.Role

/**
 * Class to
 */
abstract class AbstractAuthenticatedActivity(var role: Role = Role.CUSTOMER): AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!AuthenticatedUser.getInstance().isAuthenticated()) {
            // User is not authenticated, so should not be able to be here.
            // Redirect to login page
            Toast.makeText(this, "You must be logged in to do this action", Toast.LENGTH_LONG).show()

            startActivity(Intent(this, MainActivity::class.java))
            return
        }
    }
}