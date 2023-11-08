package com.infovine.tour.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infovine.tour.R
import com.infovine.tour.data.EstimateListContent
import kotlin.random.Random


class EstimateListAdapter(c: Context, list: ArrayList<EstimateListContent>, onClick: OnClickInterface) : RecyclerView.Adapter<EstimateListAdapter.ViewHolder>() {

    var mContext: Context? = null
    var estimateList =  ArrayList<EstimateListContent>()
    var onClickInterface : OnClickInterface? = null

    init {
        mContext = c
        estimateList = list
        onClickInterface = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mView = LayoutInflater.from(mContext).inflate(R.layout.list_estimate, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.layer.setOnClickListener {
            onClickInterface!!.onClick(estimateList[position].estRegCd)
        }

        Glide.with(mContext!!).load(estimateList[position].destImgUrl).into(holder.img)
        holder.dest.text = estimateList[position].destNm
        holder.dest_sub.text = estimateList[position].destDesc
        holder.thema.text = estimateList[position].themeNm

        if (estimateList[position].adultCnt == 1) {
            holder.status_img.setImageResource(R.drawable.estimate_status_a)
        } else {
            holder.status_img.setImageResource(R.drawable.estimate_status_b)
        }

        if (estimateList[position].adultCnt > 0) {
            holder.adult.isVisible = true
            holder.adult.text = "성인 "+estimateList[position].adultCnt.toString()+"명"
        }

        if (estimateList[position].childCnt > 0) {
            holder.child.isVisible = true
            holder.child.text = "아동 "+estimateList[position].childCnt.toString()+"명"
        }

        if (estimateList[position].babyCnt > 0) {
            holder.baby.isVisible = true
            holder.baby.text = "유아 "+estimateList[position].babyCnt.toString()+"명"
        }

        var mDate = estimateList[position].dateGb.replace("-", ".")
        holder.date.text = mDate
    }

    override fun getItemCount(): Int {
        return estimateList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layer = view.findViewById(R.id.list_estimate_layer) as LinearLayout
        var img = view.findViewById(R.id.list_estimate_dest_image) as ImageView
        var dest = view.findViewById(R.id.list_estimate_dest_txt) as TextView
        var dest_sub = view.findViewById(R.id.list_estimate_dest_sub_txt) as TextView
        var status_img = view.findViewById(R.id.list_estimate_status_image) as ImageView
        var thema = view.findViewById(R.id.list_estimate_thema) as TextView
        var adult = view.findViewById(R.id.list_estimate_adult) as TextView
        var child = view.findViewById(R.id.list_estimate_child) as TextView
        var baby = view.findViewById(R.id.list_estimate_baby) as TextView
        var date = view.findViewById(R.id.list_estimate_date) as TextView


    }

    interface OnClickInterface{
        fun onClick(cd:Int)
    }
}
