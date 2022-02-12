package com.example.employeescrud.employeelisttest.data

import androidx.lifecycle.LiveData
import com.example.employeescrud.employeelisttest.data.models.Employee

class EmployeeRepository(private val employeeDao: EmployeeDao) {

     fun getEmployees(): LiveData<List<Employee>> {
        return employeeDao.getAllEmployees()
    }

    suspend fun insertEmployee(employee: List<Employee>){
        employeeDao.insertEmployees(employee)
    }

    suspend fun deleteSwipedUser(employee: Employee){
        employeeDao.deleteEmployee(employee)
    }
}