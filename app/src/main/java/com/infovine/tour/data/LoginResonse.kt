package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class LoginResonse(

    val resulTMESSAGE: String,
    val result: String,
    val resultCode: String,
    val token: String,
    val transactioNID: String,
    val hp:String,
    val memcd:Int
)