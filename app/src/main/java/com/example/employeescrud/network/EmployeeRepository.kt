package com.example.employeescrud.network

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//class EmployeeRepository(val applicationContext: Context) {
//
//    fun employees(liveData: MutableLiveData<List<Employee>>) = RetrofitClient.employeeApi.getEmployeeList()
//        .enqueue(object : Callback<List<Employee>>{
//            override fun onResponse(
//                call: Call<List<Employee>>,
//                response: Response<List<Employee>>
//            ) {
//                liveData.value = response.body()
//            }
//
//            override fun onFailure(call: Call<List<Employee>>, t: Throwable) {
//
//            }
//
//        })
////        .enqueue(object: Callback<List<Employee>> {
////            override fun onResponse(
////                call: Call<List<Employee>>,
////                response: Response<List<Employee>>
////            ) {
////                liveData.value = response.body()
////            }
////
////            override fun onFailure(call: Call<List<Employee>>, t: Throwable) {
////                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
////            }
////        })
//
//}