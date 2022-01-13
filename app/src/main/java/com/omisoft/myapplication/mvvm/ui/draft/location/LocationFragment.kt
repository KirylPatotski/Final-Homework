package com.omisoft.myapplication.mvvm.ui.draft.location

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.omisoft.myapplication.R

class LocationFragment : Fragment() {

    companion object {
        private const val TAG = "LocationFragment"
    }

    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var requestBackgroundLocationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var passedDistanceText: TextView
    private lateinit var trackDistanceButton: AppCompatButton
    private var previousLocation: Location? = null
    private var passedDistance: Float = 0f
    private var trackDistance = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passedDistanceText = view.findViewById(R.id.passed_distance_text)
        trackDistanceButton = view.findViewById(R.id.track_distance_button)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        requestLocationPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                if (
                    permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
                    || permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
                ) {
                    getCurrentLocation()
                    updateLocations()
                }
            }
        requestBackgroundLocationPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                println(isGranted)
            }
        checkLocationPermissions()
        setClickListeners()
    }

    private fun setClickListeners() {
        trackDistanceButton.setOnClickListener {
            trackDistance = !trackDistance
        }
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PERMISSION_GRANTED
        ) {
            getCurrentLocation()
            updateLocations()
        } else {
            requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions() {
        requestLocationPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    private fun requestBackgroundLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestBackgroundLocationPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location ?: return@addOnSuccessListener
            Log.d(TAG, "LastLocation: latitude = ${location.latitude}, longitude = ${location.longitude}")
        }

        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            private var isCancellationRequested = false

            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                isCancellationRequested = true
                return this
            }

            override fun isCancellationRequested(): Boolean {
                return isCancellationRequested
            }
        }).addOnSuccessListener { location ->
            location ?: return@addOnSuccessListener
            Log.d(TAG, "CurrentLocation: latitude = ${location.latitude}, longitude = ${location.longitude}")
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocations() {

        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val updatedLocation = locationResult.lastLocation
                previousLocation?.let {
                    if (trackDistance) {
                        passedDistance += updatedLocation.distanceTo(it)
                        passedDistanceText.text = getString(R.string.location_fragment_passed_distance, passedDistance.toString())
                    }
                }

                Log.d(TAG, "Updated Location: latitude = ${updatedLocation.latitude}, longitude = ${updatedLocation.longitude}")
                previousLocation = updatedLocation
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun openAppSettings() {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
            data = uri
        })
    }
}