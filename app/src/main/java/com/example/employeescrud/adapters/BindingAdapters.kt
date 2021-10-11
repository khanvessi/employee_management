@file:JvmName("BindingUtils")
package com.example.employeescrud.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

import androidx.databinding.InverseMethod
import com.example.employeescrud.R
import com.example.employeescrud.utils.Age
import com.example.employeescrud.utils.Name
import com.example.employeescrud.utils.Salary


@BindingAdapter("imageFromApi")
fun ImageView.imageFromUri(uri: String){
    Glide.with(this.context).load(uri).into(this)
}


@InverseMethod("btnIdToName")
fun nameToButtonId(name: Name): Int{
    return if(name == Name.CONTAINS) R.id.rb_contain_name else R.id.rb_except_name
}

fun btnIdToName(buttonId:Int):Name{
    return if(buttonId == R.id.rb_contain_name) Name.CONTAINS else Name.EXCEPT
}

@InverseMethod("btnIdToAge")
fun ageToButtonId(age: Age): Int{
    return if(age == Age.CONTAINS) R.id.rb_contain_age else R.id.rb_except_age
}

fun btnIdToAge(buttonId:Int):Age{
    return if(buttonId == R.id.rb_contain_age) Age.CONTAINS else Age.EXCEPT
}

@InverseMethod("btnIdToSalary")
fun salaryToButtonId(salary: Salary): Int{
    return if(salary == Salary.CONTAINS) R.id.rb_contain_salary else R.id.rb_except_salary
}

fun btnIdToSalary(buttonId:Int):Salary{
    return if(buttonId == R.id.rb_contain_salary) Salary.CONTAINS else Salary.EXCEPT
}



//
////INVERSE BINDING USING ENUM FOR NAME
//@InverseMethod("buttonIdToNameValue")
//fun nameValueToButtonId(nameType: Name?): Int {
//    var selectedButtonId = -1
//
//    nameType?.run {
//        selectedButtonId = when (this) {
//            Name.CONTAINS -> R.id.rb_contain_name
//            Name.EXCEPT -> R.id.rb_except_name
//        }
//    }
//    return selectedButtonId
//}
//
//fun buttonIdToNameValue(selectedButtonId: Int): Name? {
//    var nameType: Name? = null
//    when (selectedButtonId) {
//        R.id.rb_contain_name -> {
//            nameType = Name.CONTAINS
//        }
//        R.id.rb_except_name -> {
//            nameType = Name.EXCEPT
//        }
//    }
//    return nameType
//}
//
////INVERSE BINDING USING ENUM FOR AGE
//@InverseMethod("buttonIdToAgeValue")
//fun ageValueToButtonId(ageType: Age?): Int {
//    var selectedButtonId = -1
//
//    ageType?.run {
//        selectedButtonId = when (this) {
//            Age.CONTAINS -> R.id.rb_contain_age
//            Age.EXCEPT -> R.id.rb_except_age
//        }
//    }
//
//    return selectedButtonId
//}
//
//fun buttonIdToAgeValue(selectedButtonId: Int): Age? {
//    var ageType: Age? = null
//    when (selectedButtonId) {
//        R.id.rb_contain_age -> {
//            ageType = Age.CONTAINS
//        }
//        R.id.rb_except_age -> {
//            ageType = Age.EXCEPT
//        }
//    }
//    return ageType
//}
//
////INVERSE BINDING USING ENUM FOR SALARY
//@InverseMethod("buttonIdToSalaryValue")
//fun salaryValueToButtonId(salaryType: Salary?): Int {
//    var selectedButtonId = -1
//
//    salaryType?.run {
//        selectedButtonId = when (this) {
//            Salary.CONTAINS -> R.id.rb_contain_salary
//            Salary.EXCEPT -> R.id.rb_except_salary
//        }
//    }
//
//    return selectedButtonId
//}
//
//fun buttonIdToSalaryValue(selectedButtonId: Int): Salary? {
//    var salaryType: Salary? = null
//    when (selectedButtonId) {
//        R.id.rb_contain_salary -> {
//            salaryType = Salary.CONTAINS
//        }
//        R.id.rb_except_salary -> {
//            salaryType = Salary.EXCEPT
//        }
//    }
//    return salaryType
//}

