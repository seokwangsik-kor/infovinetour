package com.infovine.tour.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infovine.tour.R
import com.infovine.tour.adapter.TypeAListAdapter1
import com.infovine.tour.adapter.TypeAListAdapter2
import com.infovine.tour.adapter.TypeAListAdapter3
import com.infovine.tour.data.*
import com.infovine.tour.retrofit.ServiceRetrofitClient
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class EstimateType_A_Activity : BaseActivity() {

    var topBackbtn: ImageView? = null
    var cancleBtn: LinearLayout? = null
    var ListView1: RecyclerView? = null
    var ListView2: RecyclerView? = null
    var ListView3: RecyclerView? = null
    var mAdapter1: TypeAListAdapter1? = null
    var mAdapter2: TypeAListAdapter2? = null
    var mAdapter3: TypeAListAdapter3? = null

//    var option = ArrayList<EstimateOption>()
//    var optionSub = ArrayList<EstimateOptionDetail>()
    var optionCd = ArrayList<EstimateOptionCategory>()

    companion object {
        var confirmBtn: FrameLayout? = null
        var confirmCheckBtn: CheckBox? = null
        var bottomBtnLayer: LinearLayout? = null
        var air_txt: TextView? = null
        var hotel_txt: TextView? = null
        var move_txt: TextView? = null

        var optionList1 = ArrayList<EstimateTypeA>()
        var optionList2 = ArrayList<EstimateTypeA>()
        var optionList3 = ArrayList<EstimateTypeA>()
        var arr_1 = ArrayList<String>()
        var arr_2 = ArrayList<String>()
        var arr_3 = ArrayList<String>()
        var optionCD = ArrayList<Int>()

        fun air_setText(name:String, cd: Int) {
            air_txt!!.text = ""
            var cnt = arr_1.size
            if (cnt==0) {
                arr_1.add(name)
            } else {
                var flag = true
                var index = 0
                for (i in 0 until arr_1.size) {
                    if(arr_1[i] == name) {
                        flag = false
                        index = i
                    }
                }
                if (flag) {
                    arr_1.add(name)
                } else {
                    arr_1.removeAt(index)
                }
//                optionCD.add(cd)
//                var i = 0
//                var index = 0
//                while(index <= arr_1.size){
//                    BaseUtil.LogErr(index.toString())
//                    if (arr_1[index] == name){
//                        i = index
//                        arr_1.removeAt(i)
//                        break
//                    } else {
//                        arr_1.add(name)
//                        break
//                    }
//                }


            }

            var str = ""
            var index = 0
            for (i in 0 until arr_1.size) {
                index = i
                index++
                if(arr_1.size==index) {
                    str += "($index) ${arr_1[i]}"
                } else {
                    str += "($index) ${arr_1[i]} / "
                }

            }
//            for (item in arr_1) {
//                str += "$item / "
//            }
            air_txt!!.text = str
            bottomBtn()
        }

        fun hotel_setText(name:String, cd: Int) {
            hotel_txt!!.text = ""
            var cnt = arr_2.size
            if (cnt==0) {
                arr_2.add(name)
            } else {
                var flag = true
                var index = 0
                for (i in 0 until arr_2.size) {
                    if(arr_2[i] == name) {
                        flag = false
                        index = i
                    }
                }
                if (flag) {
                    arr_2.add(name)
                } else {
                    arr_2.removeAt(index)
                }
//                optionCD.add(cd)
            }
            var str = ""
            var index = 0
            for (i in 0 until arr_2.size) {
                index = i
                index++
                if(arr_2.size==index) {
                    str += "($index) ${arr_2[i]}"
                } else  {
                    str += "($index) ${arr_2[i]} / "
                }
            }
            hotel_txt!!.text = str
            bottomBtn()
        }

        fun move_setText(name:String, cd: Int) {
            move_txt!!.text = ""
            var cnt = arr_3.size
            if (cnt==0) {
                arr_3.add(name)
            } else {
                var flag = true
                var index = 0
                for (i in 0 until arr_3.size) {
                    if(arr_3[i] == name) {
                        flag = false
                        index = i
                    }
                }

                if (flag) {
                    arr_3.add(name)
                } else {
                    arr_3.removeAt(index)
                }
//                optionCD.add(cd)
            }

            var str = ""
            var index = 0
            for (i in 0 until arr_3.size) {
                index = i
                index++
                if(arr_3.size==index) {
                    str += "($index) ${arr_3[i]}"
                } else {
                    str += "($index) ${arr_3[i]} / "
                }
            }
            move_txt!!.text = str
            bottomBtn()
        }

        fun bottomBtn() {
            if (TextUtils.isEmpty(air_txt!!.text)) {
                confirmCheckBtn!!.isChecked = false
                bottomBtnLayer!!.isVisible = false
            }
            if (TextUtils.isEmpty(hotel_txt!!.text)) {
                confirmCheckBtn!!.isChecked = false
                bottomBtnLayer!!.isVisible = false
            }
            if (TextUtils.isEmpty(move_txt!!.text)) {
                confirmCheckBtn!!.isChecked = false
                bottomBtnLayer!!.isVisible = false
            }

//            if (TextUtils.isEmpty(air_txt!!.text) && TextUtils.isEmpty(hotel_txt!!.text) && TextUtils.isEmpty(move_txt!!.text)) {
            if (!TextUtils.isEmpty(air_txt!!.text) && !TextUtils.isEmpty(hotel_txt!!.text) && !TextUtils.isEmpty(move_txt!!.text)) {
//                confirmCheckBtn!!.isChecked = false
//                bottomBtnLayer!!.isVisible = false
//            } else {
                bottomBtnLayer!!.isVisible = true
                confirmCheckBtn!!.isChecked = true
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimate_type_a_activity)

        confirmCheckBtn = findViewById(R.id.estimate_type_a_bottom_confirm_check)
        bottomBtnLayer = findViewById(R.id.estimate_type_a_bottom_layer)
        cancleBtn = findViewById(R.id.estimate_type_a_bottom_back_btn)
        cancleBtn!!.setOnClickListener {
            val intent = Intent(mContext, GlobalEstimateActivity::class.java)
            startActivity(intent)
            finish()
        }
        topBackbtn = findViewById(R.id.estimate_type_a_back_btn)
        topBackbtn!!.setOnClickListener {
            val intent = Intent(mContext, GlobalEstimateActivity::class.java)
            startActivity(intent)
            finish()
        }

        air_txt = findViewById(R.id.estimate_type_a_sel1_txt)
        hotel_txt = findViewById(R.id.estimate_type_a_sel2_txt)
        move_txt = findViewById(R.id.estimate_type_a_sel3_txt)

        ListView1 = findViewById(R.id.estimate_type_a_recyclerview1)
        ListView2 = findViewById(R.id.estimate_type_a_recyclerview2)
        ListView3 = findViewById(R.id.estimate_type_a_recyclerview3)

//        mAdapter1 = TypeAListAdapter1(mContext!!, optionList1)
//        mAdapter2 = TypeAListAdapter2(mContext!!, optionList2)
        mAdapter3 = TypeAListAdapter3(mContext!!, optionList3)

        ListView1!!.setHasFixedSize(true)
        val gridLayoutManager1 = GridLayoutManager(mContext, 3)
        ListView1!!.layoutManager = gridLayoutManager1
        ListView1!!.adapter = mAdapter1

        ListView2!!.setHasFixedSize(true)
        val gridLayoutManager2 = GridLayoutManager(mContext, 4)
        ListView2!!.layoutManager = gridLayoutManager2
        ListView2!!.adapter = mAdapter2

        ListView3!!.setHasFixedSize(true)
        val gridLayoutManager3 = GridLayoutManager(mContext, 4)
        ListView3!!.layoutManager = gridLayoutManager3
        ListView3!!.adapter = mAdapter3

        confirmBtn = findViewById(R.id.estimate_type_a_bottom_confirm_btn)
        confirmBtn!!.setOnClickListener {

            var index1 = 0
            for (i in 0 until arr_1.size) {
                index1 = i
                for (i in 0 until optionList1.size) {
                    if(arr_1[index1] == optionList1[i].mcNm) {
                        optionCD.add(optionList1[i].mcCd)
                    }
                }
            }

            var index2 = 0
            for (i in 0 until arr_2.size) {
                index2 = i
                for (i in 0 until optionList2.size) {
                    if(arr_2[index2] == optionList2[i].mcNm) {
                        optionCD.add(optionList2[i].mcCd)
                    }
                }
            }

            var index3 = 0
            for (i in 0 until arr_3.size) {
                index3 = i
                for (i in 0 until optionList3.size) {
                    if(arr_3[index3] == optionList3[i].mcNm) {
                        optionCD.add(optionList3[i].mcCd)
                    }
                }
            }
            optionCD.sort()

            var str = ""
            for (i in optionCD) {
                str += "$i,"
            }
            var aa = str.length
            str = str.substring(0, aa-1)
            SharedPrefsUtils.setStringPreference(mContext!!, Const.ESTIMATE_OPTION, str)
            val intent = Intent(mContext, EstimateType_A_1_Activity::class.java)
            startActivity(intent)
            finish()
        }

        optionList1.clear()
        optionList2.clear()
        optionList3.clear()
        arr_1.clear()
        arr_2.clear()
        arr_3.clear()
        optionCD.clear()

        OptionCategory()
    }

    fun OptionCategory() {
        optionCd.addAll(db.getOptionCategory()!!)
        optionCd.sortBy { it.sortNo }
        for (i in 0 until optionCd.size) {
            OptionData(optionCd[i].lcCd, i)
        }
//        val call: Call<EstimateOption?>? = Service_retrofitApi!!.EstimateOption()
//        call!!.enqueue(object : Callback<EstimateOption?> {
//            override fun onResponse(call: Call<EstimateOption?>, response: Response<EstimateOption?>) {
//                if(response.isSuccessful){
//                    var code = response.body()!!.resultCode
//
//                    if (code=="S00001") {
//                        var cnt = response.body()!!.list.size
//                        if (cnt==0) return
//                        for (i in 0 until cnt) {
//                            var lcCd = response.body()!!.list[i].lcCd
//                            var lcNm = response.body()!!.list[i].lcNm
//                            var lcImgUrl = response.body()!!.list[i].lcImgUrl
//                            var sortNo = response.body()!!.list[i].sortNo
//                            var memo = response.body()!!.list[i].memo
//                            optionCd.add(EstimateOptionCategory(lcCd, lcNm, lcImgUrl, sortNo, memo))
//                        }
//                        optionCd.sortBy { it.sortNo }
//                        for (i in 0 until optionCd.size) {
//                            OptionData(optionCd[i].lcCd, i)
//                        }
//                    }
//                }
//            }
//            override fun onFailure(call: Call<EstimateOption?>, t: Throwable) {
//                SeverErrorDialog()
//            }
//        })
    }

    fun OptionData(cd : Int, index:Int) {

        if(index==0) {
            optionList1.addAll(db.getOptionDetail(cd)!!)
        }
        if(index==1) {
            optionList2.addAll(db.getOptionDetail(cd)!!)
        }
        if(index==2) {
            optionList3.addAll(db.getOptionDetail(cd)!!)
        }
        if(index==0) mAdapter1!!.notifyDataSetChanged()
        if(index==1) mAdapter2!!.notifyDataSetChanged()
        if(index==2) mAdapter3!!.notifyDataSetChanged()

//        val call: Call<EstimateOptionDetail?>? = Service_retrofitApi!!.EstimateOptionDetail(cd)
//        call!!.enqueue(object : Callback<EstimateOptionDetail?> {
//            override fun onResponse(call: Call<EstimateOptionDetail?>, response: Response<EstimateOptionDetail?>) {
//                if(response.isSuccessful){
//                    var code = response.body()!!.resultCode
//
//                    if (code=="S00001") {
//                        var cnt = response.body()!!.list.size
//                        if (cnt==0) return
//                        for (i in 0 until cnt) {
//                            var mcCd = response.body()!!.list[i].mcCd
//                            var lcCd = response.body()!!.list[i].lcCd
//                            var mcNm = response.body()!!.list[i].mcNm
//                            var mcImgUrl = response.body()!!.list[i].mcImgUrl
//                            var sortNo = response.body()!!.list[i].sortNo
//                            var memo = response.body()!!.list[i].memo
//                            if(index==0) {
//                                optionList1.add(EstimateTypeA(mcCd, lcCd, mcNm, mcImgUrl, sortNo, memo))
//                            }
//                            if(index==1) optionList2.add(EstimateTypeA(mcCd, lcCd, mcNm, mcImgUrl, sortNo, memo))
//                            if(index==2) optionList3.add(EstimateTypeA(mcCd, lcCd, mcNm, mcImgUrl, sortNo, memo))
//
//                        }
//                        if(index==0) mAdapter1!!.notifyDataSetChanged()
//                        if(index==1) mAdapter2!!.notifyDataSetChanged()
//                        if(index==2) mAdapter3!!.notifyDataSetChanged()
//                    }
//                }
//            }
//            override fun onFailure(call: Call<EstimateOptionDetail?>, t: Throwable) {
//                SeverErrorDialog()
//            }
//        })
    }
}