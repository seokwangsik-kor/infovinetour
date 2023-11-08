package com.infovine.tour.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.infovine.tour.R
import com.infovine.tour.activity.BaseActivity

class PopupWebViewFragment : DialogFragment() {

    var mContext: Context? = null
    var mUrl: String? = null
//    var mType: String? = null
//    var topLayer: LinearLayout? = null
//    var layerFlag: Boolean? = false
    var backBtn: ImageView? = null
    var title_txt: TextView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        setStyle(STYLE_NO_TITLE, R.style.DialogFragmentTheme)
        var mData = arguments
        if (mData != null) {
            mUrl = mData.getString("bundleUrl")
//            layerFlag = mData.getBoolean("topLayer")
//            mType = mData.getString("type")
        }
    }

    companion object{
        var popupWebView: WebView? = null
        fun popupWebViewReload() {
            popupWebView!!.post(Runnable {
                popupWebView!!.reload()
            })
        }

        var popupWebViewFlag = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                super.onBackPressed()
                popupWebViewFlag = false
                popupWebView = null
            }
        }
    }

    @SuppressLint("JavascriptInterface")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_popup_web_view, container, false)

        backBtn = mView.findViewById(R.id.popup_webview_back_btn)
        backBtn!!.setOnClickListener {
            dismiss()
        }
        popupWebView = mView.findViewById(R.id.popup_webview)
        WebViewSettings(mContext!!, popupWebView)
        popupWebView!!.addJavascriptInterface(WebAppInterface(mContext!!), "infovineTour")
        popupWebView!!.webViewClient = webViewClient()
        popupWebView!!.webChromeClient = chromeClient()
        popupWebView!!.loadUrl(mUrl!!)
        dialog!!.setCancelable(true)
        popupWebViewFlag = true
        return mView
    }

    private inner class chromeClient : WebChromeClient() {
        override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
            return super.onJsAlert(view, url, message, result)
        }
    }

    private inner class webViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

//            if (Uri.parse(url).scheme == "intent"){
//                BaseUtil.LogErr("<UrlLoading>","intent url getScheme1 : " + Uri.parse(url).scheme)
//
//                try {
//                    val i = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
//                    val existPackage = mContext!!.packageManager.getLaunchIntentForPackage(i.getPackage()!!)
//                    try {
//                        if (existPackage != null) {
//                            startActivity(i)
//                        } else {
//                            val marketIntent = Intent(Intent.ACTION_VIEW)
//                            marketIntent.data = Uri.parse("market://details?id=" + i.getPackage())
//                            startActivity(marketIntent)
//                        }
//                    } catch (e1: Exception) {
//                        e1.printStackTrace()
//                    }
//                } catch (e: Exception) {
//                    BaseUtil.LogErr(TAG, "intentloadingerr: " + e.message)
//                }
//                return true
//            } else if(Uri.parse(url).scheme != "http" && Uri.parse(url).scheme != "https") {
//                BaseUtil.LogErr("<UrlLoading>","intent  url getScheme2 : " + Uri.parse(url).scheme +"   "+ Uri.parse(url))
//
//                try {
//                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                return true
//            }
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            super.onReceivedError(view, errorCode, description, failingUrl)

            when (errorCode) {
                ERROR_BAD_URL -> {//잘못된 url
//                    (mContext as BaseActivity).ShowExpireErrorDialog()
                }
                ERROR_HOST_LOOKUP -> {
//                    (mContext as BaseActivity).ShowExpireErrorDialog()
                }
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)

        }
    }

}