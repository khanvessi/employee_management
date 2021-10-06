package com.example.employeescrud.ui.employee

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.employeescrud.R
import com.example.employeescrud.adapters.AddEditEmployeeListener
import com.example.employeescrud.adapters.EmployeeAdapter
import com.example.employeescrud.data.models.Employee
import com.example.employeescrud.databinding.FragmentEmployeeBinding
import com.example.employeescrud.utils.exhaustive

import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class EmployeeFragment : Fragment() {

    private val employeeViewModel: EmployeeViewModel by inject()
    lateinit var binding: FragmentEmployeeBinding

    private var navController: NavController? = null
    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employee, container, false
        )
        binding.employee = employeeViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

//        progressDialog = ProgressDialog(requireContext())
//        progressDialog!!.setMessage("Fetching the data...")
//        progressDialog!!.show()
//        progressDialog!!.setCanceledOnTouchOutside(false)

        employeeViewModel.getEmployeesFromDb(requireContext())
        employeeViewModel.areEmployeesAdded.observe(viewLifecycleOwner) {
            if (!it) {
                employeeViewModel.getEmployees(requireContext())
                Log.e("areEmployeesAdded", "Are Employees Added? No: ${Thread.currentThread().name} "+ it)
            }else{
                //progressDialog!!.dismiss()
                Log.e("areEmployeesAdded", "Are Employees Added? Yes: ${Thread.currentThread().name} "+ it)
                binding.shimmerLayout.stopShimmerAnimation()
                binding.shimmerLayout.visibility = View.GONE
            }
        }


        val employeeAdapter = EmployeeAdapter(AddEditEmployeeListener({ updatingEmployee ->
            val action = EmployeeFragmentDirections.actionEmployeeFragmentToAddEmployeeFragment(
                updatingEmployee
            )
            findNavController().navigate(action)
        }, { deletingEmployee ->
            employeeViewModel.onEmployeeDelete(deletingEmployee, requireContext())
        }))

        binding.apply {
            recViewUsers.apply {
                shimmerLayout.startShimmerAnimation()
                adapter = employeeAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val employee = employeeAdapter.currentList[viewHolder.adapterPosition]
                    AlertDialog.Builder(requireContext())
                        .setTitle("Confirm Deletion")
                        .setMessage("Do you really want to delete this employee?")
                        .setNegativeButton("NO") { _, _ ->
                            employeeViewModel.undoDeletion(employee)
                        }
                        .setPositiveButton("YES") { _, _ ->
                            employeeViewModel.onEmployeeDelete(employee, requireContext())
                        }
                        .create()
                        .show()
                }
            }).attachToRecyclerView(recViewUsers)
        }

        employeeViewModel.employeesLiveData.observe(viewLifecycleOwner) {
            employeeAdapter.submitList(it)
        }

        binding.fabAddTask.setOnClickListener(View.OnClickListener {
            val action = EmployeeFragmentDirections.actionEmployeeFragmentToAddEmployeeFragment(
                Employee(
                    0,
                    "",
                    0f,
                    0,
                    ""
                ),
                "create"
            )
            findNavController().navigate(action)
        })

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            employeeViewModel.employeesEvent.collect { event ->
                when (event) {
                    is EmployeeViewModel.EmployeeEvent.SendDeleteEmployeeMessage -> {
                        if (event.flag == 1) {
                            Snackbar.make(
                                requireView(),
                                "successfully! deleted Records!  ",
                                Snackbar.LENGTH_LONG
                            )
                                .setAction("UNDO") {
                                    employeeViewModel.undoDeletion(event.employee)
                                }.show()
                            //navController!!.navigate(R.id.action_addEmployeeFragment_to_employeeFragment)
                        } else {
                            Snackbar.make(
                                requireView(),
                                "Something went wrong!",
                                Snackbar.LENGTH_LONG
                            )
                                .setAction("OK") {
                                }.show()
                        }
                    }
                    EmployeeViewModel.EmployeeEvent.RefreshFragmentInCaseOfFailedApiResponse -> {
                        recallRetrofit()
                    }
                }.exhaustive
            }
        }
    }

    private fun recallRetrofit() {
        employeeViewModel.apiSuccessResponse.observe(viewLifecycleOwner) { response ->
            if (!response) {
                Log.e("apisuccessResponse", "isSuccuesful?: ${Thread.currentThread().name} "+ response)
                employeeViewModel.getEmployees(requireContext())
            }else{
                //progressDialog!!.dismiss()
            }
        }
    }


}