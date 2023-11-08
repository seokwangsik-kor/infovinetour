package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class Eventlist(
    @SerializedName("cevent_Content_URL")
    val ceventContentURL: String,
    @SerializedName("cevent_Desc")
    val ceventDesc: String,
    @SerializedName("cevent_Img_URL")
    val ceventImgURL: String,
    @SerializedName("cevent_NM")
    val ceventNM: String,
    @SerializedName("nclick_Cnt")
    val nclickCnt: Int,
    @SerializedName("nevent_CD")
    val neventCD: Int,
    @SerializedName("nevent_GB")
    val neventGB: Int,
    @SerializedName("nevent_Loc_GB")
    val neventLocGB: Int,
    val newFlag: Boolean,
    @SerializedName("nuse_DT")
    val nuseDT: String,
    @SerializedName("nuse_Is")
    val nuseIs: Int,
    @SerializedName("reg_DT")
    val regDT: String,
    @SerializedName("ureg_DT")
    val uregDT: String
)