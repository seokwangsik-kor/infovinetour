package com.infovine.tour.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.infovine.tour.R
import com.infovine.tour.data.*
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.Const.Companion.AREA_VER
import com.infovine.tour.utils.Const.Companion.DEST_VER
import com.infovine.tour.utils.Const.Companion.OPTION_CATEGORY
import com.infovine.tour.utils.Const.Companion.OPTION_DETAIL
import com.infovine.tour.utils.Const.Companion.OPTION_DETAIL_VER
import com.infovine.tour.utils.Const.Companion.OPTION_VER
import com.infovine.tour.utils.Const.Companion.SEARCH_VER
import com.infovine.tour.utils.Const.Companion.THEME_VER
import com.infovine.tour.utils.SharedPrefsUtils
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IntroActivity : BaseActivity() {

    val PERMISSION_INFO = 777
    var permis : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
//        Thread.sleep(3000)

        Handler().postDelayed({
            if (SharedPrefsUtils.getBooleanPreference(mContext!!, Const.APP_IS_FIRST, true)) {
                val intent = Intent(mContext, PermissionActivity::class.java)
                startActivityForResult(intent, PERMISSION_INFO)

                getVersion()
//            APIData()
//                return
            } else {
                requestNotificationPermission()
            }
        },1500)

//        Thread.sleep(2000)
    }

    fun getVersion() {
        val call: Call<ApiVersion?>? = Service_retrofitApi!!.getVersion()
        call!!.enqueue(object : Callback<ApiVersion?> {
            override fun onResponse(call: Call<ApiVersion?>, response: Response<ApiVersion?>) {
                if(response.isSuccessful){
                    var code = response.body()!!.resultCode

                    if (code=="S00001") {

                        if(response.body()!!.list.isEmpty()) return

                        var AreaVer = response.body()!!.list[0].areaVer
                        var DestVer = response.body()!!.list[0].destVer
                        var LcVer = response.body()!!.list[0].lcVer
                        var McVer = response.body()!!.list[0].mcVer
                        var OptDetailVer = response.body()!!.list[0].optDetailVer
                        var OptVer = response.body()!!.list[0].optVer
                        var ThemeVer = response.body()!!.list[0].themeVer
//                        var SearchVer = response.body()!!.list[0].searchVer

                        var cnt = 0
                        if (AreaVer != SharedPrefsUtils.getStringPreference(mContext, AREA_VER)) {
                            call_AreaVer = true
                            cnt++
                        }
                        if (DestVer != SharedPrefsUtils.getStringPreference(mContext, DEST_VER)) {
                            call_DestVer = true
                            cnt++
                        }
                        if (LcVer != SharedPrefsUtils.getStringPreference(mContext, OPTION_CATEGORY)) {
                            call_LcVer = true
                            cnt++
                        }
                        if (McVer != SharedPrefsUtils.getStringPreference(mContext, OPTION_DETAIL)) {
                            call_McVer = true
                            cnt++
                        }
                        if (OptDetailVer != SharedPrefsUtils.getStringPreference(mContext, OPTION_DETAIL_VER)) {
                            call_OptDetailVer = true
                            cnt++
                        }
                        if (OptVer != SharedPrefsUtils.getStringPreference(mContext, OPTION_VER)) {
                            call_OptVer = true
                            cnt++
                        }
                        if (ThemeVer != SharedPrefsUtils.getStringPreference(mContext, SEARCH_VER)) {
                            call_ThemeVer = true
                            cnt++
                        }
//                        if (SearchVer != SharedPrefsUtils.getStringPreference(mContext, SEARCH_VER)) {
//                            call_SearchVer = true
//                            cnt++
//                        }

                        SharedPrefsUtils.setStringPreference(mContext!!, AREA_VER, AreaVer)
                        SharedPrefsUtils.setStringPreference(mContext!!, DEST_VER, DestVer)
                        SharedPrefsUtils.setStringPreference(mContext!!, OPTION_CATEGORY, LcVer)
                        SharedPrefsUtils.setStringPreference(mContext!!, OPTION_DETAIL, McVer)
                        SharedPrefsUtils.setStringPreference(mContext!!, OPTION_DETAIL_VER, OptDetailVer)
                        SharedPrefsUtils.setStringPreference(mContext!!, OPTION_VER, OptVer)
                        SharedPrefsUtils.setStringPreference(mContext!!, THEME_VER, ThemeVer)
//                        SharedPrefsUtils.setStringPreference(mContext!!, THEME_VER, SearchVer)

                        if(cnt > 0) {
                            APIData()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ApiVersion?>, t: Throwable) {
                SeverErrorDialog()
            }
        })
    }

    private fun tokenRefresh() {
        BaseUtil.LogErr("JWT_TOKEN", SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN))
        if(!TextUtils.isEmpty(SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN))){
//            val reToken = TokenRefresh(SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN))
            var fcmToken = SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_PUSH_ID)
            val reToken = TokenRefresh(SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN),fcmToken)
            val call: Call<AuthResponseData?>? = Auth_retrofitApi!!.tokenRefresh(reToken)
            call!!.enqueue(object : Callback<AuthResponseData?> {
                override fun onResponse(call: Call<AuthResponseData?>, response: Response<AuthResponseData?>) {
                    if(response.isSuccessful) {
//                        Thread.sleep(3000)
//                        InstallVersion()
//                        BaseUtil.LogErr("JWT_TOKEN---> ${response.body()!!.token}")
                        if(response.body()!!.resultCode == "S00001") {
                            SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_JWT_TOKEN, response.body()!!.token)
                            SharedPrefsUtils.setIntegerPreference(mContext!!, Const.PREF_MEMBER_CD, response.body()!!.memcd)
                            SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_MEMBER_PHONE_NUMBER, response.body()!!.hp)
                            val intent = Intent(mContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            SharedPrefsUtils.setStringPreference(mContext,Const.PREF_SNSLOGIN_ACCESS_TOKEN,"")
                            val intent = Intent(mContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        SeverErrorDialog()
                    }
                }
                override fun onFailure(call: Call<AuthResponseData?>, t: Throwable) {
                    SeverErrorDialog()
                }
            })
        } else {
            val intent = Intent(mContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private val requestNotificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { ok ->
        if (ok) {
            tokenRefresh()
        } else {
            BaseUtil.LogErr("nono")
            requestNotificationPermission()
        }
    }

    fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 다른 런타임 퍼미션이랑 비슷한 과정
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // 왜 알림을 허용해야하는지 유저에게 알려주기를 권장
                    permis = true
                    presentNotificationSetting(mContext!!)
//                    AppUpdataInfo()
                } else {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//                    tokenRefresh()
                }
            }
        }else{
//            AppUpdataInfo()
            getVersion()
            tokenRefresh()
        }
        BaseUtil.LogErr("push", SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_PUSH_ID))
    }

    fun presentNotificationSetting(context: Context) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationSettingOreo(context)
        } else {
            notificationSettingOreoLess(context)
        }
        try {
            context.startActivity(intent)
        }catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun notificationSettingOreo(context: Context): Intent {
        return Intent().also { intent ->
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivityForResult(intent, PERMISSION_INFO)
        }
    }

    fun notificationSettingOreoLess(context: Context): Intent {
        return Intent().also { intent ->
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo?.uid)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivityForResult(intent, PERMISSION_INFO)
        }
    }

    fun AppUpdataInfo() {
//        val Token = SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN)
//        val info: PackageInfo = mContext!!.packageManager.getPackageInfo(mContext!!.packageName, 0)
//        var versionName = info.versionName
//        val call: Call<AppUpdate?>? = ServiceRetrofitClient().getInstance()!!.getRetrofitInterface()!!.AppUpdate("Bearer $Token", versionName)
//        call!!.enqueue(object : Callback<AppUpdate?> {
//            override fun onResponse(call: Call<AppUpdate?>, response: Response<AppUpdate?>) {
//                if (response.isSuccessful) {
//
//                    var code = response.body()!!.code
//                    if (code != "SUCCESS") return
//
//                    SharedPrefsUtils.setIntegerPreference(mContext!!, Const.PREF_UPDATE_CODE, response.body()!!.data.resultCode)
//                    SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_UPDATE_MSG, response.body()!!.data.resultMsg)
//                    SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_UPDATE_URL, response.body()!!.data.resultUrl)
//                    SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_UPDATE_VER, response.body()!!.data.resultVer)
//                    SharedPrefsUtils.setStringPreference(mContext!!, Const.IMAGE_DOMAIN_URL, response.body()!!.data.imgDomain)
//                    //0이면 토큰리프레시 , 1~2면 업데이트 팝업
////                    var isUpdate = SharedPrefsUtils.getIntegerPreference(mContext!!, Const.PREF_UPDATE_CODE, 0)
//                    var saveTime = SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_UPDATE_SEVENDAY)
//                    var resultCode = response.body()!!.data.resultCode
//                    if (resultCode == 2) {
//                        if (!TextUtils.isEmpty(saveTime)) {
//                            try {
//                                var time = BaseUtil.endTime(saveTime)
//                                time = time.substring(time.length-2, time.length-1)
//                                if(time == "간" || time == "분" || time == "초") {
//                                    goUpdateAct()
//                                } else {
//                                    tokenRefresh()
//                                }
//                            } catch (e :java.lang.Exception) {
//                                BaseUtil.LogErr(e.message)
//                                tokenRefresh()
//                            }
//                        } else {
//                            goUpdateAct()
//                        }
//                    } else if(resultCode == 1){
//                        goUpdateAct()
////                        tokenRefresh()
//                    } else if(resultCode == 0) {
//                        tokenRefresh()
//                    }
//                } else {
//                    SeverErrorDialog()
//                }
//            }
//            override fun onFailure(call: Call<AppUpdate?>, t: Throwable) {
//                SeverErrorDialog()
//            }
//        })

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PERMISSION_INFO) {
            requestNotificationPermission()
            return
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}