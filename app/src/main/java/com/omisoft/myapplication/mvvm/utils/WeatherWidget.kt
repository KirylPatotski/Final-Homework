package com.omisoft.myapplication.mvvm.utils

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.omisoft.myapplication.R
import com.omisoft.myapplication.mvp.MvpActivity
import com.omisoft.myapplication.mvp.success.SuccessActivity
import com.omisoft.myapplication.mvvm.data.network.model.weather.WeatherResponse
import com.omisoft.myapplication.mvvm.data.network.service.weather.WeatherNetworkImpl
import kotlinx.coroutines.*


/**
 * Implementation of App Widget functionality.
 */
class WeatherWidget : AppWidgetProvider() {
    companion object {
        const val TAG = "WeatherWidget"
        const val ICON_BASE_URL = "https://openweathermap.org"
        const val IMAGE_PATH = "img"
        const val W_PATH = "w"
        const val FORMAT_SUFFIX = ".png"
    }

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        System.currentTimeMillis()
        serviceScope.launch(Dispatchers.IO) {
            val weather = WeatherNetworkImpl.getInstance()
            val response = weather.getCoroutineWeatherService().getWeather()

            serviceScope.launch(Dispatchers.Main) {
                for (appWidgetId in appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId, response)
                }
            }
        }
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
        serviceScope.cancel()
    }

    override fun onAppWidgetOptionsChanged(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle?) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, weather: WeatherResponse) {
        val openSuccessText = context.getString(R.string.home_weather_widget_open_success_text)
        val openMvpText = context.getString(R.string.home_weather_widget_open_mvp_text)
        val cityText = context.resources.getString(R.string.home_weather_widget_city_text, weather.name)
        val temperatureText = context.getString(R.string.home_weather_widget_temperature_text, weather.main.temp.toString())
        val humidityText = context.getString(R.string.home_weather_widget_humidity_text, weather.main.humidity.toString())

        val views = RemoteViews(context.packageName, R.layout.home_weather_widget)
        val pendingOpenSuccess =
            PendingIntent.getActivity(context, 101, Intent(context, SuccessActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        val pendingMvp =
            PendingIntent.getActivity(context, 102, Intent(context, MvpActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        views.setTextViewText(R.id.open_success, openSuccessText)
        views.setTextViewText(R.id.open_mvp, openMvpText)

        views.setTextViewText(R.id.city_text, cityText)
        views.setTextViewText(R.id.temperature_text, temperatureText)
        views.setTextViewText(R.id.humidity_text, humidityText)

        serviceScope.launch(Dispatchers.IO) {
            try {
                val appWidgetTarget = AppWidgetTarget(context, R.id.weather_icon, views, appWidgetId)
                val url = "$ICON_BASE_URL/$IMAGE_PATH/$W_PATH/${weather.weather.first().icon}$FORMAT_SUFFIX"

                Glide
                    .with(context)
                    .asBitmap()
                    .load(url)
                    .into(appWidgetTarget)
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "Error during load weather icon by url")
            }
        }

        views.setOnClickPendingIntent(R.id.open_success, pendingOpenSuccess)
        views.setOnClickPendingIntent(R.id.open_mvp, pendingMvp)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val openSuccess = "Open success"
        val openMvp = "Open mvp"
        val views = RemoteViews(context.packageName, R.layout.home_weather_widget)

        val pendingOpenSuccess =
            PendingIntent.getActivity(context, 101, Intent(context, SuccessActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        val pendingMvp =
            PendingIntent.getActivity(context, 102, Intent(context, MvpActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        views.setTextViewText(R.id.open_success, openSuccess)
        views.setTextViewText(R.id.open_mvp, openMvp)

        views.setOnClickPendingIntent(R.id.open_success, pendingOpenSuccess)
        views.setOnClickPendingIntent(R.id.open_mvp, pendingMvp)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
