package com.infovine.tour.data


import com.google.gson.annotations.SerializedName

data class EventPopupData(
    val code: String,
    val `data`: Data,
    val message: Any
){
    data class Data(
        val endRow: Int,
        val list: List<Eventlist>,
        val hasNextPage: Boolean,
        val hasPreviousPage: Boolean,
        val isFirstPage: Boolean,
        val isLastPage: Boolean,
        val navigateFirstPage: Int,
        val navigateLastPage: Int,
        val navigatePages: Int,
        val navigatepageNums: List<Int>,
        val nextPage: Int,
        val pageNum: Int,
        val pageSize: Int,
        val pages: Int,
        val prePage: Int,
        val size: Int,
        val startRow: Int,
        val total: Int
    )
}