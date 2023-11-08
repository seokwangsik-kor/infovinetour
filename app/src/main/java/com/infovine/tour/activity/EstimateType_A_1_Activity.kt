package com.infovine.tour.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.infovine.tour.R
import com.infovine.tour.data.Estimate
import com.infovine.tour.data.Login
import com.infovine.tour.data.LoginResonse
import com.infovine.tour.fragment.NomalAlertFragment
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EstimateType_A_1_Activity : BaseActivity(), NomalAlertFragment.NomalAlertCallBackListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimate_type_a1)

        editBtn = findViewById(R.id.estimate_type_a_1_edit_btn)
        editBtn!!.setOnClickListener {
            val intent = Intent(mContext, GlobalEstimateActivity::class.java)
            startActivity(intent)
            finish()
        }
        city = findViewById(R.id.estimate_type_a_1_city)
        city!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_DESTINATION_NAME)

        startcity = findViewById(R.id.estimate_type_a_1_start_city)
        startcity!!.text = "인천"

        date = findViewById(R.id.estimate_type_a_1_date)
        date!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_FULL_DATE)

        adult = findViewById(R.id.estimate_type_a_1_adult)
        child = findViewById(R.id.estimate_type_a_1_child)
        baby = findViewById(R.id.estimate_type_a_1_baby)
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

        money = findViewById(R.id.estimate_type_a_1_money)
        money!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_MONEY_WON)

        editEtcBtn = findViewById(R.id.estimate_type_a_1_edit_etc_btn)

        backBtn = findViewById(R.id.estimate_type_a_1_bottom_back_btn)
        backBtn!!.setOnClickListener {
            val intent = Intent(mContext, GlobalEstimateActivity::class.java)
            startActivity(intent)
            finish()
        }

        confirmBtn = findViewById(R.id.estimate_type_a_1_confirm_btn)
        confirmBtn!!.setOnClickListener {
            SendEstimate()
        }
    }

    fun SendEstimate() {
        val bundle = Bundle()
        bundle.putString("title", "견적요청 안내")
        bundle.putString("body", "작성 하신 견적서를 바탕으로\n" +
                "여러 여행사에서 고객님께 다양한\n" +
                "견적을 드리도록 하겠습니다.\n" +
                "조금만 기다려 주세요.")
        Alert_Nomal!!.arguments = bundle
        Alert_Nomal!!.show(supportFragmentManager, "")
    }

    override fun onAlertCallBack() {
        val intent = Intent(mContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}