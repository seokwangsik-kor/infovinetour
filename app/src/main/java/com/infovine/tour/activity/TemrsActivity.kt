package com.infovine.tour.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.infovine.tour.R
import com.infovine.tour.data.AuthResponseData
import com.infovine.tour.data.Resister
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import com.infovine.tour.webview.PopupWebViewFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TemrsActivity : BaseActivity() {

    var checkLayer: LinearLayout? = null
    var checkAll: CheckBox? = null
    var check_btn1: CheckBox? = null
    var check_btn2: CheckBox? = null
    var check_push: CheckBox? = null
    var check_gps: CheckBox? = null
//    var check_ad_push: CheckBox? = null
    var terms_cbtn1: TextView? = null
    var terms_cbtn2: TextView? = null
//    var terms_push_txt: TextView? = null
    var terms_gps_txt: TextView? = null
    var terms_ad_push_txt: TextView? = null
    var terms_btn1: ImageView? = null
    var terms_btn2: ImageView? = null
//    var terms_more_ad_push: ImageView? = null
    var terms_finish: FrameLayout? = null
    var check_finish: CheckBox? = null
    var check_all_layout: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temrs)

        val bundle = Bundle()

        checkLayer = findViewById(R.id.terms_check_all_layer)
        checkAll = findViewById(R.id.terms_check_all_btn)
        check_all_layout = findViewById(R.id.terms_check_all_layout)
        check_btn1 = findViewById(R.id.terms_check_btn1)
        check_btn2 = findViewById(R.id.terms_check_btn2)
//        check_push = findViewById(R.id.terms_check_push)
        check_gps = findViewById(R.id.terms_check_gps)
        check_push = findViewById(R.id.terms_check_ad_push)
        terms_cbtn1 = findViewById(R.id.terms_btn1)
        terms_cbtn2 = findViewById(R.id.terms_btn2)
//        terms_push_txt = findViewById(R.id.terms_check_push_txt)
        terms_gps_txt = findViewById(R.id.terms_check_gps_txt)
        terms_ad_push_txt = findViewById(R.id.terms_check_ad_push_txt)
        terms_btn1 = findViewById(R.id.terms_more_btn1)
        terms_btn2 = findViewById(R.id.terms_more_btn2)
//        terms_more_ad_push = findViewById(R.id.terms_more_ad_push_btn)
        terms_finish = findViewById(R.id.terms_finish_btn)
        check_finish = findViewById(R.id.terms_check_finish)

        checkAll!!.isChecked = false

        terms_btn1!!.setOnClickListener {
            bundle.putString("bundleUrl", Const.TEMRS_1)
            popupFragment!!.arguments = bundle
            popupFragment!!.show(supportFragmentManager, "")
        }
        terms_btn2!!.setOnClickListener {
            bundle.putString("bundleUrl", Const.TEMRS_2)
            popupFragment!!.arguments = bundle
            popupFragment!!.show(supportFragmentManager, "")
        }
//        terms_more_ad_push!!.setOnClickListener {
//            bundle.putString("bundleUrl", Const.TEMRS_3)
//            popupFragment!!.arguments = bundle
//            popupFragment!!.show(supportFragmentManager, "")
//        }
        check_btn1!!.setOnCheckedChangeListener { buttonView, isChecked ->
            finishBtnStatus()
        }
        check_btn2!!.setOnCheckedChangeListener { buttonView, isChecked ->
            finishBtnStatus()
        }
//        check_push!!.setOnCheckedChangeListener { buttonView, isChecked ->
//            finishBtnStatus()
//        }
        check_gps!!.setOnCheckedChangeListener { buttonView, isChecked ->
            finishBtnStatus()
        }
        check_push!!.setOnCheckedChangeListener { buttonView, isChecked ->
            finishBtnStatus()
        }
        terms_cbtn1!!.setOnClickListener {
            check_btn1!!.isChecked = !check_btn1!!.isChecked
            finishBtnStatus()
        }
        terms_cbtn2!!.setOnClickListener {
            check_btn2!!.isChecked = !check_btn2!!.isChecked
            finishBtnStatus()
        }
