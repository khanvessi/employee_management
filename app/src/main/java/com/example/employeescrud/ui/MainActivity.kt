package com.example.employeescrud.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.employeescrud.R
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.example.employeescrud.ui.employee.EmployeeViewModel
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    private val employeeViewModel: EmployeeViewModel by inject()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        //createUser()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)
    }

    private fun createUser() {
        mAuth!!.createUserWithEmailAndPassword("hamza@gmail.com", "hamza123")
            .addOnCompleteListener { registration ->
                if (registration.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "User registered successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Registration Error: " + registration.exception?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        //employeeViewModel.getEmployees(this)
    }
}




