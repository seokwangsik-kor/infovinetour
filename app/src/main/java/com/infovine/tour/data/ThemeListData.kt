package com.infovine.tour.data


import com.google.gson.annotations.SerializedName
import retrofit2.Call

data class ThemeListData(
    val list: List<mList>,
    @SerializedName("resulT_MESSAGE")
    val resulTMESSAGE: String,
    val result: String,
    val resultCode: String,
    @SerializedName("transactioN_ID")
    val transactioNID: String
) {
    data class mList(
        val sortNo: Int,
        val themeCd: Int,
        val themeImgUrl: String,
        val themeNm: String
    )
}
