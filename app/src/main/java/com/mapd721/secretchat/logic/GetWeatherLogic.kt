package com.mapd721.secretchat.logic

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class GetWeatherLogic(
    private val context: Context,
    private val baseUrl: String,
    private val apiKey: String
) {
    private val queue: RequestQueue

    init {
        queue = Volley.newRequestQueue(context)
    }

    fun getTemperature(
        latitude: Double,
        longitude: Double,
        onResult: (Double) -> Unit // arg1: temperature
    ) {
        val url = "$baseUrl?lat=$latitude&lon=$longitude&units=metric&appid=$apiKey"
        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val temperature = it.getJSONObject("main")
                  .getDouble("temp")
                onResult(temperature)
            },
            {
                Log.e("Get temperature", "Error: it")
            }
        )
        queue.add(request)
    }
}