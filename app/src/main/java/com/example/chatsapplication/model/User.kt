package com.example.chatsapplication.model

import android.os.Parcelable

data class User(var id:String, var name:String,var email:String,var password:String, var image:String){
    constructor():this("","","","","")
}
