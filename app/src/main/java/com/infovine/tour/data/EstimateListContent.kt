package com.infovine.tour.data


data class EstimateListContent(
    val estRegCd: Int,
    val regDt: String,
    val destNm: String,
    val destImgUrl: String,
    val destDesc: String,
    val dateGb: String,
    val adultCnt: Int,
    val childCnt: Int,
    val babyCnt: Int,
    val themeNm: String
)