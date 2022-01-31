package com.omisoft.myapplication.mvvm.ui.draft.weather

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.omisoft.myapplication.R
import com.omisoft.myapplication.mvvm.utils.WeatherWidget

class WeatherFragment : Fragment() {

    companion object {
        private const val TAG = "WeatherFragment"
    }

    private val viewModel by viewModels<WeatherViewModel>()
    private lateinit var weatherCityText: TextView
    private lateinit var weatherTemperatureText: TextView
    private lateinit var weatherHumidityText: TextView
    private lateinit var counterText: TextView
    private lateinit var weatherIcon: ImageView

    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherCityText = view.findViewById(R.id.weather_city_text)
        weatherTemperatureText = view.findViewById(R.id.weather_temperature_text)
        weatherHumidityText = view.findViewById(R.id.weather_humidity_text)
        counterText = view.findViewById(R.id.counter_text)
        weatherIcon = view.findViewById(R.id.weather_icon)

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
                }
            }
        checkLocationPermissions()
        subscribeOnLiveData()
        viewModel.testFlowable()
    }

    private fun subscribeOnLiveData() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner) { weather ->
            val cityText = requireContext().getString(R.string.home_weather_widget_city_text, weather.name)
            val temperatureText = requireContext().getString(R.string.home_weather_widget_temperature_text, weather.main.temp.toString())
            val humidityText = requireContext().getString(R.string.home_weather_widget_humidity_text, weather.main.humidity.toString())

            weatherCityText.text = cityText
            weatherTemperatureText.text = temperatureText
            weatherHumidityText.text = humidityText

            try {
                val url =
                    "${WeatherWidget.ICON_BASE_URL}/${WeatherWidget.IMAGE_PATH}/${WeatherWidget.W_PATH}/${weather.weather.first().icon}${WeatherWidget.FORMAT_SUFFIX}"
                Glide
                    .with(requireContext())
                    .asBitmap()
                    .load(url)
                    .into(weatherIcon)
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "Error during load weather icon by url")
            }
        }
        viewModel.counterLiveData.observe(viewLifecycleOwner) { counter ->
            counterText.text = counter.toString()
        }
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
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

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
//      Last location.
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location ?: return@addOnSuccessListener
            Log.d(TAG, "LastLocation: latitude = ${location.latitude}, longitude = ${location.longitude}")
            onLocation(location)
        }
    }

    private fun onLocation(location: Location) {
        viewModel.getWeather(location)
    }
}