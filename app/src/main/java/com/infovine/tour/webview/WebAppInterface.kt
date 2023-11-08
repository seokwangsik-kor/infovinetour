package com.infovine.tour.webview

import android.content.Context


class WebAppInterface(conText : Context) {

    private var mContext: Context? = null
    private var callBackListener : WebAppInterfaceCallBackListener? = null

    init {
        mContext = conText
        if(mContext is WebAppInterfaceCallBackListener) callBackListener = mContext as WebAppInterfaceCallBackListener
    }

//    @JavascriptInterface
//    fun popWebview(url:String) {
//        MainActivity.popupFlag = false
//        callBackListener!!.onWebAppCallBack(url)
//    }
//
//    @JavascriptInterface
//    fun popClose() {
//        callBackListener!!.onWebAppCallBack("close")
//    }
//
//    @JavascriptInterface
//    fun popConfirm() {
//        var base:BaseActivity = mContext as BaseActivity
//        base.getMyPick();
//        callBackListener!!.onWebAppCallBack("confirm")
//    }
//
//    @JavascriptInterface
//    fun loadUserToken():String {
//        var jwtToken = SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN)
////        Toast.makeText(mContext, jwtToken, Toast.LENGTH_LONG).show()
//        return jwtToken
//    }
//
//    @JavascriptInterface
//    fun sendAppData(notionURL:String, catecoryCD:String, c:Int) {
//        val intent= Intent(mContext, AppDetailActivity::class.java)
//        intent.putExtra("notionURL", notionURL)
//        intent.putExtra("catecoryCD", catecoryCD)
//        intent.putExtra("appCD", c)
//        mContext!!.startActivity(intent)
//    }


    internal interface WebAppInterfaceCallBackListener {
        fun onWebAppCallBack(url:String)
    }


}

