package com.infovine.tour.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infovine.tour.data.AreaList
import com.infovine.tour.fragment.DestListFragment
import com.infovine.tour.utils.BaseUtil

class SearchAdapter(fragmentActivity: FragmentActivity, areaList: ArrayList<AreaList>) : FragmentStateAdapter(fragmentActivity) {

    var fragmentList = ArrayList<DestListFragment>()

    override fun createFragment(position: Int): Fragment {
        DestListFragment.dest_cd = area[position].areaCd
        return fragmentList[position]
    }

    var area = ArrayList<AreaList>()

    init {
        fragmentList.clear()
        area = areaList

        for (i in 0 until area.size) {
            fragmentList.add(i, DestListFragment())
        }
    }

    override fun getItemCount(): Int {
        return area.size
    }

    override fun containsItem(itemId: Long): Boolean {
        return true
    }
}