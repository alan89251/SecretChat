package com.mapd721.secretchat.logic

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.graphics.Color
import com.mapd721.secretchat.ui.ChatFragment

class NotificationSender(
    private val context: Context,
    private val notificationManager: NotificationManager
) {
    private val channelId = "secret_chat_channel"
    private val channelName = "secret Chat Channel"
    private var notificationId = 0

    init {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    fun send(title: String, text: String) {
        val intent = Intent(context, ChatFragment::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
            or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val contentIntent = PendingIntent.getActivity(context, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(context)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setLights(Color.BLUE, 1000, 1000)
            .setChannelId(channelId)
            .build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        notificationManager.notify(notificationId, notification)
        notificationId += 1
    }
}