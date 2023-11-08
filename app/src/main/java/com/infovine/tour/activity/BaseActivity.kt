package com.infovine.tour.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.infovine.tour.R
import com.infovine.tour.data.*
import com.infovine.tour.fragment.*
import com.infovine.tour.retrofit.AuthRetrofitClient
import com.infovine.tour.retrofit.RetrofitAPI
import com.infovine.tour.retrofit.ServiceRetrofitClient
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import com.infovine.tour.utils.TourDB
import com.infovine.tour.webview.PopupWebViewFragment
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

open class BaseActivity : AppCompatActivity() {

    var ServiceApi = ServiceRetrofitClient().getInstance()!!.getRetrofitInterface()
    var Auth_retrofitClient: AuthRetrofitClient? = null
    var Service_retrofitClient: ServiceRetrofitClient? = null
    var Auth_retrofitApi: RetrofitAPI? = null
    var Service_retrofitApi: RetrofitAPI? = null
    var mContext: Context? = null

    var network_check_receiver: NetWorkCheckReceiver? = null
    var intentFilter: IntentFilter? = null

    var Alert_Nomal: NomalAlertFragment? = null
    var Alert_Network_Err: NetworkErrAlertFragment? = null
    var Alert_SystemCheck: SystemCheckAlertFragment? = null
    var eventFragment: EventPopupFragment? = null
    var Alert_UserPhoneNumber: UserPhoneConfirmAlertFragment? = null
    var popupFragment: PopupWebViewFragment? = null

    lateinit var db: TourDB
    lateinit var database : SQLiteDatabase
//    lateinit var request_data: mData

    var call_ThemeVer = false
    var call_McVer = false
    var call_OptVer = false
    var call_LcVer = false
    var call_OptDetailVer = false
    var call_DestVer = false
    var call_AreaVer = false
//    var call_SearchVer = false

    companion object {
        var eventlist = ArrayList<Eventlist>() //팝업이벤트데이터
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        mContext = this
        Auth_retrofitClient = AuthRetrofitClient().getInstance()
        Service_retrofitClient = ServiceRetrofitClient().getInstance()
        Auth_retrofitApi = Auth_retrofitClient!!.getRetrofitInterface()
        Service_retrofitApi = Service_retrofitClient!!.getRetrofitInterface()

        Alert_Nomal = NomalAlertFragment()
        Alert_Network_Err = NetworkErrAlertFragment()
        Alert_SystemCheck = SystemCheckAlertFragment()
        eventFragment = EventPopupFragment()
        Alert_UserPhoneNumber = UserPhoneConfirmAlertFragment()
        popupFragment = PopupWebViewFragment()

        intentFilter = IntentFilter()
        intentFilter?.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        network_check_receiver = NetWorkCheckReceiver()

        KakaoSdk.init(this, getString(R.string.kakao_native_key))
        var keyHash = Utility.getKeyHash(this)
        BaseUtil.LogErr(keyHash)

        db = TourDB(mContext, "Tour.db", null, 1)
        database = db.writableDatabase

//        request_data = mData()
    }

