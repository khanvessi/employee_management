package com.example.employeescrud.network.retrofitsingleton

import com.example.employeescrud.network.EmployeeApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    private val BASE_URL = "http://dummy.restapiexample.com/api/v1/"

    fun getService(): EmployeeApi {

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(HttpLoggingInterceptor().setLevel( HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder()
            //.baseUrl("https://jsonplaceholder.typicode.com")
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(EmployeeApi::class.java)
    }
}