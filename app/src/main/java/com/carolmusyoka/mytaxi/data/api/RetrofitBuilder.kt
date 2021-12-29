package com.carolmusyoka.mytaxi.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    const val BASE_URL = "https://fake-poi-api.mytaxi.com/"

    private val loggingIn: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
        HttpLoggingInterceptor.Level.BODY
    )
    val okhttp: OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(loggingIn).build()


    private fun getRetrofit(): Retrofit{
        val builder = Retrofit.Builder()

        return builder
            .baseUrl(BASE_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}