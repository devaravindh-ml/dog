package com.example.dog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure you have an 'activity_main.xml' layout file in res/layout/
        setContentView(R.layout.activity_main)

        // 1. Find the login button
        val loginButton: Button = findViewById(R.id.buttonLogin)

        // **NEW: Find the Forget Password button**
        val forgetPasswordButton: Button = findViewById(R.id.forgotPasswordButton)

        // 2. Set the click listener for the Login button
        loginButton.setOnClickListener {
            // Create an Intent to navigate from MainActivity to Report_an_Incident
            val intent = Intent(this, Report_an_Incident::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Logged in successfully! Navigating to Incident Report.", Toast.LENGTH_SHORT).show()
        }

        // **NEW: Set the click listener for the Forget Password button**
        forgetPasswordButton.setOnClickListener {
            // Create an Intent to navigate from MainActivity to ForgetPasswordActivity
            // NOTE: You must create a class named ForgetPasswordActivity for this to work.
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)

            // Optional: You usually don't call finish() here, as the user might want to
            // return to the login screen after attempting to reset their password.

            Toast.makeText(this, "Navigating to Forget Password page.", Toast.LENGTH_SHORT).show()
        }
    }
}