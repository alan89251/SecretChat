package com.mapd721.secretchat.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.logic.MessageBroadcast

class MessageBroadcastReceiver(
    private val onReceive: (Message) -> Unit
): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getSerializableExtra(MessageBroadcast.KEY_MESSAGE, Message::class.java)
        message?.let {
            onReceive(it)
        }
    }
}