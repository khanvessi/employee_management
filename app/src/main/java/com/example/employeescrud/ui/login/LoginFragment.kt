package com.example.employeescrud.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.employeescrud.R
import com.example.employeescrud.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collect
import android.app.ProgressDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject


class LoginFragment : Fragment() {

    private var navController: NavController? = null
    private val loginViewModel: LoginViewModel by inject()

    lateinit var binding: FragmentLoginBinding
    var progressDialog: ProgressDialog? = null
    var mAuth: FirebaseAuth? = null

    //lateinit var employeeRepository: EmployeeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.tilPassword.error = ""
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            loginViewModel.employeeEvent.collect {event ->
                when(event){
                        is LoginViewModel.LoginEvent.ShowEmployeesDetail -> {
//                            progressDialog = ProgressDialog(requireContext())
//                            progressDialog!!.setMessage("Verifying...")
//                            progressDialog!!.show()
//                            progressDialog!!.setCanceledOnTouchOutside(false)
//                            mAuth?.signInWithEmailAndPassword(event.userName, event.password)
//                                ?.addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        navController!!.navigate(R.id.action_loginFragment_to_employeeFragment)
//                                        progressDialog!!.dismiss()
//                                    } else {
//                                        progressDialog!!.dismiss()
//                                        binding.tilPassword.error = task.exception?.message
//                                    }
//                                    progressDialog!!.dismiss()
//                                }

                            navController!!.navigate(R.id.action_loginFragment_to_employeeFragment)

                        }



//                    is LoginViewModel.LoginEvent.ValidateAndShowMessage -> {
//                        Snackbar.make(requireView(), "UserName or Password field(s) can't be empty!", Snackbar.LENGTH_LONG)
//                            .setAction("OK") {
//                            }.show()
//                        }


                }
            }
        }

        //VALIDATION ERROR
        loginViewModel.errorEmail.observe(viewLifecycleOwner){
            //EMAIL
            binding.edtUsername.error = it
        }

        loginViewModel.errorPassword.observe(viewLifecycleOwner){
            //Password
            binding.editTextTextPersonName.error = it
        }

//        binding.fabAdd.setOnClickListener(View.OnClickListener {
//            navController!!.navigate(R.id.action_loginFragment_to_addEmployeeFragment)
//        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login, container, false
        )

        binding.employee = loginViewModel
        binding.lifecycleOwner = this
        return binding.root
    }


}