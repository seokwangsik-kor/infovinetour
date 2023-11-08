package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class Search(
    val list: List<SearchList>,
    @SerializedName("resulT_MESSAGE")
    val resulTMESSAGE: String,
    val result: String,
    val resultCode: String,
    @SerializedName("transactioN_ID")
    val transactioNID: String
)