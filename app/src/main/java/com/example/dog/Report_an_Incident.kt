package com.example.dog

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button // Important: Import the Button class
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity

    class Report_an_Incident : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            // Sets the content view using the XML layout
            setContentView(R.layout.activity_report_an_incident)

            // 1. Get a reference to the Submit Button from your XML layout.
            // Make sure the ID below matches the ID you used in activity_report_an_incident.xml
            val submitButton: Button = findViewById(R.id.btnSubmit)

            // 2. Set the click listener on the button
            submitButton.setOnClickListener {
                // 3. Create an Intent to navigate to the new activity
                // The Intent needs the current context (this@Report_an_Incident) and the destination class
                val intent = Intent(this@Report_an_Incident,ReportsActivity::class.java)

                // 4. Start the new Activity
                startActivity(intent)

                // Optional: If you don't want the user to be able to go back to this screen
                // after submitting, you can call finish().
                // finish()
            }
        }
    }


