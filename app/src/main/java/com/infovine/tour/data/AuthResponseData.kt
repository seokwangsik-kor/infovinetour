package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class AuthResponseData(
    @SerializedName("resulT_MESSAGE")
    val resulTMESSAGE: String,
    val result: String,
    val resultCode: String,
    val token: String,
    @SerializedName("transactioN_ID")
    val transactioNID: String,
    val memcd : Int,
    val hp :String
)