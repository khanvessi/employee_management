package com.example.employeescrud.ui.addeditemployee

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.employeescrud.data.EmployeeDatabase
import com.example.employeescrud.data.models.Employee
import com.example.employeescrud.network.retrofitsingleton.RetrofitClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditEmpViewModel(private val retrofitClient: RetrofitClient, androidContext: Context) : ViewModel() {

    private val addEmployeeEventChannel = Channel<AddEmployeeEvent>()
    val employeesEvent = addEmployeeEventChannel.receiveAsFlow()
    val dao = EmployeeDatabase.getDatabase(androidContext).employeeDao()

    var employeeId = MutableLiveData<Int>()
    var employeeName = MutableLiveData<String>()
    var employeeSalary = MutableLiveData<String>()
    var employeeAge = MutableLiveData<String>()
    var employeeImage = MutableLiveData<String>()
    var liveDataDb = MutableLiveData<List<Employee>>()
    var addEmployeesLiveData: LiveData<List<Employee>> = liveDataDb

    var errorName = MutableLiveData<String>()
    var errorSalary = MutableLiveData<String>()
    var errorAge = MutableLiveData<String>()

    fun onEditIconClick(employee: Employee?, context: Context) {
        employeeId.value = employee?.id!!
        employeeName.value = employee?.empName!!
        employeeSalary.value = employee?.empSalary.toString()
        employeeAge.value = employee?.empAge.toString()
        employeeImage.value = employee?.empImage!!
        viewModelScope.launch {
            addEmployeeEventChannel.send(AddEmployeeEvent.OpenAddEditFragment)
        }
    }

    fun onAddingAnEmployee(){
        onConfirmBtnClick()
    }

    fun onConfirmBtnClick() {
        //FOR UPDATE
        val employee = employeeId.value?.let {
            Employee(
                it,
                employeeName.value.toString(),
                employeeSalary.value.toString().toFloat(),
                employeeAge.value.toString().toInt(),
                ""
                //employeeImage.value.toString()
            )
        }
        viewModelScope.launch {
            val emp = dao.hasEmployee(employee?.id)
            var exist: Boolean = false
            if(emp != null)  exist = true
            if (exist){
                dao.updateEmployee(employee)
                addEmployeeEventChannel.send(AddEmployeeEvent.OpenEmployeesFragment(2))
            }

            //FOR NEW EMPLOYEE
            else {
                val employee =
                    Employee(
                        empName = employeeName.value.toString(),
                        empSalary = employeeSalary.value.toString().toFloat(),
                        empAge = employeeAge.value.toString().toInt(),
                        empImage = ""
                        //employeeImage.value.toString()
                    )
                var validInput = true

                //TODO: IMPLEMENT REGEX
                if(employeeName.value == "") {
                    errorName.value = "Name is invalid"
                    validInput = false
                }

                if(employeeSalary.value == "0.0") {
                    errorSalary.value = "Salary is invalid"
                    validInput = false
                }

                if(employeeAge.value == "0") {
                    errorAge.value = "Age is invalid"
                    validInput = false
                }

                if(validInput)
                    viewModelScope.launch {
                        val inserted = dao.insertEmployee(employee)
                        if (inserted > -1) {
                            addEmployeeEventChannel.send(AddEmployeeEvent.OpenEmployeesFragment(1))
                        } else {
                            addEmployeeEventChannel.send(AddEmployeeEvent.OpenEmployeesFragment(0))
                        }
                        Log.e(
                            "EmployeeViewModel",
                            "Update UI in thread: ${Thread.currentThread().name} " + inserted
                        )
                    }

            }
        }
    }

    fun onProfileImageClick() {
        viewModelScope.launch {
            addEmployeeEventChannel.send(AddEmployeeEvent.SetImagetoImageView)
        }
    }

    sealed class AddEmployeeEvent() {
        object SetImagetoImageView : AddEmployeeEvent()
        data class OpenEmployeesFragment(val flag: Int): AddEmployeeEvent()
        object OpenAddEditFragment: AddEmployeeEvent()
    }
}







