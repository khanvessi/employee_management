package com.example.employeescrud.network

import com.example.employeescrud.network.Dataa
import com.example.employeescrud.network.Employee
import com.example.employeescrud.network.Employee1
import com.example.employeescrud.network.models.DeleteEmp
import retrofit2.http.*

interface EmployeeApi {

    @GET("employees")
 //   @GET("posts")
    suspend fun getEmployeeList(): Employee


    @POST("api/v1/create")
    suspend fun createEmploye(
        @Body data: Dataa
    ): Employee1

    @DELETE("api/v1/delete/{id}")
    suspend fun deleteEmployee(
        @Path("id") id: Int
    ): DeleteEmp
}