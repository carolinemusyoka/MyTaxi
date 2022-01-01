package com.carolmusyoka.mytaxi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.carolmusyoka.mytaxi.data.model.Poi
import com.carolmusyoka.mytaxi.data.repository.MainRepository
import com.carolmusyoka.mytaxi.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {

    private var isSessionExpired = false

    val vehicles = MutableLiveData<List<Poi>>()
    val individualVehicle = MutableLiveData<Poi>()

    fun setVehicles(poi: List<Poi>){
        vehicles.value = poi
    }
    fun setOneVehicle(poi: Poi){
        individualVehicle.value = poi
    }

    suspend fun checkSessionExpiry(): Boolean {
        withContext(Dispatchers.IO) {
            delay(5_000) // to simulate a heavy weight operations
            isSessionExpired = true
        }
        return isSessionExpired
    }

   fun getVehicles(p1Lat: Double, p1Lon: Double, p2Lat: Double, p2Lon: Double) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getVehicles(p1Lat, p1Lon, p2Lat, p2Lon).poiList))
        } catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}