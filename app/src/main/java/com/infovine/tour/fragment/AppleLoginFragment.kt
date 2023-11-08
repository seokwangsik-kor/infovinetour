package com.infovine.tour.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.zze
import com.infovine.tour.R
import com.infovine.tour.activity.LoginActivity
import com.infovine.tour.utils.BaseUtil

class AppleLoginFragment : Fragment() {

    private val TAG = "#AppleLoginFragment"
    var apple_login: LinearLayout? = null
    var mContext: Context? = null
    private var callBackListener: AppleCallBackListener? = null

    private lateinit var mProvider: OAuthProvider.Builder
    private lateinit var mAuth: FirebaseAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (mContext is AppleCallBackListener) callBackListener = mContext as AppleCallBackListener?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        mProvider = OAuthProvider.newBuilder("apple.com")
        mProvider.scopes = listOf("email", "name") //로그인 후 받고 싶은 유저 정보 범위
//        mProvider.scopes = listOf("email") //로그인 후 받고 싶은 유저 정보 범위
        mProvider.addCustomParameter("locale", "ko")
        mAuth = FirebaseAuth.getInstance()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView : View = inflater.inflate(R.layout.fragment_apple_login, container, false)
        apple_login = mView.findViewById(R.id.login_apple_btn)
        apple_login?.setOnClickListener(appleBtnListener())
        return mView
    }

    inner class appleBtnListener : View.OnClickListener {
        override fun onClick(v: View?) {
            LoginActivity.progressLayer!!.isVisible = true
//            firebaseAnalytics.logEvent("애플 로그인 진행"){
//                param("OS", "Android")
//            }

            if(!LoginActivity.loginFlag) {
                return
            }
            LoginActivity.loginFlag = false
            val pending = mAuth.pendingAuthResult
            if (pending != null) {
                pending.addOnSuccessListener { authResult ->
                    // Get the user profile with authResult.getUser() and
                    // authResult.getAdditionalUserInfo(), and the ID
                    // token from Apple with authResult.getCredential().
                }.addOnFailureListener { e ->
                }
            } else {
                mAuth.startActivityForSignInWithProvider(mContext!! as Activity, mProvider.build())
                    .addOnSuccessListener { authResult ->

                        var mToken = (authResult.credential as zze).idToken.toString()

                        try {
                            var mNickname = ""
                            var mEmail = ""
                            if(authResult.user!!.providerData.size == 2) {
                                if(!TextUtils.isEmpty(authResult.user!!.providerData[0].displayName)){
                                    mNickname = authResult.user!!.providerData[0].displayName.toString()
                                } else {
                                    mNickname =""
                                }
                            }

                            if(authResult.user!!.providerData.size == 2) {
                                if(!TextUtils.isEmpty(authResult.user!!.providerData[1].email)){
                                    mEmail = authResult.user!!.providerData[1].email.toString()
                                } else {
                                    mEmail =""
                                }
                            }

                            callBackListener!!.onAppleCallBack(mToken, mEmail, mNickname,  "")
                        } catch (e: Exception) {
                            BaseUtil.LogErr(TAG, e.toString())
                        }

                    }
                    .addOnFailureListener { e ->
                        LoginActivity.loginFlag = true
                        LoginActivity.progressLayer!!.isVisible = false
                    }
            }
        }
    }

    internal interface AppleCallBackListener {
        fun onAppleCallBack(token:String, email:String, nickname:String, thumbnailImageUrl:String)
    }
}