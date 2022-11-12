package com.example.gps5

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.gps5.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }
    private lateinit var locationCallback: LocationCallback
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("flow", "onStart")
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,0).build()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for(location in p0.locations){
                    Log.i("flow","Update UI with locationResult data")
                    show(location)
                }

            }
        }
        binding.btnLast.setOnClickListener {
            Log.i("flow", "GetLastLocation")
            fetchLocation()
        }

    }
    private fun fetchLocation(){
        Log.i("flow","fetchLocation start")
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
                Log.i("flow", "fetchLocation request permission")
                return
            }
       task.addOnSuccessListener {
           Log.i("flow","show")
           if (it != null) {
               show(it)
           }
       }
    }
    private fun show(location: Location){
        binding.longatude.text = location.longitude.toString()
        binding.latitude.text = location.latitude.toString()
        binding.speed.text = location.speed.toString()

    }
}
