package com.mapd721.secretchat.ui.view_model

import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.logic.MessageSender

class ChatViewModel: ViewModel() {
    lateinit var contact: Contact
    lateinit var messageSender: MessageSender
}