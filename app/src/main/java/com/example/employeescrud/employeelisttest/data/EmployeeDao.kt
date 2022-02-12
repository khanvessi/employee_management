package com.example.employeescrud.employeelisttest.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.employeescrud.employeelisttest.data.models.Employee

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

    @Query("SELECT * FROM employees WHERE empName like '%' || :userInput || '%'")
    suspend fun simpleNameSearch(userInput: String?): List<Employee>

    @Query("SELECT * FROM employees WHERE empAge = :userInput")
    suspend fun simpleAgeSearch(userInput: Int?): List<Employee>

    @Query("SELECT * FROM employees WHERE empSalary = :userInput")
    suspend fun simpleSalarySearch(userInput: Float?): List<Employee>

    /**
     * This is a suspend method, which will find a List<Employees> upon user's requirements
     */
    @RawQuery
    suspend fun advanceSearch(combineQuery: SupportSQLiteQuery): List<Employee>
}