package com.infovine.tour.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infovine.tour.R
import com.infovine.tour.data.DestList
import com.infovine.tour.fragment.AppleLoginFragment
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils


class SearchDestListAdapter(c: Context, list: ArrayList<DestList>, onClick: OnClickInterface) : RecyclerView.Adapter<SearchDestListAdapter.ViewHolder>() {

    var mContext: Context? = null
    var mList =  ArrayList<DestList>()
    var onClickInterface : OnClickInterface? = null

    init {
        onClickInterface = onClick
        mContext = c
        mList.clear()
        mList = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mView = LayoutInflater.from(mContext).inflate(R.layout.list_dest_layout, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt.text = mList[position].destNm
        holder.txt_sub.text = mList[position].destDesc
//        var imgurl = mList[position].destImgUrl.replace(" ", "")
        var imgurl = mList[position].destImgUrl
        if (!TextUtils.isEmpty(imgurl)) {
//            Glide.with(mContext!!).load(Const.IMAGE_DOMAIN+mList[position].destImgUrl).into(holder.img)
            Glide.with(mContext!!).load(mList[position].destImgUrl).into(holder.img)
        }
        holder.layer.setOnClickListener {
//            Toast.makeText(mContext, mList[position].destDesc, Toast.LENGTH_LONG).show()
            SharedPrefsUtils.setIntegerPreference(mContext!!, Const.ESTIMATE_DEST, mList[position].destCd)
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_DESTINATION_NAME, mList[position].destNm)
            onClickInterface!!.onClick(mList[position].destNm, mList[position].destDesc, mList[position].destCd, mList[position].destImgUrl)
        }

//        BaseUtil.LogErr(GlobalEstimateActivity.themeCd.toString())
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layer = view.findViewById(R.id.list_dest_layer) as LinearLayout
        var img =  view.findViewById(R.id.list_dest_image) as ImageView
        var txt = view.findViewById(R.id.list_dest_txt) as TextView
        var txt_sub = view.findViewById(R.id.list_dest_sub_txt) as TextView
    }

    interface OnClickInterface{
        fun onClick(name:String, name_sub:String, cd:Int, imgUrl:String)
    }

}