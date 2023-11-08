package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class EstimateList(
    val list: List<EstimateListContent>,
    @SerializedName("resulT_MESSAGE")
    val resulTMESSAGE: String,
    val result: String,
    val resultCode: String,
    @SerializedName("transactioN_ID")
    val transactioNID: String
)