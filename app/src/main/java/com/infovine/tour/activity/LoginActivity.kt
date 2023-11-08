package com.infovine.tour.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.firebase.analytics.FirebaseAnalytics
import com.infovine.tour.R
import com.infovine.tour.data.DataResponse
import com.infovine.tour.data.Login
import com.infovine.tour.data.LoginResonse
import com.infovine.tour.fragment.AppleLoginFragment
import com.infovine.tour.fragment.GoogleLoginFragment
import com.infovine.tour.fragment.KakaoLoginFragment
import com.infovine.tour.fragment.NaverLoginFragment
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import com.kakao.sdk.common.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity(), KakaoLoginFragment.KakaoCallBackListener, GoogleLoginFragment.GoogleCallBackListener
    , AppleLoginFragment.AppleCallBackListener, NaverLoginFragment.NaverCallBackListener
{
    override fun onNaverCallBack(token: String, email: String, nickname: String, thumbnailImageUrl: String) {
        saveLoginInfo("4", token, email, nickname, thumbnailImageUrl)
    }

    override fun onAppleCallBack(token:String, email: String, nickname: String, thumbnailImageUrl: String) {
        saveLoginInfo("3", token, email, nickname, thumbnailImageUrl)
    }

    override fun onGoogleCallBack(token:String, email: String, nickname: String, thumbnailImageUrl: String) {
        saveLoginInfo("2", token, email, nickname, thumbnailImageUrl)
    }

    override fun onKakaoCallBack(token:String, email: String?, nickname: String?, thumbnailImageUrl: String?) {
        saveLoginInfo("1", token, email, nickname!!, thumbnailImageUrl)
    }

    fun saveLoginInfo(login_type:String, token:String, email: String?, nickname: String, thumbnailImageUrl: String?) {
        SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_SNSLOGIN_OS_TYPE, "aos")
        SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_SNSLOGIN_TYPE, login_type)
        SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_SNSLOGIN_ACCESS_TOKEN, token)
        SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_SNSLOGIN_EMAIL, email)
        SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_SNSLOGIN_NICKNAME, nickname)
        SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_SNSLOGIN_THUMNAIL_IMG, thumbnailImageUrl)
        postLogin()//로그인 Api 호출
        progressLayer!!.isVisible = false
    }

    private val TAG = "#LoginActivity"
    var loginAlt: LinearLayout? = null
    var loginAltConfirm: FrameLayout? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object{
        var loginFlag = true //연속클릭방지
        var progressLayer: LinearLayout? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressLayer = findViewById(R.id.login_progress_layer)

        var keyHash = Utility.getKeyHash(this)
        BaseUtil.LogErr(keyHash)

        SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_JWT_TOKEN, "")

    }

    private fun postLogin() {
        val loginData = Login(
            SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_PUSH_ID),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_SNSLOGIN_OS_TYPE),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_SNSLOGIN_NICKNAME),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_SNSLOGIN_TYPE).toInt(),
            SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_SNSLOGIN_ACCESS_TOKEN)
        )

        val call: Call<LoginResonse?>? = Auth_retrofitApi!!.login(loginData)
        call!!.enqueue(object : Callback<LoginResonse?> {
            override fun onResponse(call: Call<LoginResonse?>, response: Response<LoginResonse?>) {

                if(response.isSuccessful) {
                    BaseUtil.LogErr("res1  "+response.body())
                    var code = response.body()!!.resultCode

//                    if(code == "S0000" || code == "E9995") {//S0000성공, E9995이미가입자
                    if(code == "S00001") {//S00001성공
                        if(!TextUtils.isEmpty(response.body()!!.token)) {
                            SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_JWT_TOKEN, response.body()!!.token)
                            SharedPrefsUtils.setStringPreference(mContext!!, Const.PREF_MEMBER_PHONE_NUMBER, response.body()!!.hp)
                            SharedPrefsUtils.setIntegerPreference(mContext!!, Const.PREF_MEMBER_CD, response.body()!!.memcd)
                            val intent = Intent(mContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
//                        val intent = Intent(mContext, GlobalEstimateActivity::class.java)
//                        startActivity(intent)
//                        finish()
                    } else {

                        if(code == "E9994") {//비가입자

//                            val intent = Intent(mContext, GlobalEstimateActivity::class.java)
//                            startActivity(intent)`

                            val intent = Intent(mContext, TemrsActivity::class.java)
                            startActivity(intent)
//                            val intent = Intent(mContext, PhoneNumActivity::class.java)
//                            startActivity(intent)
                        }

                        if(code == "E9990") {//소유인증전화번호 없음
//                            loginAlt!!.isVisible = true
                        }

                        if(code == "E9993") {
//                            if (!TextUtils.isEmpty(response.body()!!.resulTMESSAGE)) {
                                Toast.makeText(mContext, response.body()!!.resulTMESSAGE, Toast.LENGTH_LONG).show()
//                            }
                        }

                        if(code == "E9991") {
//                            if (!TextUtils.isEmpty(response.body()!!.resulTMESSAGE)) {
                                Toast.makeText(mContext, "탈퇴된 계정입니다. 재가입은 7일 후에 가능합니다", Toast.LENGTH_LONG).show()
//                                Toast.makeText(mContext, response.body()!!.resulTMESSAGE, Toast.LENGTH_SHORT).show()
//                            }
                        }
                    }
                    loginFlag = true
                } else {
                    SeverErrorDialog()
                    loginFlag = true
                }
                progressLayer!!.isVisible = false
            }
            override fun onFailure(call: Call<LoginResonse?>, t: Throwable) {
                SeverErrorDialog()
                loginFlag = true
                progressLayer!!.isVisible = false
            }
        })
    }

    var waitTime = 0L
    override fun onBackPressed() {
        if(System.currentTimeMillis() - waitTime >=1500 ) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.finishAffinity(this)
        }
    }

    override fun onStop() {
        super.onStop()
        loginFlag=true
    }
}