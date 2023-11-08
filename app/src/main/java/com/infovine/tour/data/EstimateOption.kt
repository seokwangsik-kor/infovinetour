package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class EstimateOption(
    val list: List<EstimateOptionCategory>,
    @SerializedName("resulT_MESSAGE")
    val resulTMESSAGE: String,
    val result: String,
    val resultCode: String,
    @SerializedName("transactioN_ID")
    val transactioNID: String
)
//{
//    data class OptionList(
//        val lcCd: Int,
//        val lcImgUrl: String,
//        val lcNm: String,
//        val memo: String,
//        val sortNo: Int
//    )
//}
