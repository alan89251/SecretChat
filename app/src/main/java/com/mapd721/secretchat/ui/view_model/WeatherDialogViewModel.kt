package com.mapd721.secretchat.ui.view_model

import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.logic.GetDeviceLocationLogic
import com.mapd721.secretchat.logic.GetWeatherLogic
import java.text.SimpleDateFormat
import java.util.*

class WeatherDialogViewModel(
    private val getDeviceLocationLogic: GetDeviceLocationLogic,
    private val getWeatherLogic: GetWeatherLogic
): ViewModel() {
    var condition = MutableLiveData<String>()
    var temperature = MutableLiveData<String>()
    var dateStr = MutableLiveData<String>()
    var location = MutableLiveData<String>()
    var isUpdating = MutableLiveData<Boolean>(true)
    var uiShownInUpdatingVisibility = MediatorLiveData<Int>()
    var uiShownAfterUpdatedVisibility = MediatorLiveData<Int>()

    init {
        uiShownInUpdatingVisibility.addSource(isUpdating) {
            uiShownInUpdatingVisibility.value = if (it) View.VISIBLE else View.GONE
        }
        uiShownAfterUpdatedVisibility.addSource(isUpdating) {
            uiShownAfterUpdatedVisibility.value = if (it) View.GONE else View.VISIBLE
        }
    }

    fun requestTemperature() {
        getDeviceLocationLogic.requestLocation {
            getWeatherLogic.getWeather(it.latitude, it.longitude) { weather ->
                condition.value = weather.condition
                temperature.value = weather.temperature.toString()
                location.value = weather.location
                dateStr.value = SimpleDateFormat("dd MMMM, E")
                    .format(Date())
                isUpdating.value = false
            }
        }
    }
}