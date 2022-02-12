package com.example.employeescrud.ui.searchemployee

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.employeescrud.employeelisttest.data.EmployeeDatabase
import com.example.employeescrud.employeelisttest.data.models.Employee
import com.example.employeescrud.employeelisttest.data.models.SearchModel
import com.example.employeescrud.network.retrofitsingleton.RetrofitClient
import com.example.employeescrud.utils.Age
import com.example.employeescrud.utils.Name
import com.example.employeescrud.utils.Salary
import kotlinx.coroutines.launch

class SearchViewModel(private val retrofitClient: RetrofitClient, androidContext: Context) :
    ViewModel() {


    val dao = EmployeeDatabase.getDatabase(androidContext).employeeDao()
    val advanceSearchResult = MutableLiveData<List<Employee>>()

    var nameError = MutableLiveData<String>()
    var salaryError = MutableLiveData<String>()
    var ageError = MutableLiveData<String>()

    val searchModel = SearchModel(
        true, "", Name.CONTAINS, true, "", Age.CONTAINS, true,
        "", Salary.CONTAINS
    )

    fun onAdvanceSearchConfirmClick() {

        var validSearch = true


//        Log.e(
//            "CheckBox: ",
//            "${searchModel.name}  " + "/n" +
//                    "${Name.CONTAINS.ordinal}  " + "/n" +
//                    "${searchModel.age}  " + "/n" +
//                    "${Age.CONTAINS.ordinal}  " + "/n" +
//                    "${searchModel.salary}  " + "/n" +
//                    "${Salary.CONTAINS.ordinal}  " + "/n" +
//                    "${searchModel.checkBoxName}  " + "/n"
//        )

            var nClause = ""
            var sClause = ""
            var aClause = ""

            var queryStatement = "select * from employees"

            //FOR NAME
            if (searchModel.checkBoxName) {
                if (searchModel.edtName == "") {
                    nameError.value = "Invalid Name!"
                    validSearch = false
                } else {
                    if (searchModel.name == Name.CONTAINS) {
                        nClause = nClause + " empName LIKE " + "'%" + searchModel.edtName + "%'"
                    } else {
                        nClause = nClause + " empName NOT LIKE " + "'%" + searchModel.edtName + "%'"
                    }
                }
            }


            if (searchModel.checkBoxAge) {
                if (searchModel.edtAge == "") {
                    ageError.value = "Invalid Age!"
                    validSearch = false
                } else {
                    if (searchModel.age == Age.CONTAINS) {
                        aClause = aClause + " empAge = " + searchModel.edtAge.toInt()
                    } else {
                        aClause = aClause + " empAge != " + searchModel.edtAge.toInt()
                    }
                }
            }


            if (searchModel.checkBoxSalary) {
                if (searchModel.edtSalary == "") {
                    salaryError.value = "Invalid Salary!"
                    validSearch = false
                } else {
                    if (searchModel.salary == Salary.CONTAINS) {
                        sClause = sClause + " empSalary = " + searchModel.edtSalary.toFloat()
                    } else {
                        sClause = sClause + " empSalary != " + searchModel.edtSalary.toFloat()
                    }
                }
            }

        if(validSearch){

            var whereClause = ""

            if (nClause != "") {
                whereClause = whereClause + nClause
            }

            if (whereClause != "" && aClause != "")
                whereClause = whereClause + " OR "

            if (aClause != "")
                whereClause = whereClause + aClause

            if (whereClause != "" && sClause != "")
                whereClause = whereClause + " OR "

            if (sClause != "")
                whereClause = whereClause + sClause


            whereClause = " WHERE " + whereClause


            val combineQuery = queryStatement + whereClause

            Log.e("combine query: ", combineQuery)

            viewModelScope.launch {
                val query = SimpleSQLiteQuery(combineQuery)
                advanceSearchResult.value = dao.advanceSearch(query)
                Log.e("combine query result: ", " " + advanceSearchResult.value)

            }
        }
    }
    fun onEmployeeDelete(deletingEmployee: Employee, requireContext: Context) {

    }


}