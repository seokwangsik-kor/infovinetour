package com.infovine.tour.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infovine.tour.R
import com.infovine.tour.data.ThemeList
import com.infovine.tour.utils.Const


class ThemeListAdapter(c: Context, list: ArrayList<ThemeList>, onClick: OnClickInterface) : RecyclerView.Adapter<ThemeListAdapter.ViewHolder>() {


    var mContext: Context? = null
    var themeList =  ArrayList<ThemeList>()
    var selectedItemPosition = -1
    var onClickInterface : OnClickInterface? = null

    init {
        mContext = c
        themeList = list
        onClickInterface = onClick
    }

    fun listchange(p:Int) {
//        selectedItemPosition = p
        for (i in 0 until themeList.size) {
            if (themeList[i].themeCd == p) {
                selectedItemPosition = i
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mView = LayoutInflater.from(mContext).inflate(R.layout.list_theme_layout, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt.text = themeList[position].themeNm
        Glide.with(mContext!!).load(Const.IMAGE_DOMAIN+themeList[position].themeImgUrl).into(holder.img)

//        holder.layer.setOnClickListener {
//            holder.check.isChecked = !holder.check.isChecked
//            if (holder.check.isChecked) {
//                holder.txt.setTextColor(Color.parseColor("#57b918"))
////                themeCd.add(themeList[position].themeCd.toString())
//                heart1!!.isVisible = true
//            } else {
//                holder.txt.setTextColor(Color.parseColor("#171717"))
////                themeCd.remove(themeList[position].themeCd.toString())
//                if (themeCd.size == 0) {
//                    heart1!!.isVisible = false
//                }
//            }
//        }

//        BaseUtil.LogErr(GlobalEstimateActivity.themeCd.toString())
        if (selectedItemPosition == position) {
//            holder.check.isChecked = true
//            holder.txt.setTextColor(Color.parseColor("#57b918"))
//            theme_Cd = themeList[position].themeCd.toString()
//            theme_Cd = themeList[position].themeCd.toString()
            onClickInterface!!.onClick(themeList[position].themeCd.toString())

//            heart1!!.isVisible = true
            holder.layer!!.setBackgroundResource(R.drawable.gray_greenline_btn)
//            holder.txt.setTextColor(Color.parseColor("#1bc371"))
        } else {
//            holder.check.isChecked = false
//            holder.txt.setTextColor(Color.parseColor("#171717"))
            holder.layer!!.setBackgroundResource(R.drawable.white_round_background)
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
        return themeList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layer = view.findViewById(R.id.list_theme_layer) as LinearLayout
        var img =  view.findViewById(R.id.list_theme_img) as ImageView
//        var check = view.findViewById(R.id.list_theme_check) as CheckBox
        var txt = view.findViewById(R.id.list_theme_txt) as TextView
    }

    interface OnClickInterface{
        fun onClick(cd: String)
    }
}