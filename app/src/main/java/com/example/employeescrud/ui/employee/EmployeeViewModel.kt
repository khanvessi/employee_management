package com.example.employeescrud.ui.employee

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.lifecycle.*
import com.example.employeescrud.employeelisttest.data.EmployeeDatabase
import com.example.employeescrud.network.Employee
import com.example.employeescrud.network.retrofitsingleton.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class EmployeeViewModel(private val retrofitClient: RetrofitClient, androidContext: Context) :
    ViewModel() {

    private val employeeEventChannel = Channel<EmployeeEvent>()
    val employeesEvent = employeeEventChannel.receiveAsFlow()
    val apiSuccessResponse = MutableLiveData<Boolean>()
    val dao = EmployeeDatabase.getDatabase(androidContext).employeeDao()

    //TODO: EMPLOYEE
    var employeeDataClass = com.example.employeescrud.employeelisttest.data.models.Employee()
    val liveDataDb = MutableLiveData<List<com.example.employeescrud.employeelisttest.data.models.Employee>>()
    var employeesLiveData: LiveData<List<com.example.employeescrud.employeelisttest.data.models.Employee>> =
        liveDataDb
    val mediator = MediatorLiveData<Unit>()
    var areEmployeesAdded = MutableLiveData<Boolean>()
    var simpleSearch = MutableLiveData<String>()

    var simpleSearchResult = MutableLiveData<List<com.example.employeescrud.employeelisttest.data.models.Employee>>()

    fun showFullSizedImage(){
        viewModelScope.launch {
            employeeEventChannel.send(EmployeeEvent.ShowFullSizedImage)
        }
    }

    fun onSimpleSearchConfirmClick(){
        val userInput = simpleSearch.value

        val type = checkTheUserInput(userInput)

        viewModelScope.launch {
            if(type == "string") {
                simpleSearchResult.value = dao.simpleNameSearch(userInput)
                Log.e("String: ", "Matched Employees ${Thread.currentThread().name}" + simpleSearchResult.value)
            }

            if(type == "int") {
                simpleSearchResult.value = dao.simpleAgeSearch(userInput?.toInt())
                Log.e("Int: ", "Matched Employees ${Thread.currentThread().name}" + simpleSearchResult.value)
            }

            if(type == "float") {
                simpleSearchResult.value = dao.simpleSalarySearch(userInput?.toFloat())
                Log.e("Float: ", "Matched Employees ${Thread.currentThread().name}" + simpleSearchResult.value)
            }

            employeeEventChannel.send(EmployeeEvent.ShowRecViewForSimpleSearchResult)
        }

    }

    private fun checkTheUserInput(number: String?): String {
        try {
            number?.toInt()
            return "int"
        } catch (e: NumberFormatException) {

        }

        try {
            number?.toFloat()
            return "float"
        } catch (e: NumberFormatException) {
        }

        try {
            number.toString()
            return "string"
        } catch (e: NumberFormatException) {
        }
        return ""
    }


    fun getEmployeesFromDb(context: Context) {
        viewModelScope.launch {
            employeesLiveData = dao.getAllEmployees()
            Handler().postDelayed({
                if (employeesLiveData.value?.isEmpty() == false){
                    areEmployeesAdded.postValue(true)
                    apiSuccessResponse.postValue(true)
                    Log.e("EmployeeViewModel", "If the DB has the data: ${Thread.currentThread().name} "+ employeesLiveData.value)
                }
                else {
                    Log.e("EmployeeViewModel", "If the DB doesn't have the data: ${Thread.currentThread().name} "+ employeesLiveData.value)
                    areEmployeesAdded.postValue(false)
                    apiSuccessResponse.postValue(false)
                }
            }, 1000)
        }
    }

    fun getEmployees(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = retrofitClient.getService(context).getEmployeeList()
                Log.e("EmployeeViewModel", "Fetch data in thread: ${Thread.currentThread().name}")
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        updateDBAfterApiSuccessReponse(result, context)
                        apiSuccessResponse.postValue(true)
                        Log.e(
                            "EmployeeViewModel",
                            "Got the Api Response: ${Thread.currentThread().name}"
                        )
                    } else {
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("EmployeeViewModel", "GetEmployees failed: \n ${ex.message}")
                employeeEventChannel.send(EmployeeEvent.RefreshFragmentInCaseOfFailedApiResponse)
            }
        }
    }

    private fun updateDBAfterApiSuccessReponse(employeesList: Employee, context: Context) {
        employeesList.let {
            val employees: List<com.example.employeescrud.employeelisttest.data.models.Employee> =
                employeesList.data.map {
                    com.example.employeescrud.employeelisttest.data.models.Employee(
                        it.id,
                        it.employee_name,
                        it.employee_salary,
                        it.employee_age,
                        it.profile_image
                    )
                }

            viewModelScope.launch {
                val inserted = dao.insertEmployees(employees)
                Handler().postDelayed({
                    areEmployeesAdded.postValue(true)
                    apiSuccessResponse.postValue(true)
                    Log.e(
                        "Employee list insertion",
                        "Data inserted: ${Thread.currentThread().name} " + inserted
                    )
                },1000)

            }
        }
    }

    fun onEmployeeDelete(
        employee: com.example.employeescrud.employeelisttest.data.models.Employee,
        context: Context
    ) {
        viewModelScope.launch {
            val isDeleted = dao.deleteEmployee(employee)
            if (isDeleted == 1) employeeEventChannel.send(
                EmployeeEvent.SendDeleteEmployeeMessage(
                    1,
                    employee
                )
            )
            else employeeEventChannel.send(EmployeeEvent.SendDeleteEmployeeMessage(0, employee))
        }
    }

    fun undoDeletion(employee: com.example.employeescrud.employeelisttest.data.models.Employee) {
        viewModelScope.launch {
            dao.insertEmployee(employee)
        }
    }

    //FOR CHANNELS
    sealed class EmployeeEvent() {
        data class SendDeleteEmployeeMessage(
            val flag: Int,
            val employee: com.example.employeescrud.employeelisttest.data.models.Employee
        ) : EmployeeEvent()

        object RefreshFragmentInCaseOfFailedApiResponse : EmployeeEvent()
        object ShowRecViewForSimpleSearchResult : EmployeeEvent()
        object ShowFullSizedImage : EmployeeEvent()
    }
}
