package com.infovine.tour.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.infovine.tour.R

class UserPhoneConfirmAlertFragment : DialogFragment() {

    var mContext: Context? = null
    var body: TextView? = null
    var btn_no: LinearLayout? = null
    var btn_yes: LinearLayout? = null
    var phonelayer: LinearLayout? = null
    var phoneNum: TextView? = null
    var btn_no_txt: TextView? = null
    var btn_yes_txt: TextView? = null

    var confirm = true//인증이력 있으면 true
    private var callBackListener: UserPhoneConfirmCallBackListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (mContext is UserPhoneConfirmCallBackListener) callBackListener = mContext as UserPhoneConfirmCallBackListener?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogFragmentTheme)
        arguments?.let {
        }
        var mData = arguments
        if (mData != null) {
            confirm = mData.getBoolean("confirm")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                return
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mView = inflater.inflate(R.layout.fragment_user_phone_confirm_alert, container, false)
        phonelayer = mView.findViewById(R.id.alert_user_phone_number_layer)
        phoneNum = mView.findViewById(R.id.alert_user_phone_number_txt)
        body = mView.findViewById(R.id.alert_user_phone_txt)
        btn_no = mView.findViewById(R.id.alert_user_phone_close)
        btn_no!!.setOnClickListener {
            dismiss()
            callBackListener!!.onUserPhoneConfirmAlertCallBack(confirm,false)
        }
        btn_yes = mView.findViewById(R.id.alert_user_phone_confirm)
        btn_yes!!.setOnClickListener {
            dismiss()
            callBackListener!!.onUserPhoneConfirmAlertCallBack(confirm,true)
        }
        btn_no_txt = mView.findViewById(R.id.alert_user_phone_close_txt)
        btn_yes_txt = mView.findViewById(R.id.alert_user_phone_confirm_txt)

        if(confirm) {
            phonelayer!!.isVisible = true
            body!!.isVisible = false
            btn_no_txt!!.text = "아니오"
            btn_yes_txt!!.text = "예"
        }

        return mView
    }

    internal interface UserPhoneConfirmCallBackListener {
        fun onUserPhoneConfirmAlertCallBack(type:Boolean, btn_select:Boolean)
    }
}