    fun APIData() {
        CoroutineScope(Dispatchers.Main).launch {
            API_DATA()
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(network_check_receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(network_check_receiver)
    }

    fun SeverErrorDialog() {
        if(!BaseUtil.DoubleClickChecker(2000)){
            return
        }
        Toast.makeText(mContext, "서버와의 통신에서 오류가 발생하였습니다.\n잠시 후 다시 시도 해주세요.", Toast.LENGTH_LONG).show()
    }

    fun ResponseErrorDialog(msg:String) {
        if(!BaseUtil.DoubleClickChecker(2000)){
            return
        }
        when(msg) {
            "E00900" -> {
                Toast.makeText(mContext, "알 수 없는 오류", Toast.LENGTH_LONG).show()
            }
            "E00901" -> {
                Toast.makeText(mContext, "REQUEST 파라미터 에러", Toast.LENGTH_LONG).show()
            }
            "E01102" -> {
                Toast.makeText(mContext, "ACCESS TOKEN 체크 실패", Toast.LENGTH_LONG).show()
            }
            "E01001" -> {
                Toast.makeText(mContext, "JWT TOKEN 에러", Toast.LENGTH_LONG).show()
            }
            "E01002" -> {
                Toast.makeText(mContext, "JWT 유효성 검사 실패", Toast.LENGTH_LONG).show()
            }
            "E01101" -> {
                Toast.makeText(mContext, "사용자가 아님(미가입자)", Toast.LENGTH_LONG).show()
            }
            "E00802" -> {
                Toast.makeText(mContext, "DB 데이터가 없음", Toast.LENGTH_LONG).show()
            }
            "E00800" -> {
                Toast.makeText(mContext, "DB 접근 에러", Toast.LENGTH_LONG).show()
            }
            "E9997" -> {
                Toast.makeText(mContext, "내부시스템오류", Toast.LENGTH_LONG).show()
            }
            "E9991" -> {
                Toast.makeText(mContext, "탈퇴된 계정입니다. 재가입은 7일 후에 가능합니다.", Toast.LENGTH_LONG).show()
            }
            "E9995" -> {
                Toast.makeText(mContext, "이미가입자", Toast.LENGTH_LONG).show()
            }
            "E9996" -> {
                Toast.makeText(mContext, "토큰오류", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(mContext, "잠시후 다시 시도해주세요.", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun NetworkErrorDialog() {
        if(!BaseUtil.DoubleClickChecker(2000)){
            return
        }
        try {
            Alert_Network_Err!!.show(supportFragmentManager, "")
        } catch (e:java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun NetworkErrorDialogdismiss() {
        try {
            Alert_Network_Err?.dismiss()
        } catch (e:java.lang.Exception) {
            e.printStackTrace()
        }
    }

    var networkcheck: Boolean = true
    inner class NetWorkCheckReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val actionOfIntent = intent.action
            val isConnected: Boolean = checkForInternet()
            try {
                if (actionOfIntent == ConnectivityManager.CONNECTIVITY_ACTION) {
                    networkcheck = isConnected
                    if(!isConnected) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            if(!networkcheck){
                                BaseUtil.LogErr("네트워크 오류처리")
                                NetworkErrorDialog()
                            }else{
                                NetworkErrorDialogdismiss()
                            }
                        }, 1000)
                    }else{
                        NetworkErrorDialogdismiss()
                    }
                }
            } catch (e:java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkForInternet(): Boolean {
        val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
    }

    fun EventData() {
        val Token = SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN)
        val call: Call<EventPopupData?>? = ServiceRetrofitClient().getInstance()!!.getRetrofitInterface().EventList("Bearer $Token", 2, 1, 999)
        call!!.enqueue(object : Callback<EventPopupData?> {
            override fun onResponse(call: Call<EventPopupData?>, response: Response<EventPopupData?>) {
                if (response.isSuccessful) {
                    eventlist.clear()
                    var code = response.body()!!.code
                    if (code != "SUCCESS") {
                        return
                    }

                    var cnt = 0
                    try {
                        cnt = response.body()!!.data.list.size
                    } catch (E:java.lang.Exception) {
                        return
                    }
                    for (i in 0 until cnt) {

                        var nuse_DT = response.body()!!.data.list[i].nuseDT
                        var newFlag = response.body()!!.data.list[i].newFlag
                        var reg_DT = response.body()!!.data.list[i].regDT
                        var ureg_DT = response.body()!!.data.list[i].uregDT
                        var nevent_CD = response.body()!!.data.list[i].neventCD
                        var nevent_Loc_GB = response.body()!!.data.list[i].neventLocGB
                        var nevent_GB = response.body()!!.data.list[i].neventGB
                        var nclick_Cnt = response.body()!!.data.list[i].nclickCnt
                        var nuse_Is = response.body()!!.data.list[i].nuseIs
                        var cevent_NM = response.body()!!.data.list[i].ceventNM
                        var cevent_Desc = response.body()!!.data.list[i].ceventDesc
                        var cevent_Img_URL = response.body()!!.data.list[i].ceventImgURL
                        var cevent_Content_URL = response.body()!!.data.list[i].ceventContentURL
                        eventlist.add(i, Eventlist(cevent_Content_URL, cevent_Desc, cevent_Img_URL, cevent_NM, nclick_Cnt,
                            nevent_CD, nevent_GB, nevent_Loc_GB, newFlag, nuse_DT, nuse_Is, reg_DT, ureg_DT)
                        )

                    }

                    if(eventlist.size!=0){
                        EventPopup()
                    }
                } else {
                    SeverErrorDialog()
                }
            }
            override fun onFailure(call: Call<EventPopupData?>, t: Throwable) {
                SeverErrorDialog()
            }
        })
    }

    fun EventPopup() {
        if (eventFragment!!.isVisible) return
        var saveTime = SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_EVENTPOPUP_ONEDAY)
        if (TextUtils.isEmpty(saveTime)) {
            supportFragmentManager.beginTransaction().add(eventFragment!!,"").commitAllowingStateLoss()
        } else {
            try {
                val nowFormat = SimpleDateFormat("yyyy-MM-dd").parse(BaseUtil.getTime())
                val beforeFormat = SimpleDateFormat("yyyy-MM-dd").parse(saveTime)
                if (nowFormat.after(beforeFormat)) {
                    supportFragmentManager.beginTransaction().add(eventFragment!!,"").commitAllowingStateLoss()
                }
            } catch (e :java.lang.Exception) {
                BaseUtil.LogErr(e.message)
            }
        }
    }

    suspend fun API_DATA() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                //여행테마
                if (call_ThemeVer) {
                    val ThemeData = Service_retrofitApi!!.TehmeList()
                    if (ThemeData.isSuccessful) {
                        db.DeleteTable(db.TB_THEME_LIST)
                        for (i in ThemeData.body()!!.list) {
                            var themeCd = i.themeCd
                            var themeNm = i.themeNm
                            var themeImgUrl = i.themeImgUrl
                            var sortNo = i.sortNo
                            db.insertTheme(ThemeList(themeCd, themeNm, themeImgUrl, sortNo))
                        }
                    }
                }

                //여행지역
                if(call_AreaVer) {
                    val areaData = Service_retrofitApi!!.Area()
                    if (areaData.isSuccessful) {
                        db.DeleteTable(db.TB_AREA)
                        for (i in areaData.body()!!.list) {
                            var areaCd = i.areaCd
                            var areaNm = i.areaNm
                            var areaImgUrl = i.areaImgUrl
                            var areaGb = i.areaGb
                            var memo = i.memo
                            db.insertArea(AreaList(areaCd, areaNm, areaImgUrl, areaGb, memo))
                        }
                    }
                }

                //여행목적지
                if(call_DestVer) {
                    val destData = Service_retrofitApi!!.Dest(0)//0 전체조회
                    if (destData.isSuccessful) {
                        db.DeleteTable(db.TB_DEST)
                        for (i in destData.body()!!.list) {
                            var destCd = i.destCd
                            var destNm = i.destNm
                            var destImgUrl = i.destImgUrl
                            var destDesc = i.destDesc
                            var area_CD = i.area_CD
                            var destRequestCnt = i.destRequestCnt
                            var memo = i.memo
                            var areaGb = i.areaGb
                            var recommend = i.recommend
                            db.insertDest(DestList(destCd, destNm, destImgUrl, destDesc, area_CD, destRequestCnt, memo, areaGb, recommend))
                        }
                    }
                }

                //옵션카테고리
                if (call_LcVer) {
                    val OptionData =  Service_retrofitApi!!.EstimateOption()
                    if (OptionData.isSuccessful) {
                        db.DeleteTable(db.TB_OPTION_CATEGORY)
                        for (i in OptionData.body()!!.list) {
                            var lcCd = i.lcCd
                            var lcNm = i.lcNm
                            var lcImgUrl = i.lcImgUrl
                            var sortNo = i.sortNo
                            var memo = i.memo
                            db.insertOptionCategory(EstimateOptionCategory(lcCd, lcNm, lcImgUrl, sortNo, memo))
                        }
                    }
                }

                //옵션디테일
                if (call_McVer) {
                    BaseUtil.LogErr("call_McVer---->>"+call_McVer)
                    val Data =  Service_retrofitApi!!.EstimateOptionDetail(0)// 0 전체조회
                    if (Data.isSuccessful) {
                        db.DeleteTable(db.TB_OPTION_DETAIL)
                        for (i in Data.body()!!.list) {
                            var mcCd = i.mcCd
                            var lcCd = i.lcCd
                            var mcNm = i.mcNm
                            var mcImgUrl = i.mcImgUrl
                            var sortNo = i.sortNo
                            var memo = i.memo
                            db.insertOptionDetail(EstimateTypeA(mcCd, lcCd, mcNm, mcImgUrl, sortNo, memo))
                        }
                    }
                }

                //버전검색
//                if (call_SearchVer) {
//                    val Data =  Service_retrofitApi!!.SearchList("0")// 0 전체조회
//                    if (Data.isSuccessful) {
//                        db.DeleteTable(db.TB_SEARCH)
//                        for (i in Data.body()!!.list) {
//                            var num = i.num
//                            var cd = i.cd
//                            var nm = i.nm
//                            var tUrl = i.tUrl
//                            var gb = i.gb
//                            var tableCd = i.tableCd
//                            var txt = i.txt
////                            db.insertSearch(SearchList(num, cd, nm, tUrl, gb, tableCd, txt))
//                        }
//                    }
//                }

            }
//        } catch (e: CancellationException) {
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }
}