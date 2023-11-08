@file:Suppress("DEPRECATION")

package com.infovine.tour.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


object BaseUtil {

    //메인 하단 버튼 상태값(아이콘 이미지 변경용)
    var MainBottomBtnFlag = 0

    const val isDebug = true

    private var mLastClickTime: Long = 0

    fun DoubleClickChecker(time: Int): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < time) {
            return false
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return true
    }

    fun LogErr(tag: String?, text: String?) {
        if (text == null) {
            return
        }
        try {
            if (isDebug) {
                Log.e(tag, text)
            }
        } catch (e: Exception) {
            if (isDebug) {
                println(text)
            }
        }
    }

    fun LogErr(text: String?) {
        if (text == null) {
            return
        }
        try {
            if (isDebug) {
                LogErr("tour", text)
            }
        } catch (e: Exception) {
            if (isDebug) {
                println(text)
            }
        }
    }

    fun ErrorCode(code:String):String {
        var message = ""
        message = when (code) {
            "E9993" -> "SNS인증실패"
            "E9994" -> "비가입자"
            "E9995" -> "이미가입자"
            "E9997" -> "내부시스템오류"
            "E9998" -> "입력값오류"
            "E9999" -> "알수없는오류"
            else -> ""
        }
        return message
    }

    fun getMonth():String {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val month = SimpleDateFormat("MM")
        var getTime: String = month.format(date)

        if (getTime.length == 2) {
            if(getTime.substring(0,1) == "0") {
                getTime = getTime.substring(1,2)
            }
        }
        return getTime
    }

    fun ActivityFullScreen(act: Activity) {

        act.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }


    /** 두 날짜 사이의 간격 계산해서 텍스트로 반환 */
    fun intervalBetweenDateText(beforeDate: String): String {
        //현재 시간
        val nowFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getTime())
        val beforeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beforeDate)

        val diffSec     = (nowFormat.time - beforeFormat.time) / 1000                                           //초 차이
        val diffMin     = (nowFormat.time - beforeFormat.time) / (60*1000)                                      //분 차이
        val diffHor     = (nowFormat.time - beforeFormat.time) / (60 * 60 * 1000)                               //시 차이
        val diffDays    = diffSec / (24 * 60 * 60)                                                              //일 차이
        val diffMonths  = (nowFormat.year*12 + nowFormat.month) - (beforeFormat.year*12 + beforeFormat.month)   //월 차이
        val diffYears   = nowFormat.year - beforeFormat.year                                                    //연도 차이

        if(diffYears > 0){
            return "${diffYears}년 전"
        }
        if(diffMonths > 0){
            return "${diffMonths}월 전"
        }
        if (diffDays > 0){
            return "${diffDays}일 전"
        }
        if(diffHor > 0){
            return "${diffHor}시간 전"
        }
        if(diffMin > 0){
            return "${diffMin}분 전"
        }
        if(diffSec > 0){
            return "${diffSec}초 전"
        }
        return ""
    }

    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    fun getTime(): String {
        var now = System.currentTimeMillis()
        var date = Date(now)

        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var getTime = dateFormat.format(date)

        return getTime
    }

    fun getToday(): String {
        var now = System.currentTimeMillis()
        var date = Date(now)

        var dateFormat = SimpleDateFormat("yyyy.MM.dd")
        var getTime = dateFormat.format(date)

        return getTime
    }

    fun getTodayText(): String {
        var now = System.currentTimeMillis()
        var date = Date(now)

        var dateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
        var getTime = dateFormat.format(date)

        return getTime
    }

    fun getTimePlusSevenDay(): String {
        var now = System.currentTimeMillis() + 1000*60*60*168
        var date = Date(now)

        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var getTime = dateFormat.format(date)

        return getTime
    }

    fun AppStart(url:String, mContext:Context) {
        var pakageName = url
        try {
            val intent = mContext!!.packageManager.getLaunchIntentForPackage(pakageName)
            try {
                if (intent != null) {
                    mContext!!.startActivity(intent)
                } else {
                    val marketIntent = Intent(Intent.ACTION_VIEW)
                    marketIntent.data = Uri.parse("market://details?id=" + pakageName)
                    mContext!!.startActivity(marketIntent)
                }
            } catch (e1: Exception) {
            }
        } catch (e: Exception) {
        }
    }

    fun permission(activity: Activity?) :Boolean {
        val isGranted = ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        return isGranted
    }


    fun endTime(beforeDate: String): String {
        //현재 시간
        val nowFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beforeDate)
        val beforeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getTime())

        val diffSec     = (nowFormat.time - beforeFormat.time) / 1000                                           //초 차이
        val diffMin     = (nowFormat.time - beforeFormat.time) / (60*1000)                                      //분 차이
        val diffHor     = (nowFormat.time - beforeFormat.time) / (60 * 60 * 1000)                               //시 차이
        val diffDays    = diffSec / (24 * 60 * 60)                                                              //일 차이
        val diffMonths  = (nowFormat.year*12 + nowFormat.month) - (beforeFormat.year*12 + beforeFormat.month)   //월 차이
        val diffYears   = nowFormat.year - beforeFormat.year                                                    //연도 차이

        if(diffYears > 0){
            return "${diffYears}년전"
        }
        if(diffMonths > 0){
            return "${diffMonths}월전"
        }
        if (diffDays > 0){
            return "${diffDays}일전"
        }
        if(diffHor > 0){
            return "${diffHor}시간전"
        }
        if(diffMin > 0){
            return "${diffMin}분전"
        }
        if(diffSec > 0){
            return "${diffSec}초전"
        }
        return ""
    }

    fun WebViewSetting(webView: WebView) {
        val settings = webView!!.settings
        settings.javaScriptEnabled = true
        settings.allowFileAccess = true
        settings.setSupportMultipleWindows(true)
        settings.pluginState = WebSettings.PluginState.ON
        settings.displayZoomControls = false
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.databaseEnabled = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_NORMAL
        settings.textZoom = 100
    }

    fun StartCity(): MutableMap<String, ArrayList<String>> {
        val city: MutableMap<String, ArrayList<String>> = HashMap()

        val seoul = ArrayList<String>()
        seoul.add("종로구")
        seoul.add("중구")
        seoul.add("용산구")
        seoul.add("성동구")
        seoul.add("동대문구")
        seoul.add("중랑구")
        seoul.add("성북구")
        seoul.add("강북구")
        seoul.add("도봉구")
        seoul.add("노원구")
        seoul.add("은평구")
        seoul.add("서대문구")
        seoul.add("마포구")
        seoul.add("양천구")
        seoul.add("강서구")
        seoul.add("구로구")
        seoul.add("금천구")
        seoul.add("영등포구")
        seoul.add("동작구")
        seoul.add("관악구")
        seoul.add("서초구")
        seoul.add("강남구")
        seoul.add("송파구")
        seoul.add("강동구")

        val busan = ArrayList<String>()
        busan.add("중구")
        busan.add("서구")
        busan.add("동구")
        busan.add("영도구")
        busan.add("부산진구")
        busan.add("동래구")
        busan.add("남구")
        busan.add("북구")
        busan.add("강서구")
        busan.add("해운대구")
        busan.add("사하구")
        busan.add("금정구")
        busan.add("연제구")
        busan.add("수영구")
        busan.add("사상구")
        busan.add("기장군")

        val daegu = ArrayList<String>()
        daegu.add("중구")
        daegu.add("동구")
        daegu.add("서구")
        daegu.add("남구")
        daegu.add("북구")
        daegu.add("수성구")
        daegu.add("달서구")
        daegu.add("달성군")
        daegu.add("군위군")

        val incheon = ArrayList<String>()
        incheon.add("중구")
        incheon.add("동구")
        incheon.add("미추홀구")
        incheon.add("연수구")
        incheon.add("남동구")
        incheon.add("부평구")
        incheon.add("계양구")
        incheon.add("서구")
        incheon.add("강화군")
        incheon.add("옹진군")

        val Gwangju = ArrayList<String>()
        Gwangju.add("동구")
        Gwangju.add("서구")
        Gwangju.add("남구")
        Gwangju.add("북구")
        Gwangju.add("광산구")

        val Daejeon = ArrayList<String>()
        Daejeon.add("동구")
        Daejeon.add("중구")
        Daejeon.add("서구")
        Daejeon.add("유성구")
        Daejeon.add("대덕구")

        val Ulsan = ArrayList<String>()
        Ulsan.add("중구")
        Ulsan.add("남구")
        Ulsan.add("동구")
        Ulsan.add("북구")
        Ulsan.add("울주군")

        val sejong = ArrayList<String>()
        sejong.add("세종특별자치시")

        val Gyeonggi = ArrayList<String>()
        Gyeonggi.add("수원시")
        Gyeonggi.add("성남시")
        Gyeonggi.add("의정부시")
        Gyeonggi.add("안양시")
        Gyeonggi.add("부천시")
        Gyeonggi.add("광명시")
        Gyeonggi.add("동두천시")
        Gyeonggi.add("평택시")
        Gyeonggi.add("안산시")
        Gyeonggi.add("고양시")
        Gyeonggi.add("과천시")
        Gyeonggi.add("구리시")
        Gyeonggi.add("남양주시")
        Gyeonggi.add("오산시")
        Gyeonggi.add("시흥시")
        Gyeonggi.add("군포시")
        Gyeonggi.add("의왕시")
        Gyeonggi.add("하남시")
        Gyeonggi.add("용인시")
        Gyeonggi.add("파주시")
        Gyeonggi.add("이천시")
        Gyeonggi.add("안성시")
        Gyeonggi.add("김포시")
        Gyeonggi.add("화성시")
        Gyeonggi.add("광주시")
        Gyeonggi.add("양주시")
        Gyeonggi.add("포천시")
        Gyeonggi.add("여주시")
        Gyeonggi.add("연천군")
        Gyeonggi.add("가평군")
        Gyeonggi.add("양평군")

        val Gangwon = ArrayList<String>()
        Gangwon.add("춘천시")
        Gangwon.add("원주시")
        Gangwon.add("강릉시")
        Gangwon.add("동해시")
        Gangwon.add("태백시")
        Gangwon.add("속초시")
        Gangwon.add("삼척시")
        Gangwon.add("홍천군")
        Gangwon.add("횡성군")
        Gangwon.add("영월군")
        Gangwon.add("평창군")
        Gangwon.add("정선군")
        Gangwon.add("철원군")
        Gangwon.add("화천군")
        Gangwon.add("양구군")
        Gangwon.add("인제군")
        Gangwon.add("고성군")
        Gangwon.add("양양군")

        val Chungcheong = ArrayList<String>()
        Chungcheong.add("청주시")
        Chungcheong.add("충주시")
        Chungcheong.add("제천시")
        Chungcheong.add("보은군")
        Chungcheong.add("옥천군")
        Chungcheong.add("영동군")
        Chungcheong.add("증평군")
        Chungcheong.add("진천군")
        Chungcheong.add("괴산군")
        Chungcheong.add("음성군")
        Chungcheong.add("단양군")

        val Chungnam = ArrayList<String>()
        Chungnam.add("천안시")
        Chungnam.add("공주시")
        Chungnam.add("보령시")
        Chungnam.add("아산시")
        Chungnam.add("서산시")
        Chungnam.add("논산시")
        Chungnam.add("계룡시")
        Chungnam.add("당진시")
        Chungnam.add("금산군")
        Chungnam.add("부여군")
        Chungnam.add("서천군")
        Chungnam.add("청양군")
        Chungnam.add("홍성군")
        Chungnam.add("예산군")
        Chungnam.add("태안군")

        val Jeollabuk = ArrayList<String>()
        Jeollabuk.add("전주시")
        Jeollabuk.add("군산시")
        Jeollabuk.add("익산시")
        Jeollabuk.add("정읍시")
        Jeollabuk.add("남원시")
        Jeollabuk.add("김제시")
        Jeollabuk.add("완주군")
        Jeollabuk.add("진안군")
        Jeollabuk.add("무주군")
        Jeollabuk.add("장수군")
        Jeollabuk.add("임실군")
        Jeollabuk.add("순창군")
        Jeollabuk.add("고창군")
        Jeollabuk.add("부안군")

        val Jeollanam = ArrayList<String>()
        Jeollanam.add("목포시")
        Jeollanam.add("여수시")
        Jeollanam.add("순천시")
        Jeollanam.add("나주시")
        Jeollanam.add("광양시")
        Jeollanam.add("담양군")
        Jeollanam.add("곡성군")
        Jeollanam.add("구례군")
        Jeollanam.add("고흥군")
        Jeollanam.add("보성군")
        Jeollanam.add("화순군")
        Jeollanam.add("장흥군")
        Jeollanam.add("강진군")
        Jeollanam.add("해남군")
        Jeollanam.add("영암군")
        Jeollanam.add("무안군")
        Jeollanam.add("함평군")
        Jeollanam.add("영광군")
        Jeollanam.add("장성군")
        Jeollanam.add("완도군")
        Jeollanam.add("진도군")
        Jeollanam.add("신안군")

        val Gyeongsangbukdo = ArrayList<String>()
        Gyeongsangbukdo.add("포항시")
        Gyeongsangbukdo.add("경주시")
        Gyeongsangbukdo.add("김천시")
        Gyeongsangbukdo.add("안동시")
        Gyeongsangbukdo.add("구미시")
        Gyeongsangbukdo.add("영주시")
        Gyeongsangbukdo.add("영천시")
        Gyeongsangbukdo.add("상주시")
        Gyeongsangbukdo.add("문경시")
        Gyeongsangbukdo.add("경산시")
        Gyeongsangbukdo.add("의성군")
        Gyeongsangbukdo.add("청송군")
        Gyeongsangbukdo.add("영양군")
        Gyeongsangbukdo.add("영덕군")
        Gyeongsangbukdo.add("청도군")
        Gyeongsangbukdo.add("고령군")
        Gyeongsangbukdo.add("성주군")
        Gyeongsangbukdo.add("칠곡군")
        Gyeongsangbukdo.add("예천군")
        Gyeongsangbukdo.add("봉화군")
        Gyeongsangbukdo.add("울진군")
        Gyeongsangbukdo.add("울릉군")

        val Gyeongsangnamdo = ArrayList<String>()
        Gyeongsangnamdo.add("창원시")
        Gyeongsangnamdo.add("진주시")
        Gyeongsangnamdo.add("통영시")
        Gyeongsangnamdo.add("사천시")
        Gyeongsangnamdo.add("김해시")
        Gyeongsangnamdo.add("밀양시")
        Gyeongsangnamdo.add("거제시")
        Gyeongsangnamdo.add("양산시")
        Gyeongsangnamdo.add("의령군")
        Gyeongsangnamdo.add("함안군")
        Gyeongsangnamdo.add("창녕군")
        Gyeongsangnamdo.add("고성군")
        Gyeongsangnamdo.add("남해군")
        Gyeongsangnamdo.add("하동군")
        Gyeongsangnamdo.add("산청군")
        Gyeongsangnamdo.add("함양군")
        Gyeongsangnamdo.add("거창군")
        Gyeongsangnamdo.add("합천군")

        val jeju = ArrayList<String>()
        jeju.add("제주시")
        jeju.add("서귀포시")

        city["서울특별시"] = seoul
        city["부산광역시"] = busan
        city["대구광역시"] = daegu
        city["인천광역시"] = incheon
        city["광주광역시"] = Gwangju
        city["대전광역시"] = Daejeon
        city["울산관역시"] = Ulsan
        city["세종특별자치시"] = sejong
        city["경기도"] = Gyeonggi
        city["강원특별자치도"] = Gangwon
        city["충청북도"] = Chungcheong
        city["충청남도"] = Chungnam
        city["전라북도"] = Jeollabuk
        city["전라남도"] = Jeollanam
        city["경상북도"] = Gyeongsangbukdo
        city["경상남도"] = Gyeongsangnamdo
        city["제주특별자치도"] = jeju

        return city
    }

    fun City(): ArrayList<String> {
        var city = ArrayList<String>()
        city.add("서울특별시")
        city.add("부산광역시")
        city.add("대구광역시")
        city.add("인천광역시")
        city.add("광주광역시")
        city.add("대전광역시")
        city.add("울산관역시")
        city.add("세종특별자치시")
        city.add("경기도")
        city.add("강원특별자치도")
        city.add("충청북도")
        city.add("충청남도")
        city.add("전라북도")
        city.add("전라남도")
        city.add("경상북도")
        city.add("경상남도")
        city.add("제주특별자치도")

        return city
    }

    fun startCity(): ArrayList<String> {
        var city = ArrayList<String>()
        city.add("인천")
        city.add("김포")
        city.add("대구")
        city.add("김해")
        city.add("여수")
        city.add("청주")
        city.add("제주")
        city.add("울산")
        city.add("무안")
        city.add("광주")
        city.add("군산")
        return city
    }



}