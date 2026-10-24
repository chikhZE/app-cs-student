package com.zineeddine.cs_student

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CSstudentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        FirebaseMessaging.getInstance().subscribeToTopic("announcements")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to announcements topic successfully")
                } else {
                    Log.e("FCM", "Failed to subscribe to announcements topic", task.exception)
                }
            }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "updates_channel_id"
            val channelName = "CS Student Updates"
            val channelDescription = "Notifications for new announcements and updates"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
                enableVibration(true)
                enableLights(true)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}
