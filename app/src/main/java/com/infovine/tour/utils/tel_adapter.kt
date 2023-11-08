package com.infovine.tour.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infovine.tour.R

class tel_adapter(var list: Array<String>) :
    RecyclerView.Adapter<tel_adapter.Holder>() {
    interface Item_Click {
        fun onItem_click(v: View?, position: Int)
    }

    private var item_click: Item_Click? = null

    fun setOnItemClickListener(listener: Item_Click?) {
        item_click = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.telecom_bottom_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tv.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv: TextView
        var check:ImageView

        init {

            tv = itemView.findViewById<TextView>(R.id.text)
            check = itemView.findViewById(R.id.check)
            itemView.setOnClickListener { v ->
                val pos = adapterPosition
                // 리스너 객체의 메서드 호출
                if (pos != RecyclerView.NO_POSITION) {
                    if (item_click != null) {
                        item_click!!.onItem_click(v, pos)
                        tv.setTextColor(Color.parseColor("#004fff"))
                    }
                }
            }
            itemView.setOnTouchListener { _: View, event:MotionEvent ->
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        tv.setTextColor(Color.parseColor("#004fff"))
                        check.visibility = View.VISIBLE
                    }
                    MotionEvent.ACTION_MOVE -> {
                        tv.setTextColor(Color.parseColor("#171717"))
                        check.visibility = View.GONE
                    }
                }
                false
            }
        }
    }
}