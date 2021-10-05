package com.example.employeescrud.utils

import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import android.widget.TextView

import androidx.databinding.InverseBindingAdapter
import java.lang.NumberFormatException


@BindingAdapter("imageFromApi")
fun ImageView.imageFromUri(uri: String){
    Glide.with(this.context).load(uri).into(this)
}

