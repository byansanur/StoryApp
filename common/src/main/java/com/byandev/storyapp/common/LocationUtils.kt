package com.byandev.storyapp.common

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import com.vmadalin.easypermissions.EasyPermissions
import java.util.*


class LocationUtils(private val context: Context) : LocationListener {
    // flag for GPS status
    var isGPSEnabled = false
    //fal for network status
    var isNetworkEnabled = false

    //flag for GPS status
    val canGetLocation: Boolean
        get() = getLocation()

    var latitude: Double? = null
    var longitude: Double? = null
    var addressName : String? = null

    // The minimum distance to change Updates in meters
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10F // 10 meters


    // The minimum time between updates in milliseconds
    private val MIN_TIME_BW_UPDATES: Long = 1000 * 60 * 1 // 1 minute

    // Declaring geo coder
    private val geocoder = Geocoder(context, Locale("in", "ID"))

    @SuppressLint("MissingPermission")
    private fun getLocation() : Boolean {
        var output = false
        try {
            var location: Location? = null
            val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(
                    context,
                    context.getString(R.string.activate_gps),
                    Toast.LENGTH_SHORT
                ).show()
                output = false
            } else {
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    //check the network permission
                    if (EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        location?.also {
                            latitude = it.latitude
                            longitude = it.longitude
                        }
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.allow_gps),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("Location Utils", "Network disable")
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if (EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            location?.also {
                                latitude = it.latitude
                                longitude = it.longitude
                            }
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.allow_gps),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Log.e("Location Utils", "GPS disable")
                }
                //get location
                if (latitude !=null && longitude != null) {
                    try {
                        geocoder.getFromLocation(latitude!!, longitude!!,1)[0]?.getAddressLine(0)?.also {
                            addressName = it
                        }
                        output = true
                    } catch (e : Exception) {
                        addressName = context.getString(R.string.unknown_place)
                        output = false
                    }
                }
                //stop gps
                locationManager.removeUpdates(this)
            }
        } catch (e : Exception) {
            e.printStackTrace()
            output = false
            Toast.makeText(
                context,
                context.getString(R.string.error_in_app),
                Toast.LENGTH_SHORT
            ).show()
        }
        return output
    }


    override fun onLocationChanged(p0: Location) {
        //noting
    }

}