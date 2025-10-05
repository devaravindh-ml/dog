package com.example.reportsapp // 🚨 IMPORTANT: REPLACE 'com.example.reportsapp' with your actual app package name

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// 🚨 FIX: Explicitly importing the R class is the most robust way to resolve the 'Unresolved reference R' error.
// This works if your file is in the 'com.example.reportsapp' package.


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * ReportsActivity displays a Google Map to visualize reported incidents.
 * It implements OnMapReadyCallback to ensure the map is ready before use.
 */
class ReportsActivity : AppCompatActivity(), OnMapReadyCallback {

    // Lateinit promises that this variable will be initialized before it is accessed.
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure you have an 'activity_reports.xml' layout file in res/layout/
        setContentView(R.layout.activity_reports)

        // Find the map fragment container in your layout file (activity_reports.xml)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragmentContainer) as SupportMapFragment

        // Request the map instance asynchronously
        mapFragment.getMapAsync(this)
    }

    /**
     * Called when the map is ready to be used. This is where map configuration goes.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Configure basic UI settings for the map
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true

        // Example: Center the map on a specific location and add a marker
        val newYork = LatLng(40.7128, -74.0060)
        googleMap.addMarker(MarkerOptions().position(newYork).title("Report Hotspot"))

        // Move the camera to the marker and set a decent zoom level (10f)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newYork, 10f))

        // TODO: In a production app, you would fetch real incident data from a database (like Firestore)
        // and loop through it to add markers dynamically.
    }
}
