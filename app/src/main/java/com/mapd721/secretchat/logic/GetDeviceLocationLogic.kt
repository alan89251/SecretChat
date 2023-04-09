package com.mapd721.secretchat.logic

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

/**
 * Get device location
 * Note: It's caller's responsibility to enable the required permission
 */
class GetDeviceLocationLogic(
    private val locationManager: LocationManager,
) {
    //private var isUpdateLocation = true // control whether to update the location
    private lateinit var onReceievedDeviceLocation: (Location) -> Unit

    /**
     * Send a request for location
     */
    @SuppressLint("MissingPermission")
    fun requestLocation(onReceievedDeviceLocation: (Location) -> Unit) {
        this.onReceievedDeviceLocation = onReceievedDeviceLocation

        val ONE_HOUR_IN_MS = 3600000L // interval for location update, set to 1 hour to prevent frequent update
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, ONE_HOUR_IN_MS, 0F, locationListener)
    }

    // listener to receive location
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(p0: Location) {
            locationManager.removeUpdates(this)
            onReceievedDeviceLocation(p0)
        }
    }
}