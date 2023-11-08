package com.infovine.tour.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.infovine.tour.R
import com.infovine.tour.activity.LoginActivity
import com.infovine.tour.utils.BaseUtil

class GoogleLoginFragment : Fragment() {

    private val TAG = "#GoogoleLoginFragment"
    var google_login: LinearLayout? = null
    var mContext: Context? = null
    var mGoogleSignInClient : GoogleSignInClient? = null
    private var callBackListener: GoogleCallBackListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (mContext is GoogleCallBackListener) callBackListener = mContext as GoogleCallBackListener?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(mContext!!, gso)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView: View = inflater.inflate(R.layout.fragment_google_login, container, false)
        google_login = mView.findViewById(R.id.login_google_btn)
        google_login?.setOnClickListener(googleBtnListener())
        return mView
    }

    inner class googleBtnListener : View.OnClickListener {
        override fun onClick(v: View?) {
            LoginActivity.progressLayer!!.isVisible = true
//            firebaseAnalytics.logEvent("구글 로그인 진행"){
//                param("OS", "Android")
//            }

            if(!LoginActivity.loginFlag) {
                return
            }
            LoginActivity.loginFlag = false
            val signInIntent = mGoogleSignInClient!!.signInIntent
            googleLoginLauncher.launch(signInIntent)
        }
    }

    var googleLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == -1) {
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            getGoogleInfo(task)
        }
        if (result.resultCode == 0) {
            LoginActivity.loginFlag = true
            LoginActivity.progressLayer!!.isVisible = false
        }
    }

    fun getGoogleInfo(completedTask: Task<GoogleSignInAccount>) {
        try {
            val TAG = "구글 로그인 결과"
            val account = completedTask.getResult(ApiException::class.java)

            var mToken = account.idToken.toString()
            var mEmail = account.email.toString()
            var mNickname = account.displayName.toString()
            var mThumImage = ""
            if(account.photoUrl != null) {
                mThumImage = account.photoUrl.toString()
            } else {
                mThumImage = ""
            }
            callBackListener!!.onGoogleCallBack(mToken, mEmail, mNickname, mThumImage)
        }
        catch (e: ApiException) {
            BaseUtil.LogErr(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    internal interface GoogleCallBackListener {
        fun onGoogleCallBack(token:String, email:String, nickname:String, thumbnailImageUrl:String)
    }


}