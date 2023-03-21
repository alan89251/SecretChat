package com.mapd721.secretchat.data_source.firestore.message

import com.google.firebase.firestore.Exclude
import com.mapd721.secretchat.data_model.chat.Message
import java.util.*

class MessageFirestore(
    @get: Exclude
    var id: String = "",
    var text: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var sentDateTime: Long = Date().time
) {
    constructor(message: Message) : this() {
        id = message.id
        text = message.text
        senderId = message.senderId
        receiverId = message.receiverId
        sentDateTime = message.sentDateTime.time
    }

    fun toMessage(): Message {
        val message = Message()
        message.id = id
        message.text = text
        message.senderId = senderId
        message.receiverId = receiverId
        message.sentDateTime = Date(sentDateTime)
        return message
    }
}