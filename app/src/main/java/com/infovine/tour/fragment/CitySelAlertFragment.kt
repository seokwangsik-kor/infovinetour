package com.infovine.tour.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.infovine.tour.R
import com.infovine.tour.activity.GlobalEstimateActivity
import com.infovine.tour.activity.GlobalEstimateActivity.Companion.city_list_flag
import com.infovine.tour.activity.GlobalEstimateActivity.Companion.del_alt_flag
import com.infovine.tour.activity.GlobalEstimateActivity.Companion.self_city_flag

class CitySelAlertFragment : DialogFragment() {

    var mContext:Context? = null
    var title:TextView? = null
    var body:TextView? = null
    var back_btn : ImageView? = null
    var yes : LinearLayout? = null
    var no : LinearLayout? = null

    var layer: LinearLayout? = null
    var before_txt:TextView? = null
    var after_txt:TextView? = null
    var before_txt1:TextView? = null
    var after_txt1:TextView? = null

    var mTile = ""
    var mBody = ""
//    var city = ""
    var before_city = ""
    var after_city = ""
    var flag = true
    private var callBackListener: CityAlertCallBackListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (mContext is CityAlertCallBackListener) callBackListener = mContext as CityAlertCallBackListener?
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                return
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setStyle(STYLE_NO_TITLE, R.style.DialogFragmentTheme)
        var mData = arguments
        if (mData != null) {
            mTile = mData.getString("title").toString()
            mBody = mData.getString("body").toString()
            before_city = mData.getString("before_city").toString()
            after_city = mData.getString("after_city").toString()
//            city = mData.getString("city").toString()
            flag = mData.getBoolean("flag")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_city_sel_alert, container, false)

        layer = mView.findViewById(R.id.alert_city_sel_body_layer)
        before_txt = mView.findViewById(R.id.alert_city_sel_before_body)
        after_txt = mView.findViewById(R.id.alert_city_sel_after_body)
        before_txt1 = mView.findViewById(R.id.alert_city_sel_before_body1)
        after_txt1 = mView.findViewById(R.id.alert_city_sel_after_body1)
        back_btn = mView.findViewById(R.id.alert_city_sel_back_btn)
        back_btn!!.setOnClickListener {
            dismiss()
        }
        title = mView.findViewById(R.id.alert_city_sel_title)
        body = mView.findViewById(R.id.alert_city_sel_body)
        yes = mView.findViewById(R.id.alert_city_sel_confirm)
        yes!!.setOnClickListener {
            dismiss()
            callBackListener!!.onAlertCallBack(after_city)
        }
        no = mView.findViewById(R.id.alert_city_sel_cancle)
        no!!.setOnClickListener {
            dismiss()
            if(del_alt_flag) {
                callBackListener!!.onAlertCallBack("notDel")//출발지 삭제 버튼 클릭 후 팝업에 대한 처리
            } else {
                callBackListener!!.onAlertCallBack("")
            }
        }

        title!!.text = mTile
        if(del_alt_flag){
            layer!!.isVisible = false
            body!!.isVisible = true
            body!!.text = mBody
        } else {
            layer!!.isVisible = true
            before_txt!!.text = before_city
            after_txt!!.text = after_city


//            if(self_city_flag) {
//                before_txt1!!.text = "공항에서"
//                after_txt1!!.text = "]으로"
//            }
//            if(city_list_flag) {
//                before_txt1!!.text = "에서"
//                after_txt1!!.text = "]공항으로"
//            }


            if(!flag) {
                before_txt1!!.text = "에서"
                after_txt1!!.text = "]공항으로"
            } else {
                before_txt1!!.text = "공항에서"
                after_txt1!!.text = "]으로"
            }
        }
//        if(TextUtils.isEmpty(after_city)) {
//            body!!.isVisible = true
//            body!!.text = mBody
//        } else {
//            layer!!.isVisible = true
//            before_txt!!.text = before_city
//            after_txt!!.text = after_city
//            if(self_city_flag) {
//                before_txt1!!.text = "에서"
//                after_txt1!!.text = "공항으로"
//            } else {
//                before_txt1!!.text = "공항에서"
//                after_txt1!!.text = "으로"
//            }
//        }

        return mView
    }

    internal interface CityAlertCallBackListener {
        fun onAlertCallBack(cityname:String)
    }
}