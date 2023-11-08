package com.infovine.tour.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.gson.JsonObject
import com.infovine.tour.R
import com.infovine.tour.data.*
import com.infovine.tour.utils.*
import org.json.JSONObject
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class PhoneNumActivity : BaseActivity() {

    val smsReceiver = MySMSReceiver()
    var vender_layout: View? = null
    var vender_tv: TextView? = null
    var phonenum_tv: EditText? = null
    var count: TextView? = null
    var certifynum_tv: EditText? = null
    var send_certify_num: TextView? = null
    var pnum_name: EditText? = null
    var pnum_backbtn:ImageView? = null

    var checkbox: CheckBox? = null
    var join_btn: View? = null
    var join_btn_txt : TextView? = null
//    var error_img: ImageView? = null
    var timer_end_tv: TextView? = null
    var timer_end_ok: TextView? = null
    var m_sec = 0
    private var mCountDown: CountDownTimer? = null

    val UPDATE_MESSAGE = 0

    var mdntype:Int = 1

    lateinit var permission: Array<String>

    var certNumIndex:Int = 0
//    var pnumConfirm = false

    var originPnum = ""

    var temrs_layer:LinearLayout? = null
    var temrs_check:CheckBox? = null
    var temrs_content:LinearLayout? = null
    var phone_confirm_status = false//번호인증, 번호변경 플래그

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_num)

        registerReceiver(smsReceiver, smsReceiver.doFilter())

        pnum_backbtn = findViewById(R.id.pnum_backbtn)
        pnum_backbtn!!.setOnClickListener { finish() }
        vender_layout = findViewById(R.id.vender_layout)
        vender_tv = findViewById(R.id.vender_tv)
        phonenum_tv = findViewById(R.id.phonenum_tv)
        count = findViewById(R.id.count)
        certifynum_tv = findViewById(R.id.certifynum_tv)
        send_certify_num = findViewById(R.id.send_certify_num)
        checkbox = findViewById(R.id.check_box)
        join_btn = findViewById(R.id.join_btn)
        join_btn_txt = findViewById(R.id.join_btn_txt)
//        error_img = findViewById(R.id.error_img)
        timer_end_tv = findViewById(R.id.timer_end_tv);
        timer_end_ok = findViewById(R.id.timer_end_ok)
        vender_layout!!.setOnClickListener{
            venderselect()
        }
        pnum_name = findViewById(R.id.pnum_name)

        temrs_layer = findViewById(R.id.phonenum_temrs_check_layer)
        temrs_layer!!.setOnClickListener {
            temrs_check!!.isChecked = !temrs_check!!.isChecked
            if (temrs_check!!.isChecked) {
//                temrs_check!!.isChecked = true
                if (!TextUtils.isEmpty(certifynum_tv!!.text.toString())) {
                    checkbox!!.isChecked = true
                    join_btn_txt!!.setTextColor((Color.parseColor("#ffffff")))
                }
            } else {
//                temrs_check!!.isChecked = false
                checkbox!!.isChecked = false
                join_btn_txt!!.setTextColor((Color.parseColor("#bebebe")))
            }
        }
        temrs_check = findViewById(R.id.phonenum_temrs_check)
        temrs_check!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkbox!!.isChecked = true
                join_btn_txt!!.setTextColor((Color.parseColor("#ffffff")))
            } else {
                checkbox!!.isChecked = false
                join_btn_txt!!.setTextColor((Color.parseColor("#bebebe")))
            }
        }
