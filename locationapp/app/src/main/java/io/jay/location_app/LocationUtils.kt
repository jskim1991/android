package io.jay.location_app

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationUtils(val context: Context) {

    private val _fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fun hasLocationPermission(context: Context): Boolean {
        return checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
                checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
    }

//    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(onLocation: (LocationData) -> Unit) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                locationResult.lastLocation?.let {
                    val location = LocationData(latitude = it.latitude, longitude = it.longitude)
                    onLocation(location)
                }
            }
        }

        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
        _fusedLocationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
    }

    fun geocodeToAddress(location: LocationData): String {
        val geocoder = Geocoder(context, Locale.getDefault())

        val coordinates = LatLng(location.latitude, location.longitude)
        val addresses: List<Address?>? = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1)

        return addresses?.first()?.getAddressLine(0) ?: "Address not found"
    }
}