package com.priyam.squareboatapplication.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment

fun ProgressBar.show(){
    visibility=View.VISIBLE
}

fun ProgressBar.hide(){
    visibility=View.GONE
}

fun Activity.blockUI(){
    window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
}

fun Activity.unBlockUI(){
    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
}

fun Context.showToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}




