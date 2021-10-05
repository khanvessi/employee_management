package com.example.employeescrud.network

import com.google.gson.annotations.SerializedName

data class Employee1(
    val status: String,
    @SerializedName("data")
    val data: Dataaa
) {
}
data class Dataaa(
    val id: Int,
    val employee_name: String,
    val employee_salary: Float,
    val employee_age: Int,
    val profile_image: String

){

}

