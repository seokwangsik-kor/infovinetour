package com.infovine.tour.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infovine.tour.R


class SearchSaveListAdapter(c: Context, list: ArrayList<String>, onClick: OnClickInterface) : RecyclerView.Adapter<SearchSaveListAdapter.ViewHolder>() {

    var mContext: Context? = null
    var searchList =  ArrayList<String>()
    var onClickInterface : OnClickInterface? = null

    init {
        mContext = c
        searchList = list
        onClickInterface = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mView = LayoutInflater.from(mContext).inflate(R.layout.list_search_save_layout, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt.text = searchList[position]
        holder.txt.setOnClickListener {
            onClickInterface!!.onClick(searchList[position], false)
        }

        holder.img.setOnClickListener {
            onClickInterface!!.onClick(searchList[position], true)
        }
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var img = view.findViewById(R.id.list_search_save_del) as ImageView
        var txt = view.findViewById(R.id.list_search_save_txt) as TextView
    }

    interface OnClickInterface{
        fun onClick(name:String, boolean: Boolean)
    }
}