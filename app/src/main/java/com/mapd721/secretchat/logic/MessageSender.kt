package com.mapd721.secretchat.logic

import com.mapd721.secretchat.data_model.chat.Message

interface MessageSender {
    fun send(text: String): Message
    fun sendFile(name: String, bytes: ByteArray, mime: Message.Mime): Message
    fun sendLocation(latitude: Double, longitude: Double): Message
}