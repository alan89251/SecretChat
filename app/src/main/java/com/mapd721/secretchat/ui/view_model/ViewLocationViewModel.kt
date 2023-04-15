package com.mapd721.secretchat.ui.view_model

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class ViewLocationViewModel: ViewModel() {
    lateinit var latLng: LatLng
}