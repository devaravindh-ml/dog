package com.example.dog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure you have an 'activity_login.xml' layout file in res/layout/
        setContentView(R.layout.activity_main)

        // 1. Find the login button using its ID from activity_login.xml
        val loginButton: Button = findViewById(R.id.buttonLogin)

        // 2. Set the click listener
        loginButton.setOnClickListener {
            // In a real application, you would perform authentication checks here.
            // For now, we immediately proceed to the next screen.

            // Create an Intent to navigate from LoginActivity to Report_an_Incident
            val intent = Intent(this, Report_an_Incident::class.java)

            // Start the destination Activity
            startActivity(intent)

            // Optional: Call finish() if you don't want the user to return to the login screen
            // by pressing the back button after a successful login.
            finish()

            // Display a success message (for testing purposes)
            Toast.makeText(this, "Logged in successfully! Navigating to Incident Report.", Toast.LENGTH_SHORT).show()
        }
    }
}
