package com.example.employeescrud.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.employeescrud.utils.Age
import com.example.employeescrud.utils.Name
import com.example.employeescrud.utils.Salary

class SearchModel(
    checkBoxName: Boolean,
    edtName: String,
    name: Name,
    checkBoxAge: Boolean,
    edtAge: String,
    age: Age,
    checkBoxSalary: Boolean,
    edtSalary: String,
    salary: Salary
    ) : BaseObservable(){

    @get:Bindable
    var checkBoxName = checkBoxName
        set(value) {
            if (value != checkBoxName) {
                field = value
                notifyChange()
            }
        }

    @get:Bindable
    var edtName = edtName
        set(value) {
            if (value != edtName) {
                field = value
                notifyChange()
            }
        }

    @get:Bindable
    var name = name
        set(value) {
                field = value
                notifyChange()
        }

    @get:Bindable
    var checkBoxAge = checkBoxAge
        set(value) {
            if (value != checkBoxAge) {
                field = value
                notifyChange()
            }
        }

    @get:Bindable
    var edtAge = edtAge
        set(value) {
            if (value != edtAge) {
                field = value
                notifyChange()
            }
        }

    @get:Bindable
    var age = age
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var checkBoxSalary = checkBoxSalary
        set(value) {
            if (value != checkBoxSalary) {
                field = value
                notifyChange()
            }
        }

    @get:Bindable
    var edtSalary = edtSalary
        set(value) {
            if (value != edtSalary) {
                field = value
                notifyChange()
            }
        }

    @get:Bindable
    var salary = salary
        set(value) {
            field = value
            notifyChange()
        }




}