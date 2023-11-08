package com.infovine.tour.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.firebase.BuildConfig
import com.infovine.tour.R
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils

class UpdateActivity : BaseActivity() {

    var backBtn: ImageView? = null
    var ment_box: LinearLayout? = null
    var ment_title: TextView? = null
    var ment_txt: TextView? = null
    var ver: TextView? = null
    var new_ver: TextView? = null
    var update_cancle: LinearLayout? = null
    var update_now: LinearLayout? = null
    var check: CheckBox? = null
    var check1: TextView? = null
    var sevenDay: LinearLayout? = null

    var resultMsg = ""
    var resultVer = ""
    var resultCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        backBtn = findViewById(R.id.update_back_btn)
        ment_title = findViewById(R.id.update_ment_title)
        ment_box = findViewById(R.id.update_ment_box)
        ment_txt = findViewById(R.id.update_ment_txt)
        ver = findViewById(R.id.update_app_ver)
        new_ver = findViewById(R.id.update_app_new_ver)
        update_cancle = findViewById(R.id.update_cancle)
        update_now = findViewById(R.id.update_now)
        check = findViewById(R.id.update_check_btn)
        check1 = findViewById(R.id.update_check_btn1)
        sevenDay = findViewById(R.id.update_deilay_day_layer)

        resultCode = SharedPrefsUtils.getIntegerPreference(mContext!!, Const.PREF_UPDATE_CODE, 0)
        resultMsg = SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_UPDATE_MSG)
        resultVer = SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_UPDATE_VER)

        var versionName = BuildConfig.VERSION_NAME
        ver!!.text = "현재버전 : " + versionName
        new_ver!!.text = "최신버전 : " + resultVer
        ment_txt!!.text = resultMsg
        //resultCode 0:이미최신버전 1:강제업데이트 2:선택업데이트
        backBtn!!.setOnClickListener{
            CloseType()
        }
        if(TextUtils.isEmpty(resultMsg)) {
            ment_box!!.isVisible = false
        }

        if (resultCode == 1) {
            ment_title!!.text = "최신버전으로\n업데이트가 필요합니다."
            ment_txt!!.text = resultMsg
            update_cancle!!.isVisible = false
            sevenDay!!.isVisible = false
        } else if (resultCode == 2) {

        }

        check!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_UPDATE_SEVENDAY,  BaseUtil.getTimePlusSevenDay())
                intent.putExtra("ResultMsg", "close")
                setResult(RESULT_OK, intent)
                finish()
            }
        }
        check1!!.setOnClickListener {
            SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_UPDATE_SEVENDAY,  BaseUtil.getTimePlusSevenDay())
            intent.putExtra("ResultMsg", "close")
            setResult(RESULT_OK, intent)
            finish()
        }

        update_now!!.setOnClickListener {
            var updateUrl = SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_UPDATE_URL)
            if (!TextUtils.isEmpty(updateUrl)) {
                updateGo(updateUrl)
            } else {
                updateGo("market://details?id=com.infovine.moneyget")
            }
        }

        update_cancle!!.setOnClickListener {
            CloseType()
        }
    }

    fun CloseType() {
        if(resultCode == 1) {//강업시 닫기하면 앱 종료
            //메인액티비티 값 전달
            intent.putExtra("ResultMsg", "appfinish")
            setResult(RESULT_OK, intent)
            finish()
        } else if (resultCode == 2) {
            intent.putExtra("ResultMsg", "close")
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    fun updateGo(url:String) {
        val marketIntent = Intent(Intent.ACTION_VIEW)
        marketIntent.data = Uri.parse(url)
        mContext!!.startActivity(marketIntent)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
    }
}