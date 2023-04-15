package com.mapd721.secretchat.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.mapd721.secretchat.R
import com.mapd721.secretchat.databinding.DialogWeatherBinding
import com.mapd721.secretchat.logic.GetDeviceLocationLogic
import com.mapd721.secretchat.logic.GetWeatherLogic
import com.mapd721.secretchat.ui.view_model.WeatherDialogViewModel

class WeatherDialogFragment: DialogFragment() {
    private lateinit var binding: DialogWeatherBinding
    private lateinit var vm: WeatherDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = WeatherDialogViewModel(
            GetDeviceLocationLogic(
                requireActivity()
                    .getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
            ),
            GetWeatherLogic(
                requireContext(),
                resources.getString(R.string.weather_service_url),
                resources.getString(R.string.weather_service_api_key)
            )
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogWeatherBinding.inflate(LayoutInflater.from(context))
        binding.vm = vm
        binding.lifecycleOwner = this

        vm.requestTemperature()

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    companion object {
        const val TAG = "WeatherDialog"
    }
}