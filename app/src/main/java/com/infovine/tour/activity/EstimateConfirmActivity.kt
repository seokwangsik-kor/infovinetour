package com.infovine.tour.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.compose.ui.res.pluralStringResource
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.infovine.tour.R
import com.infovine.tour.data.Estimate
import com.infovine.tour.data.EstimateResponse
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EstimateConfirmActivity : BaseActivity() {

    var backBtn : ImageView? = null
    var editBtn : LinearLayout? = null
    var city_img : ImageView? = null
    var layer : LinearLayout? = null
    var imm: InputMethodManager? = null

    //1
    var area : TextView? = null
    var startArea : TextView? = null
    var date : TextView? = null
    var adult : TextView? = null
    var child : TextView? = null
    var baby : TextView? = null
    var money : TextView? = null
    var option : TextView? = null

    //2
    var pnum : TextView? = null
    var pnum_edit : TextView? = null

    //3
    var etc : EditText? = null
    var etc_count : TextView? = null

    //신청
    var confirmBtn:FrameLayout? = null
    var confirmBtnCheck: CheckBox? = null
    var confirmBtnTxt:TextView? = null

    val PHONE_CONFIRM = 222
    var phone_number = ""
    var count = 0
    var confirm_flag = false
    var phone_confirm_status = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimate_confirm)

        backBtn = findViewById(R.id.estimate_confirm_back_btn)
        backBtn!!.setOnClickListener {
            mFinish()
        }

        layer = findViewById(R.id.estimate_confirm_layer)
        layer!!.setOnClickListener {
            imm!!.hideSoftInputFromWindow(etc!!.windowToken, 0)
        }

        city_img = findViewById(R.id.estimate_confirm_cityimg)
//        Glide.with(mContext!!).load(Const.IMAGE_DOMAIN+SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_DEST_IMG)).into(city_img!!)
        Glide.with(mContext!!).load(SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_DEST_IMG)).into(city_img!!)

        editBtn = findViewById(R.id.estimate_confirm_edit_btn)
        editBtn!!.setOnClickListener {
            SharedPrefsUtils.setBooleanPreference(mContext!!, Const.ESTIMATE_EDIT,true)
            val intent = Intent(mContext, GlobalEstimateActivity::class.java)
            startActivity(intent)
            finish()
        }
        area = findViewById(R.id.estimate_confirm_city)
        area!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_DESTINATION_NAME)

        startArea = findViewById(R.id.estimate_confirm_start_city)
        startArea!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_START_AREA)

        date = findViewById(R.id.estimate_confirm_date)
        date!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_FULL_DATE)

        adult = findViewById(R.id.estimate_confirm_adult)
        child = findViewById(R.id.estimate_confirm_child)
        baby = findViewById(R.id.estimate_confirm_baby)
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
            baby!!.text = "유아: $baby_cnt"+ "명"
        } else {
            baby!!.isVisible = false
        }

        money = findViewById(R.id.estimate_confirm_money)
        money!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_MONEY_WON) + "원"

        option = findViewById(R.id.estimate_confirm_option)
        option!!.text = SharedPrefsUtils.getStringPreference(mContext, Const.ESTIMATE_OPTION_NAME_LIST)

        pnum = findViewById(R.id.estimate_confirm_pnum)
        pnum_edit = findViewById(R.id.estimate_confirm_pnum_change)
        pnum_edit!!.setOnClickListener {
            val intent = Intent(mContext, PhoneNumActivity::class.java)
            intent.putExtra("phone_confirm_status", phone_confirm_status)//true 면 번호변경
            startActivityForResult(intent, PHONE_CONFIRM)
        }

        etc = findViewById(R.id.estimate_confirm_edit_etc)
        etc_count = findViewById(R.id.estimate_confirm_edit_count)
        etc!!.addTextChangedListener(watcher)

        confirmBtn = findViewById(R.id.estimate_confirm_confirm_btn)
        confirmBtn!!.setOnClickListener {
            if (confirmBtnCheck!!.isChecked) {
                if(confirm_flag) return@setOnClickListener
                confirm_flag = true
                SendEstimate()
            }
        }
        confirmBtnCheck = findViewById(R.id.estimate_confirm_confirm_check)
        confirmBtnTxt = findViewById(R.id.estimate_confirm_confirm_txt)

        SharedPrefsUtils.setBooleanPreference(mContext!!, Const.ESTIMATE_EDIT,false)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            count = charSequence!!.length
            etc_count!!.text = count.toString() + "/100"
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    override fun onResume() {
        super.onResume()
        phone_number = SharedPrefsUtils.getStringPreference(mContext, Const.PREF_MEMBER_PHONE_NUMBER)
        if (!TextUtils.isEmpty(phone_number)) {
            pnum!!.setText(phone_number)
            pnum_edit!!.text = "번호변경"
            confirmBtnCheck!!.isChecked = true
            confirmBtnTxt!!.setTextColor((Color.parseColor("#ffffff")))
            phone_confirm_status = true
        } else {
            pnum_edit!!.text = "본인인증"
            confirmBtnCheck!!.isChecked = false
            confirmBtnTxt!!.setTextColor((Color.parseColor("#bebebe")))
        }
    }

    fun SendEstimate() {

        val estimate = Estimate(
            SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_THEMECD),
            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_DEST, 0),
            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_MONEY, 0),
            etc!!.text.toString(),
            0,//business
            0,//mileage
            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_DATETYPE, 0),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_STARTDATE),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_ENDDATE),
            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_ADULT_CNT, 0),
            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_CHILD_CNT, 0),
            SharedPrefsUtils.getIntegerPreference(mContext!!, Const.ESTIMATE_BABY_CNT, 0),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_OPTION),
            "",
            SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_START_AREA)
        )

        val call: Call<EstimateResponse?>? = Service_retrofitApi!!.Estimate(estimate)
        call!!.enqueue(object : Callback<EstimateResponse?> {
            override fun onResponse(call: Call<EstimateResponse?>, response: Response<EstimateResponse?>) {

                if(response.isSuccessful) {
                    confirm_flag = false
                    var code = response.body()!!.resultCode
                    if (code == "S00001") {
                        val intent = Intent(mContext, EstimateFinishActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        ResponseErrorDialog(code)
                    }
                } else {
                    SeverErrorDialog()
                }
            }
            override fun onFailure(call: Call<EstimateResponse?>, t: Throwable) {
                confirm_flag = false
                SeverErrorDialog()
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHONE_CONFIRM) {
            if(data==null)  return
            var data = data!!.getStringExtra("phone_number")!!
            pnum!!.text = data
            SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_MEMBER_PHONE_NUMBER, data)
            Toast.makeText(mContext!!, "본인 인증이 완료 되었습니다.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        mFinish()
    }

    fun mFinish() {
        SharedPrefsUtils.setBooleanPreference(mContext!!, Const.ESTIMATE_EDIT,true)
        val intent = Intent(mContext, GlobalEstimateActivity::class.java)
        startActivity(intent)
        finish()
    }
}