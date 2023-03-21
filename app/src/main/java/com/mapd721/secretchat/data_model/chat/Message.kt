package com.mapd721.secretchat.data_model.chat

import com.google.firebase.firestore.Exclude
import java.util.Date

class Message {
    @get:Exclude
    var id: String = ""
    var text: String = ""
    var senderId: String = ""
    var receiverId: String = ""
    @get:Exclude
    var type: Int = TYPE_SNED
    var sentDateTime: Date = Date()

    companion object {
        const val TYPE_SNED = 0
        const val TYPE_RECEIVE = 1
    }
}