package com.infovine.tour.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.infovine.tour.R
import com.infovine.tour.activity.MainActivity
import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import com.infovine.tour.utils.SharedPrefsUtils
import java.util.concurrent.atomic.AtomicInteger


class FirebaseService : FirebaseMessagingService() {

    private val TAG = "#FirebaseMsgService"
    var msgId: AtomicInteger = AtomicInteger()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        BaseUtil.LogErr("onNewToken", ">>"+token)
        SharedPrefsUtils.setStringPreference(applicationContext!!, Const.PREF_PUSH_ID, token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        BaseUtil.LogErr("message.data   "+message.data)

        if (message.data.size > 0) {
            title = message.data["title"].toString()
            body = message.data["body"].toString()
            mutable_content = message.data["mutable_content"].toString()
            sound = message.data["sound"].toString()
            pMessage = message.data["message"].toString()
            msg_type = message.data["msg_type"].toString()
            post_cd = message.data["post_cd"].toString()
            img_url = message.data["img_url"].toString()
            url_link = message.data["url_link"].toString()
            app_cd = message.data["app_cd"].toString()
            url = message.data["url"].toString()
            url_gb = message.data["url_gb"].toString()
            push_type = message.data["push_type"].toString()
            categoryCD = message.data["cate_cd"].toString()
            sendNotification()
        }
    }
    var title = ""//
    var body = ""//
    var mutable_content = ""//default true
    var sound = ""//default
    var pMessage = ""//
    var msg_type = ""// 1: msg only, 2: msg+img
    var post_cd = ""//
    var img_url = ""//
    var url_link = ""
    var app_cd = ""//
    var url = ""//
    var url_gb = ""//1: 앱노션페이지, 2: 외부URL
    var push_type = ""//1:앱실행여부알림, 2:퀴즈:마이픽앱알림, 3:클릭적립, 4: 신규리워드앱, 5: 신규꿀팁, 6: 신규이벤트소식
    var categoryCD = ""

    val SUMMARY_ID = 99999999
    val GROUP_KEY_WORK_EMAIL = "everymoney_push"


    fun sendNotification() {

        var intent = Intent(this, MainActivity::class.java)
        intent.putExtra("push_type", push_type)
        intent.putExtra("appcode", app_cd)
        intent.putExtra("cpost_CD", post_cd)
        intent.putExtra("notionURL", url)
        intent.putExtra("url_gb", url_gb)
        intent.putExtra("categoryCD", categoryCD)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var pushCnt = System.currentTimeMillis().toInt()
        SharedPrefsUtils.setIntegerPreference(this, Const.PUSH_MESSEAGE_CNT, pushCnt)

        val pendingIntent: PendingIntent

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, pushCnt, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        } else {
            pendingIntent = PendingIntent.getActivity(this, pushCnt, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, "에브리머니")
        val summarynotificationBuilder = NotificationCompat.Builder(this, "에브리머니")

        notificationBuilder.setContentTitle(title)
//            .setSmallIcon(R.drawable.ic_stat_notification_icon_w)
            .setSmallIcon(R.drawable.logo_apple)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setGroup(GROUP_KEY_WORK_EMAIL)

        summarynotificationBuilder
            .setSmallIcon(R.drawable.logo_apple)
            .setGroup(GROUP_KEY_WORK_EMAIL)
            .setGroupSummary(true)

        if(!TextUtils.isEmpty(img_url)) {
            Glide.with(applicationContext).asBitmap().load(img_url)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        notificationBuilder.setLargeIcon(resource)
                        notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                        notificationManager.notify(pushCnt, notificationBuilder.build() )
                        notificationManager.notify(SUMMARY_ID, summarynotificationBuilder.build())
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        if(TextUtils.isEmpty(img_url)) {
            notificationManager.notify(pushCnt, notificationBuilder.build() )
            notificationManager.notify(SUMMARY_ID, summarynotificationBuilder.build())
        }
    }
}