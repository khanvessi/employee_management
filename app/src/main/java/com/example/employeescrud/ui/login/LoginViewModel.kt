package com.example.employeescrud.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeescrud.employeelisttest.data.EmployeeDatabase
import com.example.employeescrud.network.Employee
import com.example.employeescrud.network.retrofitsingleton.RetrofitClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val retrofitClient: RetrofitClient, androidContext: Context) :
    ViewModel() {
    val liveData = MutableLiveData<List<Employee>>()
    val dao = EmployeeDatabase.getDatabase(androidContext).employeeDao()

    var userName = MutableLiveData<String>("shan@gmail.com")
    var password = MutableLiveData<String>("112233")

    var errorEmail = MutableLiveData<String>()
    var errorPassword = MutableLiveData<String>()


    private val employeeEventChannel = Channel<LoginEvent>()
    val employeeEvent = employeeEventChannel.receiveAsFlow()

    fun onClickLogin() {
        var validInput = true
        viewModelScope.launch {
            if (userName.value.isNullOrEmpty()) {
                validInput = false
                errorEmail.value = "Please, provide the email!"
            }
            if(password.value.isNullOrEmpty()) {
                validInput = false
                errorPassword.value = "Please, provide the password"
            }
            if(validInput)
                employeeEventChannel.send(
                    LoginEvent.ShowEmployeesDetail(
                        userName.value.toString(),
                        password.value.toString()
                    )
                )
            //NOT USING CHANNELS FOR SNACKBAR
//            else {
//                employeeEventChannel.send(LoginEvent.ValidateAndShowMessage)
//            }
        }
    }

    sealed class LoginEvent() {
        data class ShowEmployeesDetail(val userName: String, val password: String) : LoginEvent()
        //object ValidateAndShowMessage : LoginEvent()
    }


}