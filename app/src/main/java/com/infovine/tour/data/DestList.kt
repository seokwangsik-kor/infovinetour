package com.infovine.tour.data


data class DestList(
    val destCd: Int,
    val destNm: String,
    val destImgUrl: String,
    val destDesc: String,
    val area_CD: Int,
    val destRequestCnt: Int,
    val memo: String,
    val areaGb: Int,
    val recommend: Int
)