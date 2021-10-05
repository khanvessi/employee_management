package com.example.employeescrud.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.employeescrud.data.models.Employee
import kotlinx.coroutines.CompletableDeferred

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees")
     fun getAllEmployees(): LiveData<List<Employee>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployees(employee: List<Employee>): List<Long>

    @Delete
    suspend fun deleteEmployee(employee: Employee): Int

    @Update
    suspend fun updateEmployee(employee: Employee?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee?): Long

    @Query("SELECT * FROM employees WHERE id = :empId")
    suspend fun hasEmployee(empId: Int?): Employee
}