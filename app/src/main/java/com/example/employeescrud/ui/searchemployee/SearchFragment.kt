package com.example.employeescrud.ui.searchemployee

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeescrud.R
import com.example.employeescrud.adapters.AddEditEmployeeListener
import com.example.employeescrud.adapters.EmployeeAdapter
import com.example.employeescrud.databinding.FragmentSearchBinding
import com.example.employeescrud.ui.employee.EmployeeFragmentDirections
import org.koin.android.ext.android.inject


class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by inject()
    lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
              ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search, container, false
        )
        binding.search = searchViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val employeeAdapter = EmployeeAdapter(AddEditEmployeeListener({ updatingEmployee ->
//            val action = EmployeeFragmentDirections.actionEmployeeFragmentToAddEmployeeFragment(
//                updatingEmployee
//            )
//            findNavController().navigate(action)
        }, { deletingEmployee ->
            //NOT DELETING
            //searchViewModel.onEmployeeDelete(deletingEmployee, requireContext())
        }))

        binding.apply {
            recViewSearch.apply {
                adapter = employeeAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        searchViewModel.advanceSearchResult.observe(viewLifecycleOwner){
                binding.apply {
                    group2.visibility = View.GONE
                    progressAdvanceSearch.visibility = View.VISIBLE
                    txtAdvanceSearchProgressMessage.visibility = View.VISIBLE
                    Handler().postDelayed({

                        if(!it.isNullOrEmpty()){
                            recViewSearch.visibility = View.VISIBLE
                            progressAdvanceSearch.visibility = View.GONE
                            txtAdvanceSearchProgressMessage.visibility = View.GONE
                        }else{
                            recViewSearch.visibility = View.VISIBLE
                            progressAdvanceSearch.visibility = View.GONE
                            txtAdvanceSearchProgressMessage.visibility = View.VISIBLE
                            txtAdvanceSearchProgressMessage.text = "No Matches Found!"
                        }
                    },1500)
                }
                employeeAdapter.submitList(it)
        }

        searchViewModel.nameError.observe(viewLifecycleOwner){
            binding.edtSearchName.error = it
        }

        searchViewModel.ageError.observe(viewLifecycleOwner){
            binding.edtSearchAge.error = it
        }

        searchViewModel.salaryError.observe(viewLifecycleOwner){
            binding.edtSearchSalary.error = it
        }
    }
}