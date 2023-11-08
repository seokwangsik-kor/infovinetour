package com.infovine.tour.data

data class Resister(

    var snsGB: Int,
    var snsAccessToken: String,
    var pushID: String,
    var osType: String,
    var nickName: String,
    val agree: Agree
//    val cert: Cert,
) {
    data class Agree(
        val joinTerms: Int,
        val infoTerms: Int,
//        val adTerms: Int,
        val pushTerms: Int,
        val locationTerms: Int
    )
//    data class Cert(
//        val index: Int,
//        val num: String
//        val adTerms: String
//        val adTerms: String
//    )
}



