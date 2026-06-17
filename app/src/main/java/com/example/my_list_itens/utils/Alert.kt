package com.example.my_list_itens.utils

import android.app.AlertDialog
import android.content.Context


fun Context.Alert(msg : String, title : String){
    AlertDialog.Builder(  this)
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton( "ok",  null)
        .show()
}