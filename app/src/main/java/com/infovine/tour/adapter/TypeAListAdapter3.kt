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
import com.infovine.tour.activity.EstimateType_A_Activity
import com.infovine.tour.data.EstimateTypeA


class TypeAListAdapter3(c: Context, list: ArrayList<EstimateTypeA>) : RecyclerView.Adapter<TypeAListAdapter3.ViewHolder>() {


    var mContext: Context? = null
    var aList =  ArrayList<EstimateTypeA>()

    init {
        mContext = c
        aList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mView = LayoutInflater.from(mContext).inflate(R.layout.list_type_a_layout, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt.text = aList[position].mcNm

        holder.layer.setOnClickListener {
            holder.check.isChecked = !holder.check.isChecked
            if (holder.check.isChecked) {
                holder.txt.setTextColor(Color.parseColor("#57b918"))
            } else {
                holder.txt.setTextColor(Color.parseColor("#4f5664"))
            }
            EstimateType_A_Activity.move_setText(aList[position].mcNm, aList[position].mcCd)
        }
    }

    override fun getItemCount(): Int {
        return aList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layer = view.findViewById(R.id.list_type_a_btn_layer) as FrameLayout
        var check = view.findViewById(R.id.list_type_a_check) as CheckBox
        var txt = view.findViewById(R.id.list_type_a_txt) as TextView
    }
}