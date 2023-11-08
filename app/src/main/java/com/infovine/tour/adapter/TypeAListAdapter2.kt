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
import com.infovine.tour.activity.GlobalEstimateActivity
import com.infovine.tour.data.EstimateTypeA
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils


class TypeAListAdapter2(c: Context, list: ArrayList<EstimateTypeA>, onClick: OnClickInterface) : RecyclerView.Adapter<TypeAListAdapter2.ViewHolder>() {

    var mContext: Context? = null
    var aList =  ArrayList<EstimateTypeA>()
    var selectedItemPosition = -1
    var flag_single_sel = true
    var onClickInterface : OnClickInterface? = null

    init {
        mContext = c
        aList = list
        onClickInterface = onClick
    }

    companion object {
        var hotel_singleSelectNum = 0
        var updateCD = ArrayList<Int>()
        var single_flag = true
    }

    fun singleSelNum() {
        for (i in 0 until aList.size) {
            if (aList[i].sortNo == 99) {
                hotel_singleSelectNum = aList[i].mcCd
                SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_OPTION_HOTEL_SINGLE_CD, hotel_singleSelectNum)
//                onClickInterface!!.onClick(aList[i].mcNm, hotel_singleSelectNum)
            }
        }
    }

    fun listchangeSingleSelect(p:Int) {
        hotel_singleSelectNum = p
        flag_single_sel = true
        for (i in 0 until aList.size) {
            if (hotel_singleSelectNum == aList[i].mcCd) {
                selectedItemPosition = i
                onClickInterface!!.onClick(aList[i].mcNm, hotel_singleSelectNum)
            }
        }
    }

    var updateCD = ArrayList<Int>()
    var update_flag = false
    fun setUpdate(p : Int) {
        update_flag = true
        flag_single_sel = false
        for (i in 0 until aList.size) {
            if (p == aList[i].mcCd) {
                updateCD.add(aList[i].mcCd)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mView = LayoutInflater.from(mContext).inflate(R.layout.list_type_b_layout, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt.text = aList[position].mcNm

        if (flag_single_sel) {
            if (selectedItemPosition == position) {
                holder.check.isChecked=  true
                holder.txt.setTextColor(Color.parseColor("#57b918"))
            } else {
                holder.check.isChecked = false
                holder.txt.setTextColor(Color.parseColor("#4f5664"))
            }
        } else {
            if(aList[position].mcCd == hotel_singleSelectNum) {
                holder.txt.setTextColor(Color.parseColor("#4f5664"))
            } else {
                if(update_flag) {
                    var cd = aList[position].mcCd
                    for (i in 0 until updateCD.size) {
                        if (cd == updateCD[i]) {
                            holder.check.isChecked=  true
                            holder.txt.setTextColor(Color.parseColor("#57b918"))
                            onClickInterface!!.onClick(aList[position].mcNm, aList[position].mcCd)
                        }
                    }
                }
            }
        }

        holder.layer.setOnClickListener {
            if(aList[position].mcCd == hotel_singleSelectNum) {
                holder.check.isChecked = !holder.check.isChecked
                if (holder.check.isChecked) {
                    flag_single_sel = true
                    selectedItemPosition = holder.adapterPosition
                    notifyDataSetChanged()
                    single_flag = true
                } else {
                    holder.txt.setTextColor(Color.parseColor("#4f5664"))
                    single_flag = false
                }
            } else {
                holder.check.isChecked = !holder.check.isChecked
                if (holder.check.isChecked) {
                    holder.txt.setTextColor(Color.parseColor("#57b918"))
                } else {
                    holder.txt.setTextColor(Color.parseColor("#4f5664"))
                }
                flag_single_sel = false
                if(selectedItemPosition==aList.size-1) {
                    notifyItemChanged(selectedItemPosition)
                    selectedItemPosition = -1
                }
            }
            onClickInterface!!.onClick(aList[position].mcNm, aList[position].mcCd)
//            GlobalEstimateActivity.hotel_setText(aList[position].mcNm, aList[position].mcCd)
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

    interface OnClickInterface{
        fun onClick(name:String, cd: Int)
    }
}