package com.example.employeescrud.ui.addeditemployee

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
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
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.DialogInterface
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import java.io.File
import android.provider.MediaStore.Images
import java.io.ByteArrayOutputStream





class AddEditEmployeeFragment : Fragment() {

    private val args by navArgs<AddEditEmployeeFragmentArgs>()
    val addEditEmpViewModel: AddEditEmpViewModel by inject()
    lateinit var binding: FragmentAddeditEmployeeBinding
    private var navController: NavController? = null

    protected val CAMERA_REQUEST = 0
    protected val GALLERY_PICTURE = 1
    private val pictureActionIntent: Intent? = null
    var selectedImagePath: String? = null
    val PERMISSION_CODE = 2000


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

        Log.e("Nav Employee Model:", employee?.empImage.toString())
        //FOR UPDATING
        addEditEmpViewModel.onEditIconClick(employee, requireContext())

        //TODO: CHANGE THE TEXT TO ADD EMPLOYEE
        val navArgsMessage = args.addEmp
        args.addEmp?.let { Log.e("NavArgsMessaage", it) }
        if(navArgsMessage?.contains("create") == true) binding.btnAddEdit.text = "Confirm"

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            addEditEmpViewModel.employeesEvent.collect { event ->
                when (event) {
                    is AddEditEmpViewModel.AddEmployeeEvent.SetImagetoImageView -> {
                        Log.e("Image Selection Clicked", "Clickked")
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
                          //binding.btnAddEdit.setText("Update")
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
            ) == PackageManager.PERMISSION_DENIED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                PERMISSION_CODE
            )
        } else {
            startDialog()
        }
    }

    private fun startDialog() {
        val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(
            activity
        )
        myAlertDialog.setTitle("Upload Pictures Option")
        myAlertDialog.setMessage("How do you want to set your picture?")
        myAlertDialog.setPositiveButton("Gallery",
            DialogInterface.OnClickListener { _, _ ->
                openGallery()
            })
        myAlertDialog.setNegativeButton("Camera",
            DialogInterface.OnClickListener { _, _ ->
                openCamera()
            })
        myAlertDialog.show()
    }

    private fun openGallery() {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 1)
    }

    private fun openCamera() {
        val cameraIntent = Intent(ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 2)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_REQUEST){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera()
            }

            if(grantResults[1] == PackageManager.PERMISSION_GRANTED){
                openGallery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if(data?.data != null){
            val uri = data.data
            Log.e("Gallery Image Uri: ", uri.toString() + "")

                val bitmap = MediaStore.Images.Media.getBitmap(
                    activity?.applicationContext?.contentResolver,
                    uri
                )
                val imagePath = uri?.let { getRealPathFromURI(it) }
                addEditEmpViewModel.employeeImage.value = imagePath
                binding.profileImage.setImageBitmap(bitmap)
            }

        }

        if (requestCode == 2) {
            if (data?.extras?.get("data") != null) {
                val uri = data.extras?.get("data")
                val tempUri: Uri =
                    getImageUri(requireContext(), uri as Bitmap)

                val imagePath = getRealPathFromURI(tempUri)

                val bitmap = BitmapFactory.decodeFile(imagePath)
                binding.profileImage.setImageBitmap(bitmap)

                addEditEmpViewModel.employeeImage.value = imagePath
                Log.e("Camera Image Uri: ", addEditEmpViewModel.employeeImage.value + "")
            }
        }
    }

    //UTILITY FUNCTION
    private fun getRealPathFromURI(uri: Uri): String {
        var path = ""
        if (activity?.applicationContext?.contentResolver != null) {
            val cursor: Cursor? = activity?.applicationContext?.contentResolver
                ?.query(uri, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Image", null)
        return Uri.parse(path)
    }
}