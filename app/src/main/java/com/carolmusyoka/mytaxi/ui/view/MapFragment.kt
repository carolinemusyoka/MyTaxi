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
import com.carolmusyoka.mytaxi.ui.adapter.VehicleListAdapter
import com.carolmusyoka.mytaxi.ui.viewmodel.MainViewModel
import com.carolmusyoka.mytaxi.ui.viewmodel.ViewModelFactory
import com.carolmusyoka.mytaxi.utils.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.gms.maps.model.LatLngBounds






class MapFragment : Fragment(){
    private lateinit var mainViewModel: MainViewModel
    private lateinit var _binding: FragmentMapBinding
    private val binding get() = _binding
    private lateinit var vehicleListAdapter: VehicleListAdapter
    private lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap
    private  var list: List<Poi>? = null



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
    }

    private fun populateData() {
        mainViewModel.getVehicles(p1Lat = 53.694865, p1Lon = 9.757589, p2Lat = 53.394655, p2Lon = 10.099891).observe(viewLifecycleOwner, {
            it?.let { resource ->
                Log.d("TAG", "populateData: $resource")
                when(resource.status){
                    Status.SUCCESS ->{
                        binding.vehiclesRecyclerView.visibility = View.VISIBLE
                        resource.data?.let { data ->
                            mainViewModel.setVehicles(data)
                            Log.d("TAG", "populateDataList: $list")
                            vehicleListAdapter = VehicleListAdapter(data)
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

    private fun addMarkers(googleMap: GoogleMap) {

        list?.forEach {
            val latitude = it.coordinate.latitude
            val longitude = it.coordinate.longitude
            val location = LatLng(latitude, longitude)
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(it.id.toString())
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi))
            )
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            marker?.tag = it.id
        }
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
                    mainViewModel.vehicles.observe(viewLifecycleOwner, {
                        val data = it
                        val listLocation: MutableList<LatLng> = mutableListOf()
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
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(place)
                                    .title("Marker in Hamburg")
                                    .icon(BitmapDescriptorFactory.fromBitmap(getCarBitmap(requireContext())))
                            )
                        }
                    })
                    val locBounds = LatLngBounds(LatLng(53.394655, 10.09989), LatLng(53.694865, 9.75758))
//                    val cameraPosition = CameraPosition.Builder().target(locBounds).zoom(15.5f).build()
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(locBounds, 0))
                }

            }
        }
    }

    private fun getCarBitmap(context: Context): Bitmap {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.taxi_ov)
        return Bitmap.createScaledBitmap(bitmap, 100, 100, false)
    }

}