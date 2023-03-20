package com.mapd721.secretchat.data_model.chat

import com.google.firebase.firestore.Exclude
import java.util.Date

class Message {
    @get:Exclude
    var id: String = ""
    var text: String = ""
    var senderId: String = ""
    var receiverId: String = ""
    var sentDateTime: Date = Date()
}