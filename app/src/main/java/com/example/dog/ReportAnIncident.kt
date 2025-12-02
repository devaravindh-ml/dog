package com.example.dog

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Renaming the class to better reflect the purpose of the screen
class ReportAnIncident : AppCompatActivity() {

    // Constants for permission request
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private val MAP_SELECTION_REQUEST_CODE = 200

    // NEW CONSTANTS for photo picking
    private val PICK_IMAGE_REQUEST_CODE = 300
    private val TAKE_PHOTO_REQUEST_CODE = 301

    // Location fields
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // UI fields
    private lateinit var etDate: TextInputEditText
    private lateinit var etTime: TextInputEditText
    private lateinit var etLocation: TextInputEditText
    private lateinit var etDescription: TextInputEditText // <-- Added for validation
    private lateinit var tilLocation: TextInputLayout
    private lateinit var btnAddPhoto: Button
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report_an_incident) // Assuming your layout file is correct

        // Initialize Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 1. Initialize the UI fields
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        etLocation = findViewById(R.id.etLocation)
        etDescription = findViewById(R.id.etDescription)
        tilLocation = findViewById(R.id.tilLocation)
        btnAddPhoto = findViewById(R.id.btnAddPhoto)

        // 2. Set the current date and time
        setCurrentDateTime()

        // 3. Set up the click listeners for pickers and map icon
        setListeners()

        // --- Submit Button Click Listener ---
        val submitButton: Button = findViewById(R.id.btnSubmit)

        submitButton.setOnClickListener {
            // Check validation
            if (validateForm()) {
                // If validation passes, show success toast
                Toast.makeText(this@ReportAnIncident, "Report Submitted Successfully!", Toast.LENGTH_LONG).show()

                // 🚨 UPDATED NAVIGATION: Move to Lost_Found page
                val intent = Intent(this@ReportAnIncident, Lost_Found::class.java)
                startActivity(intent)
                finish() // Optionally finish the current activity so the user can't press back to return to the empty form
            }
        }
    }

    // ------------------------------------------------------------------------------------------
    //                                  VALIDATION METHOD
    // ------------------------------------------------------------------------------------------

    /**
     * Checks if required fields are filled before submission.
     */
    private fun validateForm(): Boolean {
        var isValid = true

        // 1. Check Date
        if (etDate.text.isNullOrBlank()) {
            etDate.error = "Date is required"
            isValid = false
        }

        // 2. Check Time
        if (etTime.text.isNullOrBlank()) {
            etTime.error = "Time is required"
            isValid = false
        }

        // 3. Check Location
        if (etLocation.text.isNullOrBlank()) {
            tilLocation.error = "Location is required"
            isValid = false
        } else {
            tilLocation.error = null // Clear error if valid
        }

        // 4. Check Description (Assuming a description is required)
        if (etDescription.text.isNullOrBlank() || etDescription.text.toString().length < 10) {
            etDescription.error = "Please provide a detailed description (min 10 characters)"
            isValid = false
        }

        if (!isValid) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_LONG).show()
        }

        return isValid
    }

    // ------------------------------------------------------------------------------------------
    //                                  LOCATION METHODS
    // ------------------------------------------------------------------------------------------

    /**
     * Checks location permissions and initiates the process of getting the current location.
     */
    private fun getCurrentLocation() {
        // 1. Check permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // 2. Permissions granted, get the last known location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    getAddressFromLocation(location)
                } else {
                    Toast.makeText(this, "Unable to get current location. Ensure GPS is enabled.", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error accessing location services.", Toast.LENGTH_LONG).show()
            }
    }

    /**
     * Converts GPS coordinates to a human-readable address string using Geocoder.
     */
    private fun getAddressFromLocation(location: Location) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                // Construct a clean, human-readable address line
                val fullAddress = address.getAddressLine(0) ?: "${address.locality}, ${address.countryName}"

                etLocation.setText(fullAddress)
                Toast.makeText(this, "Location set via GPS.", Toast.LENGTH_SHORT).show()
            } else {
                // Fallback: use raw coordinates if Geocoder fails to find an address
                etLocation.setText("${location.latitude}, ${location.longitude}")
                Toast.makeText(this, "Could not resolve address. Using coordinates.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            // Fallback if Geocoding service throws an exception
            etLocation.setText("${location.latitude}, ${location.longitude}")
            Toast.makeText(this, "Geocoding failed. Using coordinates.", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Handles the result of the permission request.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, immediately try to get location
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission denied. Cannot auto-fill location.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // ------------------------------------------------------------------------------------------
    //                                  ACTIVITY RESULTS (Map, Gallery, Camera)
    // ------------------------------------------------------------------------------------------

    /**
     * Handles the result returned from MapSelectionActivity, Gallery, and Camera.
     */
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return // Check if the operation was successful

        when (requestCode) {
            MAP_SELECTION_REQUEST_CODE -> {
                // Result from MapSelectionActivity
                val lat = data?.getDoubleExtra("selected_lat", 0.0)
                val lon = data?.getDoubleExtra("selected_lon", 0.0)

                if (lat != null && lon != null && lat != 0.0 && lon != 0.0) {
                    val selectedLocation = Location("MapSelection").apply {
                        latitude = lat
                        longitude = lon
                    }
                    getAddressFromLocation(selectedLocation)
                    Toast.makeText(this, "Location set from map selection.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Location selection failed.", Toast.LENGTH_SHORT).show()
                }
            }

            PICK_IMAGE_REQUEST_CODE -> {
                // Result from Gallery (successfully picked an image)
                val selectedImageUri = data?.data
                if (selectedImageUri != null) {
                    Toast.makeText(this, "Photo selected from Gallery!", Toast.LENGTH_LONG).show()
                }
            }

            TAKE_PHOTO_REQUEST_CODE -> {
                // Result from Camera (returns a thumbnail Bitmap)
                val photoBitmap = data?.extras?.get("data")
                if (photoBitmap != null) {
                    Toast.makeText(this, "Photo taken successfully!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------
    //                                  IMAGE SELECTION METHODS
    // ------------------------------------------------------------------------------------------

    /**
     * Shows a dialog allowing the user to choose between taking a photo or selecting from the gallery.
     */
    private fun showImageSourceDialog() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Photo Source")
        builder.setItems(options) { dialog, item ->
            when (options[item]) {
                "Take Photo" -> launchCameraIntent()
                "Choose from Gallery" -> launchGalleryIntent()
                "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    /**
     * Launches the system camera application.
     */
    private fun launchCameraIntent() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, TAKE_PHOTO_REQUEST_CODE)
    }

    /**
     * Launches the system gallery/file picker application.
     */
    private fun launchGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }


    // ------------------------------------------------------------------------------------------
    //                                  DATE/TIME METHODS
    // ------------------------------------------------------------------------------------------

    private fun setCurrentDateTime() {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        etDate.setText(dateFormat.format(date))
        etTime.setText(timeFormat.format(date))
    }

    private fun setListeners() {
        // Disable keyboard input for Date/Time fields
        etDate.keyListener = null
        etTime.keyListener = null
        etDate.isFocusable = false
        etTime.isFocusable = false

        // Set up the listener for the Date field
        etDate.setOnClickListener { showDatePickerDialog() }

        // Set up the listener for the Time field
        etTime.setOnClickListener { showTimePickerDialog() }

        // Add the click listener for the Map icon (Location TextInputLayout)
        tilLocation.setEndIconOnClickListener {
            // Launch the map selection screen
            val intent = Intent(this@ReportAnIncident, MapSelectionActivity::class.java)
            startActivityForResult(intent, MAP_SELECTION_REQUEST_CODE)
        }

        // Add the click listener for the Photo button
        btnAddPhoto.setOnClickListener {
            showImageSourceDialog() // Triggers the camera/gallery choice
        }
    }

    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            etDate.setText(dateFormat.format(calendar.time))
        }

        DatePickerDialog(
            this@ReportAnIncident,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePickerDialog() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            etTime.setText(timeFormat.format(calendar.time))
        }

        TimePickerDialog(
            this@ReportAnIncident,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }
}