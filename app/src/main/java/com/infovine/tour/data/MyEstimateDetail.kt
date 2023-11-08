package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class MyEstimateDetail(
    val list1: List<List1>,
    val list2: List<List2>,
    val list3: List<List3>,
    val list4: Any,
    @SerializedName("resulT_MESSAGE")
    val resulTMESSAGE: String,
    val result: String,
    val resultCode: String,
    @SerializedName("transactioN_ID")
    val transactioNID: String
) {
    data class List1(
        val sortNo: Int,
        val themeNm: String,
        val thmemCd: Int
    )

    data class List2(
        val adultCnt: Int,
        val babyCnt: Int,
        val childCnt: Int,
        val dateGb: Int,
        val dateGbNm: String,
        val destDesc: String,
        val destImgUrl: String,
        val destNm: String,
        val eDate: String,
        val estRegCd: Int,
        val money: Int,
        val regDt: String,
        val reqMemo: String,
        val sDate: String,
        val startPlace: String
    )

    data class List3(
        val estReqCd: Int,
        val estReqMcCd: Int,
        val lcCd: Int,
        val mcCd: Int,
        val mcNm: String
    )
}