package com.infovine.tour.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.gson.JsonObject
import com.infovine.tour.R
import com.infovine.tour.data.Estimate
import com.infovine.tour.data.EstimateResponse
import com.infovine.tour.data.LoginResonse
import com.infovine.tour.fragment.NomalAlertFragment
import com.infovine.tour.fragment.UserPhoneConfirmAlertFragment
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EstimateType_B_Activity : BaseActivity(), NomalAlertFragment.NomalAlertCallBackListener, UserPhoneConfirmAlertFragment.UserPhoneConfirmCallBackListener {

    var editBtn : LinearLayout? = null
    var city : TextView? = null
    var startcity : TextView? = null
    var date : TextView? = null
    var adult : TextView? = null
    var child : TextView? = null
    var baby : TextView? = null
    var money : TextView? = null
    var editEtcBtn : LinearLayout? = null
    var backBtn : LinearLayout? = null
    var confirmBtn : LinearLayout? = null
    var etc:EditText? =null

    val PHONE_CONFIRM = 222

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimate_type_b_activity)

        editBtn = findViewById(R.id.estimate_type_b_edit_btn)
        editBtn!!.setOnClickListener {
            val intent = Intent(mContext, GlobalEstimateActivity::class.java)
            startActivity(intent)
            finish()
        }
        city = findViewById(R.id.estimate_type_b_city)
        city!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_DESTINATION_NAME)

        startcity = findViewById(R.id.estimate_type_b_start_city)
        startcity!!.text = "인천"

        date = findViewById(R.id.estimate_type_b_date)
        date!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_FULL_DATE)

        adult = findViewById(R.id.estimate_type_b_adult)
        child = findViewById(R.id.estimate_type_b_child)
        baby = findViewById(R.id.estimate_type_b_baby)
        var adult_cnt = SharedPrefsUtils.getIntegerPreference(mContext, Const.ESTIMATE_ADULT_CNT, 0)
        if (adult_cnt > 0){
            adult!!.text = "어른: $adult_cnt"+ "명"
        }
        var child_cnt = SharedPrefsUtils.getIntegerPreference(mContext, Const.ESTIMATE_CHILD_CNT, 0)
        if (child_cnt > 0){
            child!!.text = "아동: $child_cnt" + "명"
        } else {
            child!!.isVisible = false
        }
        var baby_cnt = SharedPrefsUtils.getIntegerPreference(mContext, Const.ESTIMATE_BABY_CNT, 0)
        if (baby_cnt > 0){
            baby!!.text = "아동: $baby_cnt"+ "명"
        } else {
            baby!!.isVisible = false
        }

        money = findViewById(R.id.estimate_type_b_money)
        money!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_MONEY_WON)

        editEtcBtn = findViewById(R.id.estimate_type_b_edit_etc_btn)

        backBtn = findViewById(R.id.estimate_type_b_bottom_back_btn)
        backBtn!!.setOnClickListener {
            val intent = Intent(mContext, GlobalEstimateActivity::class.java)
            startActivity(intent)
            finish()
        }
        etc = findViewById(R.id.estimate_type_b_edit_etc)

        confirmBtn = findViewById(R.id.estimate_type_b_confirm_btn)
        confirmBtn!!.setOnClickListener {
            SendEstimate()
        }
    }

    fun SendEstimate() {

//        val estimate = Estimate(
//            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.PREF_MEMBER_CD, 0)
//            SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_THEMECD),
//            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_DEST, 0),
//            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_MONEY, 0),
//            SharedPrefsUtils.getStringPreference(mContext!!, etc!!.text.toString()),
//            0,//business
//            0,//mileage
//            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_DATETYPE, 0),
//            SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_STARTDATE),
//            SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_ENDDATE),
//            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_ADULT_CNT, 0),
//            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_CHILD_CNT, 0),
//            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_BABY_CNT, 0),
//            SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_AIRLINE),
//            SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_OPTION)
//        )
//
//        val call: Call<EstimateResponse?>? = Service_retrofitApi!!.Estimate(estimate)
//        call!!.enqueue(object : Callback<EstimateResponse?> {
//            override fun onResponse(call: Call<EstimateResponse?>, response: Response<EstimateResponse?>) {
//
//                if(response.isSuccessful) {
//
//                    //견적전송 > 휴대폰인증 확인(api?) > 인증전 알럿 or 본인번호 확인 알럿 > 노멀 알럿 노출
//                    var code = response.body()!!.resultCode
//                    if (code == "S00001") {
//
//                        UserPhoneConfirm()// 유저 폰 인증 확인
//
//                    } else {
//                        ResponseErrorDialog(code)
//                    }
//                } else {
//                    SeverErrorDialog()
//                }
//            }
//            override fun onFailure(call: Call<EstimateResponse?>, t: Throwable) {
//                SeverErrorDialog()
//            }
//        })
    }

    fun UserPhoneConfirm() {
        val bundle = Bundle()
        bundle.putBoolean("confirm", false)
        Alert_UserPhoneNumber!!.arguments = bundle
        Alert_UserPhoneNumber!!.show(supportFragmentManager, "")
    }

    override fun onAlertCallBack() {
        val intent = Intent(mContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onUserPhoneConfirmAlertCallBack(type:Boolean, btn_select: Boolean) {
        //인증이력 있으면 true
        if (type) {
            if(btn_select) {
                NomalAlert()
            } else {

            }
        } else {
            if(btn_select) {
                val intent = Intent(mContext, PhoneNumActivity::class.java)
                startActivityForResult(intent, PHONE_CONFIRM)
            } else {

            }
        }
    }

    fun NomalAlert() {
        val bundle = Bundle()
        bundle.putString("title", "견적요청 안내")
        bundle.putString("body", "작성 하신 견적서를 바탕으로\n" +
                "여러 여행사에서 고객님께 다양한\n" +
                "견적을 드리도록 하겠습니다.\n" +
                "조금만 기다려 주세요.")
        Alert_Nomal!!.arguments = bundle
        Alert_Nomal!!.show(supportFragmentManager, "")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (PHONE_CONFIRM == requestCode) {
            if(data==null) return
            var bool  = data.getBooleanExtra("phone_confirm", true)
            if(bool) {
                NomalAlert()
            }
        }
    }

}