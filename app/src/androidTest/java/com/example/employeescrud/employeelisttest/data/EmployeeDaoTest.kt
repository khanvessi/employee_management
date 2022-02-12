package com.example.employeescrud.employeelisttest.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.employeescrud.employeelisttest.data.models.Employee
import com.example.employeescrud.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class EmployeeDaoTest {

    //THIS WILL EXECUTE THE CODE SYNCHRONOUSLY ONE AFTER ANOTHER - THAT IS TO SOLVE THE LIVEDATA PROBLEM CUZ IT RUNS ASYNCHRONOUSLY
    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: EmployeeDatabase
    private lateinit var dao: EmployeeDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EmployeeDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.employeeDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertEmployee() = runBlockingTest {

        val employee = Employee(1, "Khan",1000f,32,"image")
        dao.insertEmployee(employee)

        //LIFE DATA RUNS ASYNCHRONOUS, LIST OF EMPLOYEES ITEMS THIS OBSERVE FUNCTION RETURNS
        val allEmployees = dao.getAllEmployees().getOrAwaitValue()

        assertThat(allEmployees).contains(employee)

    }


    @Test
    fun deleteEmployee()
         = runBlockingTest {
            val employee = Employee(1, "Khan",20044f, 23,"")

            dao.insertEmployee(employee)
            dao.deleteEmployee(employee)

            val allEmployees = dao.getAllEmployees().getOrAwaitValue()
            assertThat(allEmployees).doesNotContain(employee)
        }

}