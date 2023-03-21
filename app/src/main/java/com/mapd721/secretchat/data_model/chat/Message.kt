package com.mapd721.secretchat.data_model.chat

import java.util.Date

class Message {
    var id: String = ""
    var text: String = ""
    var senderId: String = ""
    var receiverId: String = ""
    var type: Int = TYPE_SNED
    var sentDateTime: Date = Date()

    companion object {
        const val TYPE_SNED = 0
        const val TYPE_RECEIVE = 1
    }
}