package com.infovine.tour.data


data class Estimate(
//    val memCd: Int,
    val token: String,
    val themeCdList: String,
    val destCd: Int,
    val money: Int,
    val regMemo: String,
    val business: Int,
    val mileage: Int,
    val dateGb: Int,
    val sDate: String,
    val eDate: String,
    val adultCnt: Int,
    val childCnt: Int,
    val babyCnt: Int,
    val mcList: String,
    val optionCodeList: String,
    val startPlace: String
)