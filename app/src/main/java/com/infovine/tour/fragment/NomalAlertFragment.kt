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
import androidx.fragment.app.DialogFragment
import com.infovine.tour.R

class NomalAlertFragment : DialogFragment() {

    var mContext:Context? = null
    var title:TextView? = null
    var body:TextView? = null
    var btn:LinearLayout? = null
    var mTile = ""
    var mBody = ""
    private var callBackListener: NomalAlertCallBackListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (mContext is NomalAlertCallBackListener) callBackListener = mContext as NomalAlertCallBackListener?
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
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_nomal_alert, container, false)
        title = mView.findViewById(R.id.alert_nomal_title)
        body = mView.findViewById(R.id.alert_nomal_body)
        btn = mView.findViewById(R.id.alert_nomal_confirm)
        btn!!.setOnClickListener {
            dismiss()
            callBackListener!!.onAlertCallBack()
        }
        title!!.text = mTile
        body!!.text = mBody
        return mView
    }

    internal interface NomalAlertCallBackListener {
        fun onAlertCallBack()
    }
}