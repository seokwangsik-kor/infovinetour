package com.infovine.tour.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.andrewjapar.rangedatepicker.CalendarPicker
import com.infovine.tour.R
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import java.util.*


class DatePickersActivity : BaseActivity() {

    var confirmBtn: FrameLayout? = null
    var dateTxt: TextView? = null
//    var calendar: DatePicker? = null
    var close: ImageView? = null
    var calendar_view: CalendarPicker? = null
    var sDate = ""
    var eDate = ""
    var mDate = ""

    var sdate = ""
    var edate = ""

    var fulldate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_pickers)

        close = findViewById(R.id.datePicker_close)
        close!!.setOnClickListener {
            setResult(RESULT_OK, null)
            finish()
        }
        confirmBtn = findViewById(R.id.datePicker_confirm_btn)
        confirmBtn!!.setOnClickListener {

            //api전송용 데이터
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_STARTDATE, sDate)
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_ENDDATE, eDate)

            //이전 선택날짜 표시용 데이터
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_STARTDATE1, sdate)
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_ENDDATE1, edate)

            //견적서 확인 표시용 데이터
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_FULL_DATE, fulldate)

            val intent = Intent()
            intent.putExtra("date", mDate)
            setResult(RESULT_OK, intent)
            finish()
        }
        dateTxt = findViewById(R.id.datePicker_confirm_txt)
        calendar_view = findViewById(R.id.calendar_view)

        val startDate = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        val endDate = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        endDate.add(Calendar.MONTH, 12)

        var sdate1 = SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_STARTDATE1)
        var edate1 = SharedPrefsUtils.getStringPreference(mContext!!, Const.ESTIMATE_ENDDATE1)
        if(!TextUtils.isEmpty(sdate1)) {
            val formatter1 = Date(sdate1)
            val formatter2 = Date(edate1)

            if(formatter1 != null){
                calendar_view.apply {
                    this!!.showDayOfWeekTitle(true)
                    setSelectionDate(formatter1, formatter2)
                }
            }
        } else {
            calendar_view.apply {
                this!!.showDayOfWeekTitle(true)
//            setRangeDate(startDate.time, endDate.time)
//            setSelectionDate(startDate.time, endDate.time)
            }
        }
//        val formatter1 = Date(sdate1)
//        val formatter2 = Date(edate1)
//
//        if(formatter1 != null){
//            calendar_view.apply {
//                this!!.showDayOfWeekTitle(true)
//                setSelectionDate(formatter1, formatter2)
//            }
//        } else {
//            calendar_view.apply {
//                this!!.showDayOfWeekTitle(true)
////            setRangeDate(startDate.time, endDate.time)
////            setSelectionDate(startDate.time, endDate.time)
//            }
//        }

        calendar_view!!.setOnStartSelectedListener { startDate, label ->
            confirmBtn!!.isVisible = false
        }

        calendar_view!!.setOnRangeSelectedListener { startDate, endDate, startLabel, endLabel ->
            confirmBtn!!.isVisible = true
            sdate = startDate.toString()
            edate = endDate.toString()

            var starDay = "" +getKorDay(startDate) + ""
            var endDay =  "" +getKorDay(endDate) + ""

            val start_date: List<String> = startLabel.split(" ")
            val end_date: List<String> = endLabel.split(" ")

            sDate = start_date[2]+"-"+start_date[1].replace("월", "")+"-"+start_date[0]
            eDate = end_date[2]+"-"+end_date[1].replace("월", "")+"-"+end_date[0]

            var sDate1 = start_date[2]+"."+start_date[1].replace("월", "")+"."+start_date[0]
            var eDate1 = end_date[2]+"."+end_date[1].replace("월", "")+"."+end_date[0]
            fulldate = sDate1 + " ~ " + eDate1

            confirmBtn!!.isVisible = true
//            mDate = start_date[2]+"."+start_date[1].replace("월", "")+"."+start_date[0]+
//                    " ~ "+end_date[2]+"."+end_date[1].replace("월", "")+"."+end_date[0]
            mDate = start_date[1].replace("월", "")+"."+start_date[0]+" "+starDay+
                    " ~ "+end_date[1].replace("월", "")+"."+end_date[0]+" "+endDay
            dateTxt!!.text = mDate + " •선택"

            calendar_view!!.setPadding(0, 0, 0, 200)
        }
    }

    fun getKorDay(day : Date) : String{
        var dd = ""
        val calendar = Calendar.getInstance()
        calendar.time = day
        var no = calendar.get(Calendar.DAY_OF_WEEK)
        when(no) {
            1 -> dd = "일"
            2 -> dd = "월"
            3 -> dd = "화"
            4 -> dd = "수"
            5 -> dd = "목"
            6 -> dd = "금"
            7 -> dd = "토"
        }
        return dd
    }
}