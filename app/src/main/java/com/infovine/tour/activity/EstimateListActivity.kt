package com.infovine.tour.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.infovine.tour.R
import com.infovine.tour.adapter.EstimateListAdapter
import com.infovine.tour.adapter.SearchDestListAdapter
import com.infovine.tour.data.*
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EstimateListActivity : BaseActivity() {

    var backBtn: ImageView? = null

    var estimatelistView: RecyclerView? = null
    var estimateAdapter: EstimateListAdapter? = null
    var bottom_btn1: LinearLayout? = null
    var go_estimage: ImageView? = null
    var estimateList = ArrayList<EstimateListContent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimate_list)

        backBtn = findViewById(R.id.my_estimate_list_back_btn)
        backBtn!!.setOnClickListener {
            finish()
        }

        go_estimage = findViewById(R.id.my_estimate_go)
        go_estimage!!.setOnClickListener {
            val intent = Intent(mContext!!, GlobalEstimateActivity::class.java)
            startActivity(intent)
            finish()
        }

        bottom_btn1 = findViewById(R.id.my_estimate_list_bottom_btn1)
        bottom_btn1!!.setOnClickListener {
            finish()
        }

        estimatelistView = findViewById(R.id.my_estimate_list)
        estimateAdapter = EstimateListAdapter(mContext!!, estimateList, itemClickInterface)
        estimatelistView!!.adapter = estimateAdapter

        myEstimateList()
    }

    val oBject = object : EstimateListAdapter.OnClickInterface {
        override fun onClick(cd: Int) {
            val intent = Intent(mContext!!, MyEstimateDetailActivity::class.java)
            intent.putExtra("cd", cd)
            startActivity(intent)
        }
    }
    val itemClickInterface: EstimateListAdapter.OnClickInterface = oBject


    fun myEstimateList() {

        var token = Token(SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN))

        val call: Call<EstimateList?>? = Service_retrofitApi!!.EstimateList(token)
        call!!.enqueue(object : Callback<EstimateList?> {
            override fun onResponse(call: Call<EstimateList?>, response: Response<EstimateList?>) {

                if(response.isSuccessful) {
                    var code = response.body()!!.resultCode
                    if (code == "S00001") {
                        var cnt = 0
                        try {
                            cnt = response.body()!!.list.size
                        } catch (E:java.lang.Exception) {
                            return
                        }
                        for (i in 0 until cnt) {
                            var estRegCd = response.body()!!.list[i].estRegCd
                            var regDt = response.body()!!.list[i].regDt
                            var destNm = response.body()!!.list[i].destNm
                            var destImgUrl = response.body()!!.list[i].destImgUrl
                            var destDesc = response.body()!!.list[i].destDesc
                            var dateGb = response.body()!!.list[i].dateGb
                            var adultCnt = response.body()!!.list[i].adultCnt
                            var childCnt = response.body()!!.list[i].childCnt
                            var babyCnt = response.body()!!.list[i].babyCnt
                            var themeNm = response.body()!!.list[i].themeNm
                            estimateList.add(i, EstimateListContent(estRegCd, regDt, destNm, destImgUrl, destDesc, dateGb, adultCnt, childCnt, babyCnt, themeNm))
                        }

                        estimateAdapter!!.notifyDataSetChanged()

                    } else if(code == "E01001") {//JWT TOKEN 에러
                        tokenRefresh()
                    } else if(code == "E01002") {//JWT 유효성 검사 실패
                        tokenRefresh()
                    } else {
                        ResponseErrorDialog(code)
                    }
                } else {
                    SeverErrorDialog()
                }
            }
            override fun onFailure(call: Call<EstimateList?>, t: Throwable) {
                SeverErrorDialog()
            }
        })
    }

    fun tokenRefresh() {
        if(!TextUtils.isEmpty(SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN))){
            var fcmToken = SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_PUSH_ID)
            val reToken = TokenRefresh(SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN),fcmToken)
            val call: Call<AuthResponseData?>? = Auth_retrofitApi!!.tokenRefresh(reToken)
            call!!.enqueue(object : Callback<AuthResponseData?> {
                override fun onResponse(call: Call<AuthResponseData?>, response: Response<AuthResponseData?>) {
                    if(response.isSuccessful) {
                        if(response.body()!!.resultCode == "S00001") {
                            SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_JWT_TOKEN, response.body()!!.token)
                            SharedPrefsUtils.setIntegerPreference(mContext!!, Const.PREF_MEMBER_CD, response.body()!!.memcd)
                            SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_MEMBER_PHONE_NUMBER, response.body()!!.hp)

                            myEstimateList()
                        }
                    }
                }
                override fun onFailure(call: Call<AuthResponseData?>, t: Throwable) {
                    SeverErrorDialog()
                }
            })
        }
    }
}