package com.infovine.tour.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.infovine.tour.R
import com.infovine.tour.data.EstimateC
import com.infovine.tour.data.MyEstimateDetail
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class MyEstimateDetailActivity : BaseActivity() {

    var backBtn: ImageView? = null
    var statusImg: ImageView? = null
    var destImg: ImageView? = null
    var dest: TextView? = null
    var destSub: TextView? = null
    var theme: TextView? = null
    var start: TextView? = null
    var date: TextView? = null
    var adult: TextView? = null
    var child: TextView? = null
    var baby: TextView? = null
    var money: TextView? = null
    var etc: TextView? = null
    var air: TextView? = null
    var hotel: TextView? = null
    var bottomBtn1: LinearLayout? = null
    var bottomBtn2: LinearLayout? = null
    private val decimalFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_estimate_detail)

        backBtn = findViewById(R.id.my_estimate_detail_back_btn)
        backBtn!!.setOnClickListener {
            finish()
        }
        statusImg = findViewById(R.id.my_estimate_detail_status_image)
        destImg = findViewById(R.id.my_estimate_detail_image)
        dest = findViewById(R.id.my_estimate_detail_dest_txt)
        destSub = findViewById(R.id.my_estimate_detail_dest_sub_txt)
        theme = findViewById(R.id.my_estimate_detail_thema)
        start = findViewById(R.id.my_estimate_detail_start_city)
        date = findViewById(R.id.my_estimate_detail_date)
        adult = findViewById(R.id.my_estimate_detail_adult)
        child = findViewById(R.id.my_estimate_detail_child)
        baby = findViewById(R.id.my_estimate_detail_baby)
        money = findViewById(R.id.my_estimate_detail_money)
        etc = findViewById(R.id.my_estimate_detail_etc)
        air = findViewById(R.id.my_estimate_detail_air_option)
        hotel = findViewById(R.id.my_estimate_detail_hotel_option)
        bottomBtn1 = findViewById(R.id.my_estimate_detail_bottom_btn1)
        bottomBtn1!!.setOnClickListener {

        }
        bottomBtn2 = findViewById(R.id.my_estimate_detail_bottom_btn2)

        var cd = intent.getIntExtra("cd", 0)
        myEstimateContent(cd)

    }

    fun myEstimateContent(cd:Int) {

        var mData = EstimateC(cd , SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN))

        val call: Call<MyEstimateDetail?>? = Service_retrofitApi!!.EstimateContent(mData)
        call!!.enqueue(object : Callback<MyEstimateDetail?> {
            override fun onResponse(call: Call<MyEstimateDetail?>, response: Response<MyEstimateDetail?>) {

                if(response.isSuccessful) {
                    var code = response.body()!!.resultCode
                    if (code == "S00001") {

                        try {
                            Glide.with(mContext!!).load(response.body()!!.list2[0].destImgUrl).into(destImg!!)
                            dest!!.text = response.body()!!.list2[0].destNm
                            destSub!!.text = response.body()!!.list2[0].destDesc
                            theme!!.text = response.body()!!.list1[0].themeNm
                            start!!.text = response.body()!!.list2[0].startPlace
                            date!!.text = response.body()!!.list2[0].dateGbNm.replace("-", ".")
                            var mAdult = response.body()!!.list2[0].adultCnt
                            var mChild = response.body()!!.list2[0].childCnt
                            var mBaby = response.body()!!.list2[0].babyCnt
                            if (mAdult > 0) {
                                adult!!.isVisible = true
                                adult!!.text = "성인 "+mAdult.toString()+"명"
                            }
                            if (mChild > 0) {
                                child!!.isVisible = true
                                child!!.text = "아동 "+mChild.toString()+"명"
                            }
                            if (mBaby > 0) {
                                baby!!.isVisible = true
                                baby!!.text = "유아 "+mBaby.toString()+"명"
                            }
                            var mMoney = decimalFormat.format(response.body()!!.list2[0].money.toString().replace(",","").toDouble())
                            money!!.text = mMoney
                            var mEtc = response.body()!!.list2[0].reqMemo
                            if (TextUtils.isEmpty(mEtc)) mEtc = "없음"
                            etc!!.text = mEtc

                            var mAir = ""
                            var mHotel = ""
                            var count = response.body()!!.list3.size
                            for (i in 0 until count) {
                                if(response.body()!!.list3[i].lcCd == 1) {//항공사
                                    mAir += response.body()!!.list3[i].mcNm +", "
                                }
                                if(response.body()!!.list3[i].lcCd == 2) {//호텔
                                    mHotel += response.body()!!.list3[i].mcNm + ", "
                                }
                            }

                            mAir = mAir.substring(0, mAir.length-2)
                            mHotel = mHotel.substring(0, mHotel.length-2)


//                            var mAir = response.body()!!.list3[0].mcNm
                            air!!.text= mAir
//                            air!!.text= mAir.substring(0, mAir.length-1)
//                            var mHotel = response.body()!!.list3[1].mcNm
                            hotel!!.text= mHotel
//                            hotel!!.text= mHotel.substring(0, mHotel.length-1)

                        } catch (E:java.lang.Exception) {
                            E.printStackTrace()
                        }

                    } else {
                        ResponseErrorDialog(code)
                        finish()
                    }
                } else {
                    SeverErrorDialog()
                }
            }
            override fun onFailure(call: Call<MyEstimateDetail?>, t: Throwable) {
                SeverErrorDialog()
            }
        })
    }
}
