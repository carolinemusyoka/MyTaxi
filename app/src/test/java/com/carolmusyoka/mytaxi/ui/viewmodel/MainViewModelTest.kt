package com.carolmusyoka.mytaxi.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.carolmusyoka.mytaxi.data.api.ApiHelper
import com.carolmusyoka.mytaxi.data.model.Poi
import com.carolmusyoka.mytaxi.data.repository.MainRepository
import com.carolmusyoka.mytaxi.utils.Resource
import com.carolmusyoka.mytaxi.utils.Resource.Companion.error
import com.carolmusyoka.mytaxi.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var apiHelper: ApiHelper

    @Mock
    private lateinit var mainRepository: MainRepository

    @Mock
    private lateinit var apiVehicleObserver: Observer<Resource<List<Poi>>>

    @Mock
    private lateinit var poi: List<Poi>

    @Before
    fun setUp() {
        // do something if required
    }


    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message"
            doThrow(RuntimeException(errorMessage))
                .`when`(apiHelper)
                .getVehicles(53.694865, 9.757590,  53.394655,10.099891)
            val viewModel = MainViewModel(mainRepository)
            viewModel.getVehicles(53.694865, 9.757590,  53.394655,10.099891).observeForever(apiVehicleObserver)
            verify(apiHelper).getVehicles(53.694865, 9.757590,  53.394655,10.099891)
            verify(apiVehicleObserver).onChanged(
                error(
                    data = poi ,
                    errorMessage
                )
            )
            viewModel.getVehicles(53.694865, 9.757590,  53.394655,10.099891).removeObserver(apiVehicleObserver)
        }
    }

    @After
    fun tearDown() {
        // do something if required
    }

}