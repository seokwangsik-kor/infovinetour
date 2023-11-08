package com.infovine.tour.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.infovine.tour.R
import com.infovine.tour.activity.LoginActivity
import com.infovine.tour.utils.BaseUtil
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class NaverLoginFragment : Fragment() {

    var naver_login: LinearLayout? = null
    var mContext: Context? = null
    private var callBackListener: NaverCallBackListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (mContext is NaverCallBackListener) callBackListener = mContext as NaverCallBackListener?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        val naverClientId = getString(R.string.naver_client_id)
        val naverClientSecret = getString(R.string.naver_client_secret)
        val naverClientName = "인포파인투어 네이버 아이디 로그인"
        NaverIdLoginSDK.initialize(mContext!!, naverClientId, naverClientSecret , naverClientName)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_naver_login, container, false)
        naver_login = view.findViewById(R.id.login_naver_btn)
        naver_login!!.setOnClickListener {
            naverLogin()
        }
        return view
    }

    private fun naverLogin(){

        LoginActivity.progressLayer!!.isVisible = true
        if(!LoginActivity.loginFlag) {
            return
        }
        LoginActivity.loginFlag = false

        var accessToken = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                var mEmail = response.profile!!.email.toString()
                var mNickname = response.profile!!.nickname.toString()
                var mThumImage = response.profile!!.profileImage.toString()
                callBackListener!!.onNaverCallBack(accessToken, mEmail, mNickname, mThumImage)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }


        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                BaseUtil.LogErr("로그인 성공")
                BaseUtil.LogErr("AccessToken -> ${NaverIdLoginSDK.getAccessToken()}")
                BaseUtil.LogErr("RefreshToken -> ${NaverIdLoginSDK.getRefreshToken()}")
                BaseUtil.LogErr("Expires -> ${NaverIdLoginSDK.getExpiresAt()}")
                BaseUtil.LogErr("Type -> ${NaverIdLoginSDK.getTokenType()}")
                BaseUtil.LogErr("State -> ${NaverIdLoginSDK.getState()}")

                accessToken = NaverIdLoginSDK.getAccessToken().toString()
                NidOAuthLogin().callProfileApi(profileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                BaseUtil.LogErr("errorCode:$errorCode, errorDesc:$errorDescription")
                Toast.makeText(mContext, "네이버계정으로 로그인 실패", Toast.LENGTH_LONG).show()
                LoginActivity.loginFlag = true
                LoginActivity.progressLayer!!.isVisible = false
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
                Toast.makeText(mContext, "네이버계정으로 로그인 실패", Toast.LENGTH_LONG).show()
                LoginActivity.loginFlag = true
                LoginActivity.progressLayer!!.isVisible = false
            }
        }
        NaverIdLoginSDK.authenticate(mContext!!, oauthLoginCallback)
    }


    internal interface NaverCallBackListener {
        fun onNaverCallBack(token:String, email:String, nickname:String, thumbnailImageUrl:String)
    }

}