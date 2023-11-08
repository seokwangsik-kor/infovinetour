package com.infovine.tour.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.infovine.tour.R
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils

class PermissionActivity : BaseActivity() {

    lateinit var permission: Array<String>
    var per_ok: FrameLayout? = null
    val PER_DISK = 1000
    val PER_PHONE = 2000
    val PER_GPS = 3000
    val PER_CAMERA = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        per_ok = findViewById(R.id.permisson_confirm_btn)
        per_ok!!.setOnClickListener {
            if(Build.VERSION.SDK_INT < 29) {
                if (!BaseUtil.permission(this)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PER_DISK)
                    }
                } else {
                    finish()
                }
            } else {
                PhoneNemPer()
            }
        }
//        SharedPrefsUtils.setBooleanPreference(mContext!!, Const.APP_IS_FIRST,false)
    }

    fun PhoneNemPer() {
        permission = if (Build.VERSION.SDK_INT > 29) {
            arrayOf(Manifest.permission.READ_PHONE_NUMBERS)
        } else {
            arrayOf(Manifest.permission.READ_PHONE_STATE)
        }

        if (ActivityCompat.checkSelfPermission(mContext!!, permission.get(0)) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, PER_PHONE)
            }
        }
    }

    fun GpsPer() {
        permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, PER_GPS)
        }
    }

    fun CameraPer() {
        permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, PER_CAMERA)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PER_DISK) {
            PhoneNemPer()
            return
        }

        if (requestCode == PER_PHONE) {
            GpsPer()
            return
        }

        if(requestCode == PER_GPS) {
            CameraPer()
            return
        }

        if(requestCode == PER_CAMERA) {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        setResult(RESULT_OK)
        SharedPrefsUtils.setBooleanPreference(mContext!!, Const.APP_IS_FIRST,false)
    }

    override fun onBackPressed() {
        return
    }

}