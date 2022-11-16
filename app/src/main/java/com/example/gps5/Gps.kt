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
    var listenerUpdate: (()->Unit)? = null
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
        Log.i("flow", "fetchLocation started")
        val task = fusedLocationProviderClient.lastLocation
        Log.i("flow", "fusedProviderClient.lastLocation complete")
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("flow","Activity: ${this.activity}")
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            Log.i("flow", "request permission return true")
            return
        }
        task.addOnSuccessListener {
            Log.i("flow", "addOnSuccessListener started")
            if (it != null) {
                Log.i("flow", "addOnSuccessListener invoke")
                location = it
                listener?.invoke()// show(it)
            }
            Log.i("flow", "addOnSuccessListener complete")
        }

    }
    private fun checkPermissions(): Boolean {
        Log.i("flow", "CheckPermission")
        Log.i("flow", "Context: ${this.context}")

        Log.i("flow", "Request permission return false")
        return false
    }
    public lateinit var activity: Activity
    public lateinit var context: Context
    public lateinit var location: Location
    constructor(activity_: Activity, context_: Context) : this() {
        this.context = context_
        this.activity = activity_
        Log.i("flow","Activity: $activity, Context: $context")
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,10000).build()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        fetchLocation()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for(l in p0.locations){
                    Log.i("flow","Update UI with locationResult data")
                    Log.i("flow", "Location: ${l.speed} ${l.latitude} ${l.longitude}")
                    location = l
                    listenerUpdate?.invoke()  //show(location)
                }

            }
        }
    }
}