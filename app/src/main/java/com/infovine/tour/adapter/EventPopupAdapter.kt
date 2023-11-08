package com.infovine.tour.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infovine.tour.R
import com.infovine.tour.data.Eventlist
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import com.infovine.tour.webview.PopupWebViewFragment

class EventPopupAdapter(context:Context, eList:ArrayList<Eventlist>) : RecyclerView.Adapter<EventPopupAdapter.ViewHolder>() {

    var eventlist = ArrayList<Eventlist>()
    var mContext :Context? = null
    var popupFragment: PopupWebViewFragment? = null
    init {
        eventlist.clear()
        eventlist = eList
        mContext = context
        popupFragment = PopupWebViewFragment()
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_event_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return eventlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(mContext!!).load(SharedPrefsUtils.getStringPreference(mContext!!, Const.IMAGE_DOMAIN_URL)+eventlist[position].ceventImgURL).into(holder.imgview)
        holder.imgview.setOnClickListener {
            var type = eventlist[position].neventGB
            if (type == 1) {
                val bundle = Bundle()
                bundle.putString("bundleUrl", eventlist[position].ceventContentURL)
                bundle.putBoolean("topLayer", true)
                popupFragment!!.arguments = bundle
                popupFragment!!.show((mContext!! as AppCompatActivity).supportFragmentManager, "")
            } else {
                var i = Intent(Intent.ACTION_VIEW, Uri.parse(eventlist[position].ceventContentURL))
                mContext!!.startActivity(i)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgview = view.findViewById(R.id.event_list_image) as ImageView
    }
}