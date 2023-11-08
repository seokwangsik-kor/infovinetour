package com.infovine.tour.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.infovine.tour.R
import com.infovine.tour.activity.LoginActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient


class KakaoLoginFragment : Fragment() {

    private val TAG = "#KakaoLoginFragment"
    var kakao_login: LinearLayout? = null
    var mContext:Context? = null
    private var callBackListener: KakaoCallBackListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (mContext is KakaoCallBackListener) callBackListener = mContext as KakaoCallBackListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView: View = inflater.inflate(R.layout.fragment_kakao_login, container, false)
        kakao_login = mView.findViewById(R.id.login_kakao_btn)
        kakao_login?.setOnClickListener(kakaoBtnListener())
        return mView
    }

    inner class kakaoBtnListener : View.OnClickListener {
        override fun onClick(v: View?) {

            LoginActivity.progressLayer!!.isVisible = true
//            firebaseAnalytics.logEvent("카카오 로그인 진행"){
//                param("OS", "Android")
//            }

            if(!LoginActivity.loginFlag) {
                return
            }
            LoginActivity.loginFlag = false

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Toast.makeText(mContext, "카카오계정으로 로그인 실패", Toast.LENGTH_LONG).show()
                    LoginActivity.loginFlag = true
                    LoginActivity.progressLayer!!.isVisible = false
                } else if (token != null) {
                }
            }


            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(mContext!!)) {
                UserApiClient.instance.loginWithKakaoTalk(mContext!!) { token, error ->
                    if (error != null) {
                        Toast.makeText(mContext, "카카오톡으로 로그인 실패", Toast.LENGTH_LONG).show()
                        LoginActivity.loginFlag = true
                        LoginActivity.progressLayer!!.isVisible = false
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(mContext!!, callback = callback)
                    } else if (token != null) {
                        // 사용자 정보 요청 (기본)
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                            } else if (user != null) {
//                                Log.i(
//                                    TAG, "사용자 정보 요청 성공" +
//                                            "\n회원번호: ${user.id}" +
//                                            "\n이메일: ${user.kakaoAccount?.email}" +
//                                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
//                                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
//                                )
                                var mTokon = token.accessToken
                                var mEmail = user.kakaoAccount?.email
                                var mNickname = user.kakaoAccount?.profile?.nickname
                                var mThumImage = user.kakaoAccount?.profile?.thumbnailImageUrl

                                callBackListener?.onKakaoCallBack(mTokon!!, mEmail, mNickname!!, mThumImage!!)
                            }
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(mContext!!, callback = callback)
            }
        }
    }


    internal interface KakaoCallBackListener {
        fun onKakaoCallBack(token:String, email:String?, nickname:String?, thumbnailImageUrl:String?)
    }

}