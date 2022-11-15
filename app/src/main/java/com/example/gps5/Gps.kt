package com.example.gps5

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class Gps() {
    var listener: (()->Unit)? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    public fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    public fun fetchLocation(context: Context) {
        Log.i("flow", "fetchLocation start")
        val task = fusedLocationProviderClient.lastLocation
        if (checkPermissions(context)) {
            task.addOnSuccessListener {
                Log.i("flow", "show")
                if (it != null) {
                    listener?.invoke()// show(it)
                }
            }
        }
    }
    private fun checkPermissions(context: Context): Boolean {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            Log.i("flow", "fetchLocation request permission")
            return true
        }
        return false
    }
    public lateinit var location: Location
    constructor(activity: Activity) : this() {
        fetchLocation(activity)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,10000).build()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for(loc in p0.locations){
                    Log.i("flow","Update UI with locationResult data")
                    location = loc
                //show(location)// CallBack function
                }

            }
        }

    }
}