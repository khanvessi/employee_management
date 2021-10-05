package com.example.employeescrud.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "employees")
//TODO: CHANGE TO SERIALIZABLE
@Parcelize
data class Employee(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerializedName("employee_name")
    val empName: String? = "",
    @SerializedName("employee_salary")
    val empSalary: Float? = 0f,
    @SerializedName("employee_age")
    val empAge: Int? = 0,
    @SerializedName("profile_image")
    val empImage: String? = ""
) : Parcelable {


}