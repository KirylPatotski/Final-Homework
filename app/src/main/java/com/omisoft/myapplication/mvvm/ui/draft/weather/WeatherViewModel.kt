package com.omisoft.myapplication.mvvm.ui.draft.weather

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omisoft.myapplication.mvvm.data.network.model.weather.WeatherResponse
import com.omisoft.myapplication.mvvm.data.network.service.weather.WeatherNetworkImpl
import com.omisoft.myapplication.mvvm.utils.test.RxObservables
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class WeatherViewModel : ViewModel() {

    companion object {
        private const val TAG = "WeatherViewModel"
    }

    private val weatherNetwork = WeatherNetworkImpl.getInstance()
    private val disposables = CompositeDisposable()

    val weatherLiveData = MutableLiveData<WeatherResponse>()
    val counterLiveData = MutableLiveData<Int>()

    fun getWeather(location: Location) {
        disposables.add(
            weatherNetwork.getRxWeatherService()
                .getWeatherByLocation(lat = location.latitude, lon = location.longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ weather ->
                    weatherLiveData.postValue(weather)
                }, { error ->
                    Log.e(TAG, error.message ?: "")
                })
        )
    }

    fun testFlowable() {
        disposables.add(
        RxObservables.getFlowable()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ result ->
                counterLiveData.postValue(result)
                Log.d(TAG, result.toString())
            }, { error ->
                Log.e(TAG, error.message ?: "")
            })
        )
    }

    override fun onCleared() {
        if (!disposables.isDisposed) {
            disposables.dispose()
            disposables.clear()
        }
        super.onCleared()
    }
}