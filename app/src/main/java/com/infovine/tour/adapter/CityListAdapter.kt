package com.infovine.tour.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infovine.tour.R


class CityListAdapter(c: Context, list: ArrayList<String>, onClick: OnClickInterface) : RecyclerView.Adapter<CityListAdapter.ViewHolder>() {


    var mContext: Context? = null
    var cityList =  ArrayList<String>()
    var selectedItemPosition = -1
    var onClickInterface : OnClickInterface? = null

    init {
        mContext = c
        cityList = list
        onClickInterface = onClick
    }

    fun listchange(p:Int) {
//        if(p>0) {
            selectedItemPosition = p
//        } else {
//            selectedItemPosition = -1
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mView = LayoutInflater.from(mContext).inflate(R.layout.list_city_layout, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt.text = cityList[position]

        if (selectedItemPosition == position) {
            holder.check.isChecked = true
            holder.txt.setTextColor(Color.parseColor("#1bc371"))
//            start_city = cityList[position]
//            heart5!!.isVisible = true
//            start_city_layer!!.isVisible = true
//            start_city_txt!!.text = cityList[position]
//            GlobalEstimateActivity.start_city_txt1!!.text = "공항에서 출발"
//            city_list_flag = true
            onClickInterface!!.onClick(cityList[position], position)
        } else {
            holder.check.isChecked = false
            holder.txt.setTextColor(Color.parseColor("#4f5664"))
        }

        holder.layer.setOnClickListener {
            if (selectedItemPosition >= 0) {
                notifyItemChanged(selectedItemPosition)

            }
            selectedItemPosition = holder.adapterPosition
            notifyItemChanged(selectedItemPosition)
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layer = view.findViewById(R.id.list_city_layer) as FrameLayout
        var check = view.findViewById(R.id.list_city_check) as CheckBox
        var txt = view.findViewById(R.id.list_city_txt) as TextView
    }

    interface OnClickInterface{
        fun onClick(Region: String, p:Int)
    }
}