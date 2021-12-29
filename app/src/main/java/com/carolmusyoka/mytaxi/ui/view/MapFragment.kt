package com.carolmusyoka.mytaxi.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.carolmusyoka.mytaxi.R
import com.carolmusyoka.mytaxi.data.api.ApiHelper
import com.carolmusyoka.mytaxi.data.api.RetrofitBuilder
import com.carolmusyoka.mytaxi.data.model.Poi
import com.carolmusyoka.mytaxi.databinding.FragmentMapBinding
import com.carolmusyoka.mytaxi.ui.adapter.ItemClickListener
import com.carolmusyoka.mytaxi.ui.adapter.VehicleListAdapter
import com.carolmusyoka.mytaxi.ui.viewmodel.MainViewModel
import com.carolmusyoka.mytaxi.ui.viewmodel.ViewModelFactory
import com.carolmusyoka.mytaxi.utils.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.gms.maps.model.MarkerOptions





class MapFragment : Fragment(), ItemClickListener{
    private lateinit var mainViewModel: MainViewModel
    private lateinit var _binding: FragmentMapBinding
    private val binding get() = _binding
    private lateinit var vehicleListAdapter: VehicleListAdapter
    private lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap
    private  var list: List<Poi>? = null
    private val listLocation: MutableList<LatLng> = mutableListOf()
    private var allMarkers: MutableList<Marker> = mutableListOf()
    private lateinit var locationMarker: Marker

    //TODO
    // code clean-up (the right way)
    // DI
    // TESTING


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = binding.map

        viewLifecycleOwner.lifecycleScope.launch {
            startMap(savedInstanceState)
        }

        activity.let {
            val factory = ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
            mainViewModel = ViewModelProvider(requireActivity(), factory).get(MainViewModel::class.java)
        }
        populateData()
        binding.viewAll.setOnClickListener {
            viewAll()
        }
    }

    private fun viewAll() {
        Log.d("TAG", "startMapList: $list")
        val builder = LatLngBounds.Builder()
        val locBounds = LatLngBounds(LatLng(53.394655, 10.09989), LatLng(53.694865, 9.75758))
        builder.include(locBounds.southwest)
        builder.include(locBounds.northeast)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))

        mainViewModel.vehicles.observe(viewLifecycleOwner, {
            val data = it
            Log.d("TAG", "startMapNewList:$data ")
            data.forEach { poi ->
                val latitude = poi.coordinate.latitude
                val longitude = poi.coordinate.longitude
                val location = LatLng(latitude, longitude)
                Log.d("TAG", "Location: $location")
                listLocation.add(location)
            }
            Log.d("TAG", "LocationArray:$listLocation ")
            listLocation.forEach { place ->
               locationMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(place)
                        .icon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(requireContext())))
                )!!
                allMarkers.add(locationMarker)

            }

        })
    }

    private fun populateData() {
        mainViewModel.getVehicles(53.694865, 9.757589,  53.394655,10.099891).observe(viewLifecycleOwner, {
            it?.let { resource ->
                Log.d("TAG", "populateData: $resource")
                when(resource.status){
                    Status.SUCCESS ->{
                        binding.vehiclesRecyclerView.visibility = View.VISIBLE
                        resource.data?.let { data ->
                            mainViewModel.setVehicles(data)
                            Log.d("TAG", "populateDataList: $list")
                            vehicleListAdapter = VehicleListAdapter(data, this)
                            binding.vehiclesRecyclerView.adapter = vehicleListAdapter
                            binding.vehiclesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        }
                    }
                    Status.LOADING ->{

                    }
                    Status.ERROR ->{
                        Toast.makeText(context, "${resource.message}", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        })
    }

    private suspend fun startMap(savedInstanceState: Bundle?) {
        mMapView.onCreate(null)
        withContext(Dispatchers.Default) {
            mMapView.onResume()
            try {
                MapsInitializer.initialize(requireContext())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            withContext(Dispatchers.Main) {
                mMapView.getMapAsync { mMap ->
                    googleMap = mMap
                    Log.d("TAG", "startMapList: $list")
                    val builder = LatLngBounds.Builder()
                    val locBounds = LatLngBounds(LatLng(53.394655, 10.09989), LatLng(53.694865, 9.75758))
                    builder.include(locBounds.southwest)
                    builder.include(locBounds.northeast)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))

                }

            }
        }
    }

    override fun onCardClick(poi: Poi) {
        // change location bounds and move camera to exact location
        Toast.makeText(context, poi.fleetType, Toast.LENGTH_SHORT).show()
         listLocation.clear()
        Log.d("TAG", "onCardClick: ${poi.fleetType}")
        removeAllMarkers()
        val latitude = poi.coordinate.latitude
        val longitude = poi.coordinate.longitude
        val location = LatLng(latitude, longitude)
        Log.d("TAG", "Location: $location")
        listLocation.add(location)
        val cameraPosition = CameraPosition.Builder().target(location).zoom(15.5f).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        listLocation.forEach { place ->
            locationMarker = googleMap.addMarker(
                MarkerOptions()
                    .position(place)
                    .icon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(requireContext())))
            )!!
            allMarkers.add(locationMarker)

        }
        Toast.makeText(context, "$listLocation", Toast.LENGTH_SHORT).show()
    }

    private fun getCarBitmap(context: Context): Bitmap {
        val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.taxi_ov)
        return Bitmap.createScaledBitmap(bitmap, 100, 100, false)
    }
    private fun removeAllMarkers() {
        for (locationMarker in allMarkers) {
            locationMarker.remove()
        }
        allMarkers.clear()
    }

}