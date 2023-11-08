package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class ApiVersion(
    val list: List<VersionList>,
    @SerializedName("resulT_MESSAGE")
    val resulTMESSAGE: String,
    val result: String,
    val resultCode: String,
    @SerializedName("transactioN_ID")
    val transactioNID: String
)
//{
//    data class verList(
//        val areaVer: String,
//        val destVer: String,
//        val lcVer: String,
//        val mcVer: String,
//        val optDetailVer: String,
//        val optVer: String,
//        val themeVer: String
//    )
//}