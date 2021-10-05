package com.example.employeescrud.network

import com.google.gson.annotations.SerializedName

data class Employee(
    val status: String,
    @SerializedName("data")
    val data: List<Dataa>
    ) {
}
data class Dataa(
    val id: Int,
    var employee_name: String,
    var employee_salary: Float,
    var employee_age: Int,
    val profile_image: String

){

}

