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
    public fun fetchLocation() {
        Log.i("flow", "fetchLocation start")
        val task = fusedLocationProviderClient.lastLocation
        if (checkPermissions()) {
            task.addOnSuccessListener {
                Log.i("flow", "show")
                if (it != null) {
                    listener?.invoke()// show(it)
                }
            }
        }
    }
    private fun checkPermissions(): Boolean {

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            Log.i("flow", "fetchLocation request permission")
            return true
        }
        return false
    }
    public lateinit var activity: Activity
    public lateinit var context: Context
    public lateinit var location: Location
    constructor(activity_: Activity, context_: Context) : this() {
        this.context = context_
        this.activity = activity_
        fetchLocation()
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