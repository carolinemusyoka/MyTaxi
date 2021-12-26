package com.carolmusyoka.mytaxi.ui.view

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.carolmusyoka.mytaxi.R
import com.carolmusyoka.mytaxi.data.api.ApiHelper
import com.carolmusyoka.mytaxi.data.api.RetrofitBuilder
import com.carolmusyoka.mytaxi.databinding.FragmentMapBinding
import com.carolmusyoka.mytaxi.ui.adapter.VehicleListAdapter
import com.carolmusyoka.mytaxi.ui.viewmodel.MainViewModel
import com.carolmusyoka.mytaxi.ui.viewmodel.ViewModelFactory
import com.carolmusyoka.mytaxi.utils.Status
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var _binding: FragmentMapBinding
    private val binding get() = _binding
    private lateinit var vehicleListAdapter: VehicleListAdapter
    private lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap


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
                }

            }
        }
    }

    private fun populateData() {
        mainViewModel.getVehicles(p1Lat = 53.694865, p1Lon = 9.757589, p2Lat = 53.394655, p2Lon = 10.099891).observe(viewLifecycleOwner, {
            it?.let { resource ->
                Log.d("TAG", "populateData: $resource")
                when(resource.status){
                    Status.SUCCESS ->{
                        binding.vehiclesRecyclerView.visibility = View.VISIBLE
                        resource.data?.let { data ->
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
}