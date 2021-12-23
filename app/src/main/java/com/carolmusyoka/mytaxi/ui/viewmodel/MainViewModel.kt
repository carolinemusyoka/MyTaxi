package com.carolmusyoka.mytaxi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.carolmusyoka.mytaxi.data.repository.MainRepository
import com.carolmusyoka.mytaxi.utils.Resource
import kotlinx.coroutines.Dispatchers

class MainViewModel (private val mainRepository: MainRepository): ViewModel() {

    suspend fun getVehicles(p1Lat: Double, p1Lon: Double, p2Lat: Double, p2Lon: Double) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getVehicles(p1Lat, p1Lon, p2Lat, p2Lon).poiList))
        } catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}