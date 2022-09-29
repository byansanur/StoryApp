package com.byandev.storyapp.presentation.map_view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.byandev.storyapp.R
import com.byandev.storyapp.databinding.FragmentMapsBinding
import com.byandev.storyapp.di.LocationUtils
import com.byandev.storyapp.presentation.SharedViewModel
import com.byandev.storyapp.utils.Resources
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by viewModels()

    private lateinit var gMap: GoogleMap

    private var myLat = ""
    private var myLon = ""

    @Inject
    lateinit var locationUtils: LocationUtils

    private val callback = OnMapReadyCallback { googleMap ->
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true
        gMap.uiSettings.isCompassEnabled = true

        gMap.isMyLocationEnabled = true
        val a = LatLng(myLat.toDouble(), myLon.toDouble())
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(a, 15f))
        gMap.animateCamera(CameraUpdateFactory.zoomIn())
        gMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 1000, null);

        getStoryLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenuHost()

        if (locationUtils.canGetLocation) {
            if (!locationUtils.isGPSEnabled)
                Toast.makeText(requireContext(), "Please activate your gps!", Toast.LENGTH_SHORT).show()
            else {
                myLat = getLatitude()
                myLon = getLongitude()
            }
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun getStoryLocation() {
        lifecycleScope.launchWhenCreated {
            viewModel.getListStoryLocation().observe(viewLifecycleOwner) {
                when(it) {
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        for (i in it.data?.listStory?.indices!!) {
                            val latLon = LatLng(it.data.listStory[i].lat!!, it.data.listStory[i].lon!!)

                            Glide.with(requireContext())
                                .asBitmap()
                                .load(it.data.listStory[i].photoUrl)
                                .dontTransform()
                                .centerCrop()
                                .into(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        val scale = requireContext().resources.displayMetrics.density
                                        val pixels = (50 * scale + 0.5f).toInt()
                                        val bitmap = Bitmap.createScaledBitmap(
                                            resource,
                                            pixels,
                                            pixels,
                                            true
                                        )

                                        gMap.addMarker(MarkerOptions()
                                            .position(latLon)
                                            .title("${it.data.listStory[i].name} - ${it.data.listStory[i].description}")
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                            .anchor(0.5f, 1F)
                                        )
                                    }

                                    override fun onLoadFailed(errorDrawable: Drawable?) {
                                        super.onLoadFailed(errorDrawable)
                                        gMap.addMarker(MarkerOptions()
                                            .position(latLon)
                                            .title("${it.data.listStory[i].name} - ${it.data.listStory[i].description}")
                                        )
                                    }

                                })
                        }
                    }
                    is Resources.Error -> {}
                }
            }
        }
    }

    private fun setupMenuHost() {
        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.map_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.normal_type -> {
                        gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                        return true
                    }
                    R.id.satellite_type -> {
                        gMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        return true
                    }
                    R.id.terrain_type -> {
                        gMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        return true
                    }
                    R.id.hybrid_type -> {
                        gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun getLatitude(): String {
        var lati = ""
        if (locationUtils.canGetLocation) {
            lati = locationUtils.latitude.toString()
        }
        return lati
    }

    private fun getLongitude(): String {
        var long = ""
        if (locationUtils.canGetLocation) {
            long = locationUtils.longitude.toString()
        }
        return long
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}