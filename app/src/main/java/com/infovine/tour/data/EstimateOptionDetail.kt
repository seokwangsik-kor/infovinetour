package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class EstimateOptionDetail(
    val list: List<EstimateTypeA>,
    @SerializedName("resulT_MESSAGE")
    val resulTMESSAGE: String,
    val result: String,
    val resultCode: String,
    @SerializedName("transactioN_ID")
    val transactioNID: String
)
//{
//    data class OptionDetail(
//        val lcCd: Int,
//        val mcCd: Int,
//        val mcImgUrl: String,
//        val mcNm: String,
//        val memo: String,
//        val sortNo: Int
//    )
//}