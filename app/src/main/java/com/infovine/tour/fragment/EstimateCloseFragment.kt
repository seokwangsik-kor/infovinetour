package com.infovine.tour.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.infovine.tour.R

class EstimateCloseFragment : DialogFragment() {

    var mContext: Context? = null
    var back_btn : ImageView? = null
    var yes : LinearLayout? = null
    var no : LinearLayout? = null

    private var callBackListener: EstimateCloseAlertCallBackListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (mContext is EstimateCloseAlertCallBackListener) callBackListener = mContext as EstimateCloseAlertCallBackListener?
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_estimate_close, container, false)

        back_btn =  mView.findViewById(R.id.alert_estimate_close_back_btn)
        back_btn!!.setOnClickListener {
            dismiss()
        }
        yes = mView.findViewById(R.id.alert_estimate_close_confirm)
        yes!!.setOnClickListener {
            dismiss()
            callBackListener!!.onEstimateClosCallBack()
        }
        no = mView.findViewById(R.id.alert_estimate_close_cancle)
        no!!.setOnClickListener {
            dismiss()
        }

        return mView
    }

    internal interface EstimateCloseAlertCallBackListener {
        fun onEstimateClosCallBack()
    }
}