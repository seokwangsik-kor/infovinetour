package com.infovine.tour.webview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView

class WebViewSettings(context: Context, mWeb:WebView?) {

    var mContext: Context? = null
    var mWebView: WebView? = null

    init {
        init(mWeb)
        mContext = context
    }

    private fun init(webView:WebView?) {

        mWebView = webView

        val settings = mWebView!!.settings
        settings.javaScriptEnabled = true
        settings.allowFileAccess = true
        settings.setSupportMultipleWindows(true)
        settings.pluginState = WebSettings.PluginState.ON
//        settings.setSupportZoom(true)
//        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.javaScriptCanOpenWindowsAutomatically = true
//        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.databaseEnabled = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.textZoom = 100

    }
}