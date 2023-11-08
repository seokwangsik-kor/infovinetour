package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class Dest(
    val list: List<DestList>,
    @SerializedName("resulT_MESSAGE")
    val resulTMESSAGE: String,
    val result: String,
    val resultCode: String,
    @SerializedName("transactioN_ID")
    val transactioNID: String
)