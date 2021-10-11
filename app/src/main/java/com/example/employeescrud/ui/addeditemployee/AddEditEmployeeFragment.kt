package com.example.employeescrud.ui.addeditemployee

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.employeescrud.R
import com.example.employeescrud.databinding.FragmentAddeditEmployeeBinding
import com.example.employeescrud.utils.exhaustive
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class AddEditEmployeeFragment : Fragment() {

    private val args by navArgs<AddEditEmployeeFragmentArgs>()
    val addEditEmpViewModel: AddEditEmpViewModel by inject()
    lateinit var binding: FragmentAddeditEmployeeBinding
    private var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_addedit_employee, container, false
        )
        binding.addEmployeeViewModel = addEditEmpViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val employee = args.employee

        //FOR UPDATING
        addEditEmpViewModel.onEditIconClick(employee, requireContext())

        //TODO: CHANGE THE TEXT TO ADD EMPLOYEE
        val navArgsMessage = args.addEmp
        if(navArgsMessage.equals("create")) binding.signin.text = "Add Employee"

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            addEditEmpViewModel.employeesEvent.collect { event ->
                when (event) {
                    is AddEditEmpViewModel.AddEmployeeEvent.SetImagetoImageView -> {
                        checkPermissions()
                    }
                    is AddEditEmpViewModel.AddEmployeeEvent.OpenEmployeesFragment -> {
                        if(event.flag == 1){
                            Snackbar.make(requireView(), "Employee added successfully!", Snackbar.LENGTH_LONG)
                                .setAction("OK") {
                                }.show()
                            navController!!.navigate(R.id.action_addEmployeeFragment_to_employeeFragment)
                        }else if(event.flag == 0){
                            Snackbar.make(requireView(), "Something went wrong, Please try again", Snackbar.LENGTH_LONG)
                                .setAction("OK") {
                                }.show()
                        }else{
                            Snackbar.make(requireView(), "Employee has been updated!", Snackbar.LENGTH_LONG)
                                .setAction("OK") {
                                    navController!!.navigate(R.id.action_addEmployeeFragment_to_employeeFragment)
                                }.show()
                        }
                    }
                    is AddEditEmpViewModel.AddEmployeeEvent.OpenAddEditFragment -> {
                          binding.signin.setText("Update")
                    }
                }.exhaustive
            }
        }

        addEditEmpViewModel.errorAge.observe(viewLifecycleOwner){
            binding.age.error = it
        }
        addEditEmpViewModel.errorName.observe(viewLifecycleOwner){
            binding.name.error = it
        }
        addEditEmpViewModel.errorSalary.observe(viewLifecycleOwner){
            binding.salary.error = it
        }
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                2000
            );
        } else {
            openGallery()
        }
    }

    private fun openGallery() {
            val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(cameraIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 2000){
            openGallery()
        }

        if(requestCode == 1){
            val uri = data?.data
            addEditEmpViewModel.employeeImage.value = uri.toString()
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.applicationContext?.contentResolver, uri )
            binding.profileImage.setImageBitmap(bitmap)
        }
    }
}