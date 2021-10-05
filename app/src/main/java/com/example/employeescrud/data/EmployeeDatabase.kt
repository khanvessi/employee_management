package com.example.employeescrud.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.employeescrud.data.models.Employee

@Database(entities = [Employee::class], version = 1)
abstract class EmployeeDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao

    companion object {
        private var INSTANCE: EmployeeDatabase? = null
        fun getDatabase(context: Context): EmployeeDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        EmployeeDatabase::class.java,
                        "employee_database"
                    )
                        //.createFromAsset("quotes.db")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}