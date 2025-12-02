package com.example.dog

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

// MapSelectionActivity handles user interaction for selecting a location on a map.
class MapSelectionActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {

    private lateinit var map: GoogleMap
    private lateinit var btnConfirmLocation: Button
    private var selectedMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure you have created the activity_map_selection layout file!
        setContentView(R.layout.activity_map_selection)

        // Initialize UI components
        btnConfirmLocation = findViewById(R.id.btnConfirmLocation)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set up the confirmation button click listener
        btnConfirmLocation.setOnClickListener {
            selectedMarker?.position?.let { latLng ->
                returnSelectedLocation(latLng.latitude, latLng.longitude)
            } ?: run {
                Toast.makeText(this, "Tap the map to select a location first.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Set map interaction settings
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapClickListener(this)
        map.setOnMarkerDragListener(this)

        // Default: Zoom to a general area (e.g., London) if no location is passed
        val defaultLocation = LatLng(51.5074, 0.1278)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
    }

    /**
     * Handles map click (tap) events. Places a new marker or moves the existing one.
     */
    override fun onMapClick(latLng: LatLng) {
        // Remove any existing marker
        selectedMarker?.remove()

        // Add a new marker at the tapped location
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Selected Location")
            .snippet(getAddressSnippet(latLng))
            .draggable(true) // Make the marker draggable

        selectedMarker = map.addMarker(markerOptions)
        selectedMarker?.showInfoWindow()

        // Move the camera to the new marker and enable the confirm button
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, map.cameraPosition.zoom.coerceAtLeast(14f)))
        btnConfirmLocation.isEnabled = true
    }

    // --- Marker Drag Listeners ---
    override fun onMarkerDragStart(marker: Marker) {
        // Optional: Can change marker color or visibility
    }

    override fun onMarkerDragEnd(marker: Marker) {
        val newLatLng = marker.position
        // Update the marker snippet with the new resolved address
        marker.snippet = getAddressSnippet(newLatLng)
        marker.showInfoWindow()
        map.animateCamera(CameraUpdateFactory.newLatLng(newLatLng))
    }

    override fun onMarkerDrag(marker: Marker) {
        // Optional: Update coordinates text in real-time
    }
    // -----------------------------

    /**
     * Utility function to get a brief address for the marker snippet using Geocoder.
     */
    private fun getAddressSnippet(latLng: LatLng): String {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val street = address.thoroughfare ?: address.featureName ?: "Unknown Street"
                val locality = address.locality ?: address.countryName ?: "Unknown Area"
                "$street, $locality"
            } else {
                "Coordinates: ${String.format("%.4f, %.4f", latLng.latitude, latLng.longitude)}"
            }
        } catch (e: Exception) {
            "Coordinates: ${String.format("%.4f, %.4f", latLng.latitude, latLng.longitude)}"
        }
    }

    /**
     * Returns the selected location coordinates to the calling ReportsActivity.
     */
    private fun returnSelectedLocation(lat: Double, lon: Double) {
        val resultIntent = Intent().apply {
            // Pass the selected latitude and longitude back
            putExtra("selected_lat", lat)
            putExtra("selected_lon", lon)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}