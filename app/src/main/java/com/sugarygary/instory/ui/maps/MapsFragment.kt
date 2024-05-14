package com.sugarygary.instory.ui.maps

import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.sugarygary.instory.R
import com.sugarygary.instory.data.repository.State
import com.sugarygary.instory.databinding.FragmentMapsBinding
import com.sugarygary.instory.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@AndroidEntryPoint
class MapsFragment : BaseFragment<FragmentMapsBinding>(FragmentMapsBinding::inflate),
    OnMapReadyCallback {
    private val viewModel: MapsViewModel by viewModels()
    private lateinit var map: GoogleMap
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    override fun setupListeners() {
        binding.materialToolbar4.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initData() {
        viewModel.fetchStoriesWithLocation()
    }

    override fun setupMaps(savedInstanceState: Bundle?) {
        with(binding) {
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this@MapsFragment)
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isIndoorLevelPickerEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMapToolbarEnabled = true
        try {
            val success =
                map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_style
                    )
                )
            if (!success) {
                Log.e("MapsFragment", "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("MapsFragment", "Can't find style. Error: ", exception)
        }
        getMyLocation()
    }

    override fun onLowMemory() {
        binding.mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onResume() {
        binding.mapView.onResume()
        super.onResume()
    }


    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
    }

    override fun setupObservers() {
        viewModel.storyResponse.observe(viewLifecycleOwner) {
            when (it) {
                State.Empty -> {}
                is State.Error -> {
                }

                State.Loading -> {}
                is State.Success -> {
                    val builder = LatLngBounds.Builder()
                    it.data.forEach { story ->
                        if (story.lat != null && story.lon != null) {
                            val latlng = LatLng(story.lat.toDouble(), story.lon.toDouble())
                            builder.include(latlng)
                            val formatter =
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            val dateTime = LocalDateTime.parse(story.createdAt, formatter)
                            val currentTime = LocalDateTime.now(ZoneOffset.UTC)
                            val difference = ChronoUnit.SECONDS.between(dateTime, currentTime)
                            val lastSeen: String
                            val seconds = difference % 60
                            val minutes = (difference / 60) % 60
                            val hours = (difference / (60 * 60)) % 24
                            val days = (difference / (60 * 60 * 24)) % 7
                            val weeks = difference / (60 * 60 * 24 * 7)

                            when {
                                weeks > 0 -> lastSeen =
                                    getString(R.string.map_story_week_timestamp, weeks)

                                days > 0 -> lastSeen =
                                    getString(R.string.map_story_day_timestamp, days)

                                hours > 0 -> lastSeen =
                                    getString(R.string.map_story_hour_timestamp, hours)

                                minutes > 0 -> lastSeen = getString(
                                    R.string.map_story_minute_timestamp,
                                    minutes
                                )

                                else -> lastSeen =
                                    getString(R.string.map_story_second_timestamp, seconds)
                            }
                            map.addMarker(
                                MarkerOptions().position(latlng).title(story.name)
                                    .snippet(lastSeen)
                            )
                        }
                    }
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))
                }
            }
        }
    }


}