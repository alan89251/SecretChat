package com.mapd721.secretchat.data_model.chat

import java.util.Date

class Message {
    var id: String = ""
    var text: String = ""
    var senderId: String = ""
    var receiverId: String = ""
    var sentDateTime: Date = Date()
}