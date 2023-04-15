package com.mapd721.secretchat.ui

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mapd721.secretchat.R
import com.mapd721.secretchat.databinding.FragmentViewLocationBinding
import com.mapd721.secretchat.ui.view_model.ViewLocationViewModel

class ViewLocationFragment : Fragment() {
    private lateinit var binding: FragmentViewLocationBinding
    private lateinit var vm: ViewLocationViewModel

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.addMarker(
            MarkerOptions()
                .position(vm.latLng)
                .title(vm.latLng.toString())
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vm.latLng, 15f))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewLocationViewModel()
        arguments?.let {
            val latLngStrArray = it.getString(ARG_LAT_LNG)!!.split(",")
            vm.latLng = LatLng(
                latLngStrArray[0]!!.toDouble(),
                latLngStrArray[1]!!.toDouble()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)
    }

    companion object {
        private const val ARG_LAT_LNG = "latLng"

        @JvmStatic
        fun newInstance(latLng: String) {
            ViewLocationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LAT_LNG, latLng)
                }
            }
        }
    }
}