package com.infovine.tour

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.infovine.tour.utils.TourDB
import java.util.*

class mApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =  this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Objects.requireNonNull(notificationManager).getNotificationChannel("infovineTour") == null) {

                val channel = NotificationChannel("infovineTour", "알림", NotificationManager.IMPORTANCE_HIGH)
                // 알림 채널설정
                channel.setShowBadge(true)
                channel.enableVibration(false)
                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE)
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}