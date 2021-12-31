package com.carolmusyoka.mytaxi.data.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitBuilder {

    private const val BASE_URL = "https://fake-poi-api.mytaxi.com/"

    private val loggingIn: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
        HttpLoggingInterceptor.Level.BODY
    )
    private val okhttp: OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(loggingIn).build()


    @Singleton
    @Provides
     fun getRetrofit(): Retrofit{
        val builder = Retrofit.Builder()

        return builder
            .baseUrl(BASE_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    fun apiService(): ApiService = getRetrofit().create(ApiService::class.java)

}