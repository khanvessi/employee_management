package com.example.employeescrud.network.retrofitsingleton

import com.example.employeescrud.ui.addeditemployee.AddEditEmpViewModel
import com.example.employeescrud.ui.employee.EmployeeViewModel
import com.example.employeescrud.ui.login.LoginViewModel
import com.example.employeescrud.ui.searchemployee.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EmployeeViewModel(get(), androidContext()) }
    viewModel { AddEditEmpViewModel(get(), androidContext()) }
    viewModel { LoginViewModel(get(), androidContext()) }
    viewModel { SearchViewModel(get(), androidContext()) }
}


val networkingModule = module {
    single { RetrofitClient() }
}