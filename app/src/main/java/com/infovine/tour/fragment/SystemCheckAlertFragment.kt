package com.infovine.tour.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.infovine.tour.R

class SystemCheckAlertFragment : DialogFragment() {

    var mContext: Context? = null
    var btn: LinearLayout? = null
    var toptxt: TextView? = null
    var bodytxt: TextView? = null
    var mBody = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            var mData = arguments
            if (mData != null) {
                mBody = mData.getString("body").toString()
            }
        }

        setStyle(STYLE_NO_TITLE, R.style.DialogFragmentTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                return
            }
        }
    }

    @SuppressLint("NewApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_system_check_alert, container, false)

        toptxt = mView.findViewById(R.id.alert_systemcheck_top_txt)
        val color = mContext!!.getColor(R.color.main_color)
        val txt = "서비스명를 이용해주셔서 감사합니다.\n" +
                "고객님의 서비스 품질 향상을 위해 서버점검으로 \n" +
                "서비스가 불가피하게 제한됩니다."
        val spannable = SpannableString(txt)
        spannable.setSpan(ForegroundColorSpan(color), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        toptxt!!.text = spannable

        bodytxt = mView.findViewById(R.id.alert_systemcheck_ment_txt)
        bodytxt!!.text = mBody

        btn = mView.findViewById(R.id.alert_systemcheck_confirm)
        btn!!.setOnClickListener {
            dismiss()
        }
        return mView
    }
}