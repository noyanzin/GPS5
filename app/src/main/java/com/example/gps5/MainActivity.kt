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
    private lateinit var gps: Gps
    private lateinit var binding: ActivityMainBinding
    override fun onResume() {
        super.onResume()
        gps.startLocationUpdates()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("flow", "onStart")
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        gps = Gps(this, this)
        gps.listener = {
            show(gps.location)
        }

        binding.btnLast.setOnClickListener {
            Log.i("flow", "GetLastLocation")
            gps.fetchLocation()
        }

    }


    private fun show(location: Location){
        binding.longatude.text = location.longitude.toString()
        binding.latitude.text = location.latitude.toString()
        binding.speed.text = location.speed.toString()

    }
}
