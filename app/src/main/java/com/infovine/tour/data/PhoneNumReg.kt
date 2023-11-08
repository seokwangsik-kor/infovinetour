package com.infovine.tour.data


data class PhoneNumReg(
    val token: String,
    val userName: String,
    val snsInfo: SnsInfo,
    val cert: Cert
){
    data class Cert(
        val index: Int,
        val num: String
    )
    data class SnsInfo(
        val snsGB: Int,
        val snsAccessToken: String
    )
}
