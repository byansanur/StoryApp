package com.byandev.storyapp.presentation.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.byandev.storyapp.common.LocationUtils
import com.byandev.storyapp.common.UtilsConnect
import com.byandev.storyapp.common.base.BaseFragment
import com.byandev.storyapp.data.model.dto.Stories
import com.byandev.storyapp.presentation.PresentationViewModel
import com.byandev.storyapp.presentation.R
import com.byandev.storyapp.presentation.SharedViewModel
import com.byandev.storyapp.presentation.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vmadalin.easypermissions.EasyPermissions
import kotlinx.coroutines.flow.map
import org.koin.android.ext.android.inject


class FragmentMaps : BaseFragment<FragmentMapsBinding>() {

    private val presentationViewModel: PresentationViewModel by viewModels()


    private lateinit var gMap: GoogleMap
    private var listStory: MutableList<Stories> = arrayListOf()

    private var myLat = ""
    private var myLon = ""

    private lateinit var locationUtils : LocationUtils
    private lateinit var utilsConnect : UtilsConnect

    private val callback = OnMapReadyCallback { googleMap ->
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true
        gMap.uiSettings.isCompassEnabled = true

        checkPermissionLocation()
    }

    private fun checkPermissionLocation() : Boolean {
        return if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            gMap.isMyLocationEnabled = true
            val a = LatLng(myLat.toDouble(), myLon.toDouble())
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(a, 10f))
            gMap.animateCamera(CameraUpdateFactory.zoomIn())
            gMap.animateCamera(CameraUpdateFactory.zoomTo(10f), 1000, null);

            getStoryLocation()
            true
        } else {
            requestPermissionLocation()
            false
        }
    }

    private fun requestPermissionLocation() {
        if (!EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest
                .permission.ACCESS_COARSE_LOCATION)) {
            EasyPermissions.requestPermissions(this, "Allow the app to access the location",
                1003,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMapsBinding {
        return FragmentMapsBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        locationUtils = LocationUtils(requireContext())
        utilsConnect = UtilsConnect(requireContext())
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
            if (utilsConnect.isConnectedToInternet()) {
                var stories: Stories? = null
                presentationViewModel.getListStory(1).map { pagingData ->
                    pagingData.map { story -> stories = story }
                }

                if (stories != null) {
                    listStory = arrayListOf(stories!!)
                    for (story in listStory) {
                        val latLon = LatLng(story.lat!!, story.lon!!)

                        Glide.with(requireContext())
                            .asBitmap()
                            .load(story.photoUrl)
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

                                    gMap.addMarker(
                                        MarkerOptions()
                                            .position(latLon)
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                            .anchor(0.5f, 1F)

                                    )?.tag = story
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    super.onLoadFailed(errorDrawable)
                                    gMap.addMarker(MarkerOptions()
                                        .position(latLon)
                                        .title("${story.name} - ${story.description}")
                                    )
                                }

                            })
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error - 00xx00",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
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
}