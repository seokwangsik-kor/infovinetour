package com.infovine.tour.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.infovine.tour.R
import com.infovine.tour.activity.BaseActivity
import com.infovine.tour.adapter.EventPopupAdapter
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils

class EventPopupFragment : DialogFragment() {

    var mContext: Context? = null
    var close: TextView? = null
    var checkBtnText: TextView? = null
    var checkBtn: ImageView? = null
    var mPager: ViewPager2? = null
    var mAdapter : EventPopupAdapter? = null
    var mIndicator : TabLayout? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setStyle(STYLE_NO_TITLE, R.style.DialogFragmentTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_event_popup, container, false)

        mPager = mView.findViewById(R.id.event_popup_viewpager)
        mIndicator = mView.findViewById(R.id.event_popup_indicator)
        checkBtn = mView.findViewById(R.id.event_popup_check)
        checkBtn!!.setOnClickListener {
            SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_EVENTPOPUP_ONEDAY,  BaseUtil.getTime())
            finish()
        }
        checkBtnText = mView.findViewById(R.id.event_popup_check_text)
        checkBtnText!!.setOnClickListener {
            SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_EVENTPOPUP_ONEDAY,  BaseUtil.getTime())
            finish()
        }


        close = mView.findViewById(R.id.event_popup_close)
        close!!.setOnClickListener {
            finish()
        }

        mAdapter = EventPopupAdapter(mContext!!, BaseActivity.eventlist)
        mPager!!.adapter = mAdapter

        if (BaseActivity.eventlist.size == 1) {
            mIndicator!!.isVisible = false
        }
        TabLayoutMediator(mIndicator!!, mPager!!){tab, position ->
        }.attach()

        return mView
    }

    fun finish() {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.remove(this)
            ?.commitAllowingStateLoss()
    }
}