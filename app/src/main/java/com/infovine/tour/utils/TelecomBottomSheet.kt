package com.infovine.tour.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.infovine.tour.R

class TelecomBottomSheet(context: Context) : BottomSheetDialog(context) {
    var tel_list: RecyclerView?
    var tel_adapter: tel_adapter

//    var itemClick: tel_adapter.Item_Click? = null

    init {
        //        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.custom_telecom_bottom)
        setCancelable(false)
        val telecom = context.resources.getStringArray(R.array.tel_list)
        tel_list = findViewById<RecyclerView>(R.id.tel_list)
        tel_adapter = tel_adapter(telecom)
        tel_list!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        tel_list!!.adapter = tel_adapter
//        tel_list!!.addItemDecoration(Recycler_divider(context, R.drawable.recycler_divider_tran, 1))
    }

    fun setItemClick(itemClick: tel_adapter.Item_Click?) {
//        this.itemClick = itemClick
        tel_adapter.setOnItemClickListener(itemClick)
    }
}