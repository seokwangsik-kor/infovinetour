package com.infovine.tour.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infovine.tour.R
import com.infovine.tour.activity.SearchActivity.Companion.DestListDumy
import com.infovine.tour.adapter.DestListAdapter
import com.infovine.tour.data.DestList
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.BaseUtil.getMonth
import java.text.SimpleDateFormat
import java.util.*

class DestListFragment : Fragment() {

    var listView: RecyclerView? = null
    var listAdapter: DestListAdapter? = null
    var mContext: Context? = null
    var mLayoutManager: LinearLayoutManager? = null
    val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
    var destList = ArrayList<DestList>()
    var ment : TextView? = null
    var ment_txt = ""

    private var callBackListener: CallBackListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        if (mContext is CallBackListener) callBackListener = mContext as CallBackListener?
    }

    companion object {
        var dest_cd = 0
        var areaGb = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_dest_layout, container,false)
        listView = mView.findViewById(R.id.list_dest_recyclerview)
        ment = mView.findViewById(R.id.list_dest_recommend_ment)

        listView!!.itemAnimator = null
        listView!!.setHasFixedSize(true)

        listAdapter = DestListAdapter(mContext!!, destList, itemClickInterface)
        listAdapter!!.setHasStableIds(true)
        mLayoutManager = LinearLayoutManager(mContext)
        mLayoutManager!!.recycleChildrenOnDetach = true
        (mLayoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
        listView!!.layoutManager = mLayoutManager
        listView!!.setRecycledViewPool(viewPool)
        listView!!.adapter = listAdapter

        var month = getMonth()+"월달"
        ment_txt = "** 지금 출발하기 좋은 $month 추천 여행지 입니다."

        ListChange(dest_cd)
        return mView
    }

    val oBject = object :DestListAdapter.OnClickInterface{
        override fun onClick(name: String, name_sub:String, cd: Int, imgUrl:String) {
            BaseUtil.LogErr(name+ cd)
            callBackListener!!.onDestFragmentCallBack(name, name_sub, cd, imgUrl)
        }
    }
    val itemClickInterface: DestListAdapter.OnClickInterface = oBject

    fun ListChange(cd: Int) {
        destList.clear()

        ment!!.text = ment_txt
        if (cd==0) {
            BaseUtil.LogErr("areaGb" + areaGb)
//            destList.addAll(DestListDumy)
            if (areaGb == 1) {
                ment!!.isVisible = true
            } else {
//                ment!!.isVisible = false
            }
            for (i in 0 until DestListDumy.size) {
                if(DestListDumy[i].recommend == 1)
                    destList.add(DestListDumy[i])
            }
        } else {
            ment!!.isVisible = false
            for (i in 0 until DestListDumy.size) {
                if(DestListDumy[i].area_CD == cd)
                    destList.add(DestListDumy[i])
            }
        }

        destList.sortBy { it.area_CD }

        if(listAdapter !=null) {
            listAdapter!!.notifyDataSetChanged()
        }
    }

    internal interface CallBackListener {
        fun onDestFragmentCallBack(name:String, name_sub:String, cd:Int, imgUrl:String)
    }
}