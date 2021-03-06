package com.audiospotapplication.data.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.audiospotapplication.data.cache.cacheDataSourceUsingSharedPreferences
import com.audiospotapplication.data.retrofit.GlobalKeys
import com.audiospotapplication.R
import com.audiospotapplication.ui.homepage.HomepageActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        remoteMessage.let {
            Log.d(TAG, "From: ${remoteMessage.from}")

            // Check if message contains a data payload.
            remoteMessage.data.isNotEmpty().let {
                Log.d(TAG, "Message data payload: " + remoteMessage.data)
            }

            // Check if message contains a notification payload.
            remoteMessage.notification?.let {
                Log.d(TAG, "Message Notification Body: ${it.body}")
                it.body?.let { it1 -> sendNotification(it1) }
            }
        }

    }

    override fun onNewToken(p0: String) {
        handleTokenChange(p0)
    }


    private fun handleTokenChange(token: String) {
        val cacheDataSource = cacheDataSourceUsingSharedPreferences.getINSTANCE(applicationContext)
        cacheDataSource.setStringIntoCache(GlobalKeys.StoreData.TOKEN, token)
    }

    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, HomepageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}