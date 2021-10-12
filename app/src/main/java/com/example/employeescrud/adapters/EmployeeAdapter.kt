package com.example.employeescrud.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.employeescrud.data.models.Employee
import com.example.employeescrud.databinding.EmployeeItemBinding

class EmployeeAdapter(private val addEditEmployeeListener: AddEditEmployeeListener) : androidx.recyclerview.widget.ListAdapter<Employee, EmployeeAdapter.EmployeeViewHolder>(
    DiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = EmployeeItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val currentItem = getItem(position)
        //THESE LISTENER WILL HANDLE THE CLICK EVENTS (NO LOGIC)
        holder.bind(currentItem, addEditEmployeeListener)
    }

    class EmployeeViewHolder(private val binding: EmployeeItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(
            employee: Employee,
            listener: AddEditEmployeeListener,
        ){
            binding.listener = listener
            //TODO: USE employeViewModel?.employeeDataClass
            binding.employee = employee
            binding.executePendingBindings()


        }

//        private fun showFullSizedImage() {
//            var isImageFitToScreen: Boolean = false
//            Log.e("Clicked, ")
//            binding.apply {
//                if (isImageFitToScreen) {
//                    isImageFitToScreen = false
//                    itemImgView.layoutParams = FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.WRAP_CONTENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT
//                    )
//                    itemImgView.adjustViewBounds = true
//                } else {
//                    isImageFitToScreen = true
//                    itemImgView.layoutParams = FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.MATCH_PARENT,
//                        FrameLayout.LayoutParams.MATCH_PARENT
//                    )
//                    itemImgView.scaleType = ImageView.ScaleType.FIT_XY
//                }
//            }
        }
    }



    class DiffCallback : DiffUtil.ItemCallback<Employee>(){
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }
    }


class AddEditEmployeeListener(val clickListener: (employee: Employee) -> Unit, val onDeleteClickListener: (employee: Employee) -> Unit){
    fun onClick(updateEmployee: Employee) = clickListener(updateEmployee)
    fun onDeleteClick(deleteEmployee: Employee) = onDeleteClickListener(deleteEmployee)
}