//        temrs_check!!.setOnClickListener {
//            temrs_check!!.isChecked = !temrs_check!!.isChecked
//            if (temrs_check!!.isChecked) {
//                join_btn_txt!!.setTextColor((Color.parseColor("#ffffff")))
//            } else {
//                join_btn_txt!!.setTextColor((Color.parseColor("#bebebe")))
//            }
//        }

        temrs_content = findViewById(R.id.phonenum_temrs_layer)
        temrs_content!!.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("bundleUrl", Const.TEMRS_2)
            popupFragment!!.arguments = bundle
            popupFragment!!.show(supportFragmentManager, "")
        }

        certifynum_tv!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                BaseUtil.LogErr("sss  "+s+"     "+s!!.length)
                if (temrs_check!!.isChecked) {
                    if (!TextUtils.isEmpty(certifynum_tv!!.text.toString())) {
                        checkbox!!.isChecked = s!!.length>3
                        join_btn_txt!!.setTextColor((Color.parseColor("#ffffff")))
                    }
                } else {
//                temrs_check!!.isChecked = false
                    checkbox!!.isChecked = false
                    join_btn_txt!!.setTextColor((Color.parseColor("#bebebe")))
                }
//                checkbox!!.isChecked = s!!.length>3
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        send_certify_num!!.setOnClickListener{
            if(TextUtils.isEmpty(pnum_name!!.text)) {
                Toast.makeText(mContext, "이름을 입력해 주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(phonenum_tv!!.text)) {
                Toast.makeText(mContext, "휴대폰 번호를 입력해 주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            BaseUtil.LogErr("번호요청")
            if(!BaseUtil.DoubleClickChecker(5000)){
                return@setOnClickListener
            }

            sendCertifyNum()
        }

        join_btn!!.setOnClickListener{
            if(checkbox!!.isChecked){
                if(originPnum != phonenum_tv!!.text.toString()) {
                    Toast.makeText(mContext, "잘못된 휴대번호 입니다", Toast.LENGTH_LONG).show()
//                    error_img!!.visibility = View.VISIBLE
                    return@setOnClickListener
                } else {
//                    error_img!!.visibility = View.GONE
                    if(temrs_check!!.isChecked) {

                        if(TextUtils.isEmpty(pnum_name!!.text.toString())) {
                            Toast.makeText(mContext, "이름을 입력해 주세요.", Toast.LENGTH_LONG).show()
                            return@setOnClickListener
                        }

                        if (TextUtils.isEmpty(certifynum_tv!!.text.toString())) {
                            Toast.makeText(mContext, "인증번호를 입력해 주세요.", Toast.LENGTH_LONG).show()
                            return@setOnClickListener
                        }
                        postDataReConfirm()
                    } else {
                        Toast.makeText(mContext, "제3자 정보 제공 동의 바랍니다.", Toast.LENGTH_LONG).show()
                    }
                }
//                if(pnumConfirm) {
//                    postDataReConfirm()
//                } else {
//                    postData()
//                }
            }
        }

        permission = if (Build.VERSION.SDK_INT > 29) {
            arrayOf(
                Manifest.permission.READ_PHONE_NUMBERS
            )
        } else {
            arrayOf(
                Manifest.permission.READ_PHONE_STATE
            )
        }

        phone_confirm_status = intent.getBooleanExtra("phone_confirm_status", false)

        //true면 번호변경
        if (phone_confirm_status) {
            phonenum_tv?.isEnabled = true
        } else {
            if (ActivityCompat.checkSelfPermission(mContext!!, permission.get(0)) != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permission,0)
                }
            }else{
                phonenum_tv?.isEnabled = false
                phonenum_tv?.setText(getPhoneNumber(mContext!!))
                if(getPhoneNumber(mContext!!).equals("")){
                    phonenum_tv?.isEnabled = true
                }
//            sendCertifyNum()
            }
        }

        venderselect()

//        pnumConfirm = intent.getBooleanExtra("E9990", false)//소유인증전화번호 없는 상태

        getAppSignatures(mContext!!)

    }

    fun getPhoneNumber(con: Context): String? {
        var phoneNumber = ""
        val telephonyManager = con.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        try {
            if (ActivityCompat.checkSelfPermission(
                    con,
                    Manifest.permission.READ_SMS
                ) != PackageManager.PERMISSION_GRANTED
            );
            ActivityCompat.checkSelfPermission(
                con,
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED
            ActivityCompat.checkSelfPermission(
                con,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
            run {}
            @SuppressLint("MissingPermission")
            val tmpPhoneNumber = telephonyManager.line1Number
//            var temprtn: String
//
//            BaseUtil.LogErr("tmpPhoneNumber  "+tmpPhoneNumber)
//
//            if (tmpPhoneNumber != null && tmpPhoneNumber !== "") {
//                if (tmpPhoneNumber.length > 11) {
//                    temprtn = "0" + tmpPhoneNumber.substring(3)
//                    phoneNumber = temprtn
//                    temprtn = ""
//                }
//                if (tmpPhoneNumber.substring(3, 4) == "0") // 0100xxx-xxxx
//                {
//                    temprtn = tmpPhoneNumber.substring(0, 3)
//                    temprtn += tmpPhoneNumber.substring(4)
//                    phoneNumber = temprtn
//                } // <------------------------------
//            }

            phoneNumber = tmpPhoneNumber.replace("+82", "0")
        } catch (e: java.lang.Exception) {
            phoneNumber = ""
            e.printStackTrace()
        }
        return phoneNumber
    }

    fun venderselect(){
        val telecomBottomSheet = TelecomBottomSheet(this)
        telecomBottomSheet.show()
        telecomBottomSheet.setItemClick(object : tel_adapter.Item_Click {
            override fun onItem_click(v: View?, position: Int) {
                when (position) {
                    0 -> {
                        mdntype = 1
                        vender_tv!!.text = mContext!!.resources.getStringArray(R.array.tel_list).get(position)
                    }
                    1 -> {
                        mdntype = 2
                        vender_tv!!.text = mContext!!.resources.getStringArray(R.array.tel_list).get(position)
                    }
                    2 -> {
                        mdntype = 3
                        vender_tv!!.text = mContext!!.resources.getStringArray(R.array.tel_list).get(position)
                    }
                    3 -> {
                        mdntype = 4
                        vender_tv!!.text = mContext!!.resources.getStringArray(R.array.tel_list).get(position)
                    }
                }
                telecomBottomSheet.dismiss()
            }
        })
        telecomBottomSheet.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                return@OnKeyListener true
                telecomBottomSheet.dismiss()
            }
            false
        })
    }

    fun startSmsRetriver() {
        val task = SmsRetriever.getClient(this)
            .startSmsRetriever()

        task.addOnSuccessListener {
        }

        task.addOnFailureListener {
        }
    }

    private fun sendCertifyNum(){
        timer_end_tv!!.visibility = View.GONE
        startSmsRetriver()

        val json = JsonObject()
        //전화번호 암호화 AES256
        var pnum: String? = AES256Chiper.AES_Encode(phonenum_tv!!.text.toString())

        json.addProperty("mdn", pnum)
        json.addProperty("tel", mdntype)
        json.addProperty("osType", "aos")
        json.addProperty("userName", pnum_name!!.text.toString())

        var call: Call<JsonObject?>? = Auth_retrofitApi!!.sendReqNumCert(json)
        call!!.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {

                if(response.isSuccessful) {
                    val respon = JSONObject(response.body().toString())
                    val resultCD = respon.opt("resultCD")
//                    {"", "성공"}
//                    {"E8997", "시간내재요청오류"}
//                    {"E8998", "통신사오류"}
//                    {"E8999", "통신사불일치"}
//                    {"E9997", "내부시스템오류"}
//                    {"E9998", "입력값오류"}
//                    {"E9999", "알수없는오류"}
                    when(resultCD) {
                        "S00001" -> {
                            runTimer()
//                            send_certify_num!!.setImageResource(R.drawable.btn_sned_result_2)
                            certNumIndex = respon.optInt("index")
//                            error_img!!.visibility = View.GONE
                            originPnum = phonenum_tv!!.text.toString()
//                            timer_end_ok!!.isVisible = true
                        }
                        "E8997" -> {
                            Toast.makeText(mContext, "너무 빠른 시간에 인증번호 재요청을 하였습니다.\n잠시후 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                        }
                        "E8998" -> {
                            Toast.makeText(mContext, "통신사 오류가 발생되었습니다.", Toast.LENGTH_LONG).show()
                        }
                        "E8999" -> {
                            timer_end_ok!!.isVisible = false
//                            error_img!!.setImageResource(R.drawable.msg_2)
//                            error_img!!.visibility = View.VISIBLE
                            Toast.makeText(mContext, "전화번호와 통신사가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                        }
                        "E9998" -> {
                            timer_end_ok!!.isVisible = false
//                            error_img!!.setImageResource(R.drawable.msg_2)
//                            error_img!!.visibility = View.VISIBLE
                            Toast.makeText(mContext, "잘못된 휴대번호 입니다", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(mContext, "잠시후 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                        }
                    }

                }else{
                    SeverErrorDialog()
                }
            }
            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                SeverErrorDialog()
            }
        })
    }

    fun runTimer() //임시로 효과를 부여하기 위해 작성함. 이벤트 적용하면 제거하시오.
    {
        m_sec = 180
        count!!.visibility = View.VISIBLE
        count?.setText("3:00")
        mCountDown?.cancel()

        mCountDown = object : CountDownTimer((m_sec * 1000).toLong(), 1000) {
            override fun onTick(millisUntillFinished: Long) {
                if (m_sec > 0) {
                    m_sec = m_sec - 1
                }
                val msg: Message = handler.obtainMessage()
                msg.what = UPDATE_MESSAGE
                msg.arg1 = m_sec / 60
                msg.arg2 = m_sec % 60
                msg.obj = null
                handler.sendMessage(msg)
            }

            override fun onFinish() {
                mCountDown?.cancel()
                count?.setText("0:00")
//                timer_end_tv!!.visibility = View.VISIBLE
                //et_verify_num.setText("");
            }
        }.start()
    }

    val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_MESSAGE -> {
                    val st: String
                    st =
                        if (msg.arg2 < 10) msg.arg1.toString() + ":0" + msg.arg2 else msg.arg1.toString() + ":" + msg.arg2
                    count!!.setText(st)
//                    BaseUtil.LogErr("st   "+st +"     "+ (st == "0:00"))
                    if(st == "0:00"){
                        BaseUtil.LogErr("종료")
                        timer_end_tv!!.visibility = View.VISIBLE
                        checkbox!!.isChecked = false
                        join_btn_txt!!.setTextColor((Color.parseColor("#bebebe")))
                        mCountDown!!.cancel()
                    }

                }
            }
        }
    }

    private fun postDataReConfirm() {

        val phoneData = PhoneNumReg(SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_JWT_TOKEN),
            pnum_name!!.text.toString().replace(" ", ""),
            PhoneNumReg.SnsInfo(
                Integer.parseInt(SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_SNSLOGIN_TYPE)),
                SharedPrefsUtils.getStringPreference(mContext!!, Const.PREF_SNSLOGIN_ACCESS_TOKEN)
            ),
            PhoneNumReg.Cert(
                certNumIndex,
                certifynum_tv!!.text.toString()
            )
        )

        val call: Call<DataResponse?>? = Auth_retrofitApi!!.PhoneNumChange(phoneData)
        call!!.enqueue(object : Callback<DataResponse?> {
            override fun onResponse(call: Call<DataResponse?>, response: Response<DataResponse?>) {

                if(response.isSuccessful) {
                    var code = response.body()!!.resultCD
//                    Toast.makeText(mContext, "코드=" + code, Toast.LENGTH_LONG).show()
//                    {"S0000", "성공"}
//                    {"E8993", "인증번호요청없음"}
//                    {"E8994", "인증번호횟수초과"}
//                    {"E8995", "인증번호시간만료"}
//                    {"E8996", "인증번호불일치"}
//                    {"E9991", "해지후가입제한"}
//                    {"E9992", "사용약관동의거부"}
//                    {"E9993", "SNS인증실패"}
//                    {"E9995", "이미가입자"}
//                    {"E9997", "내부시스템오류"}
//                    {"E9998", "입력값오류"}
//                    {"E9999", "알수없는오류"}
                    when(code){
                        "S00001" -> {
//                            postLogin()//완료 후 로그인 api 호출(jwt 토큰 획득)
//                            error_img!!.visibility = View.GONE
//                            finish()
                            val intent = Intent()
                            intent.putExtra("phone_number", phonenum_tv!!.text.toString())
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                        "E8993" -> {
                            Toast.makeText(mContext, "인증번호를 요청 후 이용해주세요.", Toast.LENGTH_LONG).show()
                        }
                        "E8994" -> {
                            Toast.makeText(mContext, "인증번호 입력횟수가 초과되었습니다.", Toast.LENGTH_LONG).show()
                        }
                        "E8995" -> {
                            Toast.makeText(mContext, "인증번호 입력시간이 초과되었습니다.", Toast.LENGTH_LONG).show()
                        }
                        "E8996" -> {
//                            error_img!!.setImageResource(R.drawable.msg_1)
//                            error_img!!.visibility = View.VISIBLE
                            Toast.makeText(mContext, "인증번호가 일지 하지 않습니다.", Toast.LENGTH_LONG).show()
                        }
                        "E9991" -> {
                            Toast.makeText(mContext, "현재 가입이 불가능합니다.(가입해지이후)", Toast.LENGTH_LONG).show()
                        }
                        "E9992" -> {
                            Toast.makeText(mContext, "사용자약관동의가 거부되었습니다.", Toast.LENGTH_LONG).show()
                        }
                        "E9993" -> {
                            Toast.makeText(mContext, "SNS인증이 실패되었습니다.", Toast.LENGTH_LONG).show()
                        }
                        "E9995" -> {
                            Toast.makeText(mContext, "이미 가입자입니다.", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(mContext, "잠시후 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                        }
                    }

                } else {
                    SeverErrorDialog()
                }
            }
            override fun onFailure(call: Call<DataResponse?>, t: Throwable) {
                SeverErrorDialog()
            }
        })
    }

    inner class MySMSReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if(SmsRetriever.SMS_RETRIEVED_ACTION == intent.action){
                val extras = intent.extras
                val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status

                when(status?.statusCode){
                    CommonStatusCodes.SUCCESS -> {
                        val message = extras?.get(SmsRetriever.EXTRA_SMS_MESSAGE) as? String

                        if(!message.isNullOrEmpty()){
                            showMessage(message)
                        }
                    }

                    CommonStatusCodes.TIMEOUT -> {
                    }
                }
            }
        }

        fun doFilter(): IntentFilter = IntentFilter().apply {
            addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        }
    }

    fun showMessage(message: String){

        val start: Int = message.lastIndexOf("[") + 1
        val end: Int = message.lastIndexOf("]")
        val sms: String = message.substring(start, end)

        certifynum_tv?.post {
            certifynum_tv?.setText(sms)
        }
    }

    fun getAppSignatures(context: Context): List<String> {
        val appCodes = mutableListOf<String>()

        try {
            val packageName = context.packageName
            val packageManager = context.packageManager
            val signatures = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
            for (signature in signatures) {
                val hash = getHash(packageName, signature.toCharsString())
                if (hash != null) {
                    appCodes.add(String.format("%s", hash))
                }
                BaseUtil.LogErr(String.format("이 값을 SMS 뒤에 써서 보내주면 됩니다 : %s", hash))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return appCodes
    }

    private fun getHash(packageName: String, signature: String): String? {
        val HASH_TYPE = "SHA-256"
        val NUM_HASHED_BYTES = 9
        val NUM_BASE64_CHAR = 11

        val appInfo = "$packageName $signature"
        try {
            val messageDigest = MessageDigest.getInstance(HASH_TYPE)
            messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
            var hashSignature = messageDigest.digest()

            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
            // encode into Base64
            var base64Hash = android.util.Base64.
            encodeToString(hashSignature, android.util.Base64.NO_PADDING or android.util.Base64.NO_WRAP)
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)

            Log.d("HASH", String.format("pkg: %s -- hash: %s", packageName, base64Hash))
            return base64Hash
        } catch (e: NoSuchAlgorithmException) {
            Log.e("Hash", "hash:NoSuchAlgorithm", e)
        }

        return null
    }
}