//        terms_push_txt!!.setOnClickListener {
//            check_push!!.isChecked = !check_push!!.isChecked
//            finishBtnStatus()
//        }
        terms_gps_txt!!.setOnClickListener {
            check_gps!!.isChecked = !check_gps!!.isChecked
            finishBtnStatus()
        }
        terms_ad_push_txt!!.setOnClickListener {
            check_push!!.isChecked = !check_push!!.isChecked
            finishBtnStatus()
        }

        checkLayer!!.setOnClickListener {
            checkAll!!.isChecked = !checkAll!!.isChecked
            checkAll()
        }

        checkAll!!.setOnClickListener {
            checkAll()
        }

        terms_finish!!.setOnClickListener{
            if(check_finish!!.isChecked){
                //1동의, 0비동의
                var join = 0
                var info = 0
//                var push:String? = "0"
                var gps = 0
                var push_yn = 0
                if (check_btn1!!.isChecked) join = 1
                if (check_btn2!!.isChecked) info = 1
                if (check_gps!!.isChecked) gps = 1
                if (check_push!!.isChecked) push_yn = 1
//                if (check_night_push!!.isChecked) night_push = "1"
                SharedPrefsUtils.setIntegerPreference(mContext, Const.PREF_TERMS_JOIN, join)
                SharedPrefsUtils.setIntegerPreference(mContext, Const.PREF_TERMS_INFO, info)
                SharedPrefsUtils.setIntegerPreference(mContext, Const.PREF_TERMS_GPS_YN, gps)
                SharedPrefsUtils.setIntegerPreference(mContext, Const.PREF_TERMS_PUSH_YN, push_yn)







//                SharedPrefsUtils.setStringPreference(mContext, Const.PREF_TERMS_NIGHT_PUSH_YN, night_push)

//                val intent = Intent(mContext, PhoneNumActivity::class.java)
//                startActivity(intent)
//                firebaseAnalytics.logEvent("가입 완료 컨펌"){
//                    param("OS", "Android")
//                }
                postData()
            } else {
                val bundle = Bundle()
                bundle.putString("title", "알림")
                bundle.putString("body", "약관 동의를 체크해 주세요.")
                Alert_Nomal!!.arguments = bundle
                Alert_Nomal!!.show(supportFragmentManager, "")
            }
        }
    }

    fun checkAll() {
        if(checkAll!!.isChecked) {
            check_btn1!!.isChecked = true
            check_btn2!!.isChecked = true
            check_gps!!.isChecked = true
            check_push!!.isChecked = true
//                check_night_push!!.isChecked = true
        } else {
            check_btn1!!.isChecked = false
            check_btn2!!.isChecked = false
            check_gps!!.isChecked = false
            check_push!!.isChecked = false
//                check_night_push!!.isChecked = false
        }
    }

    fun finishBtnStatus() {
//        if (check_btn1!!.isChecked && check_btn2!!.isChecked && check_push!!.isChecked && check_gps!!.isChecked && check_ad_push!!.isChecked) {
        if (check_btn1!!.isChecked && check_btn2!!.isChecked && check_gps!!.isChecked && check_push!!.isChecked) {
            check_finish!!.isChecked = true
            checkAll!!.isChecked = true
            return
        }
        if (check_btn1!!.isChecked && check_btn2!!.isChecked) {
//        if (check_btn2!!.isChecked) {
            check_finish!!.isChecked = true
            checkAll!!.isChecked = false
            return
        }
        if(!check_btn1!!.isChecked) {
            check_finish!!.isChecked = false
        }
        if(!check_btn2!!.isChecked) {
            check_finish!!.isChecked = false
        }
        checkAll!!.isChecked = false
    }

    private fun postData() {

        val resisterData = Resister(
            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.PREF_SNSLOGIN_TYPE, 0),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_SNSLOGIN_ACCESS_TOKEN),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_PUSH_ID),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_SNSLOGIN_OS_TYPE),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_SNSLOGIN_NICKNAME),
            Resister.Agree(
                SharedPrefsUtils.getIntegerPreference(mContext!!, Const.PREF_TERMS_JOIN, 0),
                SharedPrefsUtils.getIntegerPreference(mContext!!, Const.PREF_TERMS_INFO, 0),
                SharedPrefsUtils.getIntegerPreference(mContext!!, Const.PREF_TERMS_PUSH_YN, 0),
                SharedPrefsUtils.getIntegerPreference(mContext!!, Const.PREF_TERMS_GPS_YN, 0)
            )
        )

        val call: Call<AuthResponseData?>? = Auth_retrofitApi!!.register(resisterData)
        call!!.enqueue(object : Callback<AuthResponseData?> {
            override fun onResponse(call: Call<AuthResponseData?>, response: Response<AuthResponseData?>) {

                if(response.isSuccessful) {
                    var code = response.body()!!.resultCode

                    when(code){
                        "S00001" -> {
                            if(!TextUtils.isEmpty(response.body()!!.token)) {
                                SharedPrefsUtils.setStringPreference(mContext!!,Const.PREF_JWT_TOKEN,response.body()!!.token)
                                SharedPrefsUtils.setIntegerPreference(mContext!!,Const.PREF_MEMBER_CD, response.body()!!.memcd)

//                                val intent = Intent(mContext, GlobalEstimateActivity::class.java)
//                                startActivity(intent)
                                val intent = Intent(mContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
//                            error_img!!.visibility = View.GONE
                        }

                        "E9991" -> {
//                            Toast.makeText(mContext, "탈퇴된 계정입니다. 재가입은 7일 후에 가능합니다.", Toast.LENGTH_LONG).show()
                            Toast.makeText(mContext, response.body()!!.resulTMESSAGE, Toast.LENGTH_LONG).show()
                        }
                        "E9992" -> {
                            Toast.makeText(mContext, "사용자약관동의가 거부되었습니다.", Toast.LENGTH_LONG).show()
                        }
                        "E9993" -> {
                            Toast.makeText(mContext, "SNS인증이 실패되었습니다.", Toast.LENGTH_LONG).show()
                        }
                        "E9995" -> {
                            SharedPrefsUtils.setStringPreference(mContext!!,Const.PREF_JWT_TOKEN,response.body()!!.token)
                            SharedPrefsUtils.setIntegerPreference(mContext!!,Const.PREF_MEMBER_CD, response.body()!!.memcd)

                            val intent = Intent(mContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
//                            Toast.makeText(mContext, "이미 가입자입니다.", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(mContext, "잠시후 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                        }
                    }

                } else {
                    SeverErrorDialog()
                }
            }
            override fun onFailure(call: Call<AuthResponseData?>, t: Throwable) {
                SeverErrorDialog()
            }
        })
    }

    override fun onBackPressed() {
        if(popupFragment!=null) {
            if(popupFragment!!.isVisible) {
                popupFragment!!.dismiss()
                return
            }
        }
        finish()
    }
}