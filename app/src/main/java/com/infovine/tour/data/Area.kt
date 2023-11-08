package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class Area(
    val list: List<AreaList>,
    @SerializedName("resulT_MESSAGE")
    val resulTMESSAGE: String,
    val result: String,
    val resultCode: String,
    @SerializedName("transactioN_ID")
    val transactioNID: String
)