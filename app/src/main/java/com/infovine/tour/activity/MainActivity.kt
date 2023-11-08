package com.infovine.tour.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import com.infovine.tour.R
import com.infovine.tour.data.EstimateC
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    var btn : LinearLayout? = null
    var btn2 : LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.estimate_start)
        btn!!.setOnClickListener {
            SharedPrefsUtils.setBooleanPreference(mContext!!, Const.ESTIMATE_EDIT,false)
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_STARTDATE1, "")
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_ENDDATE1, "")
            val intent = Intent(mContext, GlobalEstimateActivity::class.java)
            startActivity(intent)
        }

        btn2 = findViewById(R.id.main_bottom_btn2)
        btn2!!.setOnClickListener {
            val intent = Intent(mContext, EstimateListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this)
    }
}