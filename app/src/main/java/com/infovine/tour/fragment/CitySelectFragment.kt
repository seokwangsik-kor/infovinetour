package com.infovine.tour.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.infovine.tour.R
import com.infovine.tour.utils.BaseUtil.City
import com.infovine.tour.utils.BaseUtil.StartCity


class CitySelectFragment : DialogFragment() {

    var mContext:Context? = null
    var back_btn : ImageView? = null
    var confirm : FrameLayout? = null
    var confirm_check : CheckBox? = null
    var do_txt : AutoCompleteTextView? = null
    var si_txt : AutoCompleteTextView? = null
    var do_adapter : ArrayAdapter<String>? =null
    var si_adapter : ArrayAdapter<String>? =null

    private var callBackListener: CitySelectCallBackListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (mContext is CitySelectCallBackListener) callBackListener = mContext as CitySelectCallBackListener?
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
        val mView = inflater.inflate(R.layout.fragment_city_select, container, false)

        confirm_check = mView.findViewById(R.id.city_select_confirm_check)
        confirm = mView.findViewById(R.id.city_select_confirm)
        back_btn = mView.findViewById(R.id.city_select_close)
        back_btn!!.setOnClickListener {
            dismiss()
        }
        do_txt = mView.findViewById(R.id.city_select_do)
        si_txt = mView.findViewById(R.id.city_select_si)

        do_adapter = ArrayAdapter(mContext!!, R.layout.list_cityname, City())
        do_txt!!.setAdapter(do_adapter)

        do_txt!!.setOnItemClickListener { parent, view, position, id ->
            var region = City().get(position)
            var data = StartCity().get(region)
            siSet(data!!, region)
        }

        do_txt!!.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.citysel_list_box, null))
        do_txt!!.minHeight = 200
        do_txt!!.dropDownHeight = 600

        return mView
    }

    fun siSet(data:ArrayList<String>, region:String) {
        si_adapter = ArrayAdapter(mContext!!, R.layout.list_cityname, data)
        si_txt!!.setAdapter(si_adapter)
        si_txt!!.setOnItemClickListener { parent, view, position, id ->
            var cityname = data.get(position)
            confirm_check!!.isChecked = true
            confirm_check!!.setOnClickListener {
                callBackListener!!.onCitySelectCallBack(region, cityname)
                dismiss()
            }
        }
        si_txt!!.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.citysel_list_box, null))
        si_txt!!.minHeight = 200
        si_txt!!.dropDownHeight = 600
    }

    internal interface CitySelectCallBackListener {
        fun onCitySelectCallBack(region:String, cityname:String)
    }
}