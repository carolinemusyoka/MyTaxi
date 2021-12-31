package com.carolmusyoka.mytaxi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carolmusyoka.mytaxi.data.api.ApiHelper
import com.carolmusyoka.mytaxi.data.repository.MainRepository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
abstract class ViewModelFactory(
    private val apiHelper: ApiHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(MainRepository((apiHelper))) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}