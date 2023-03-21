package com.mapd721.secretchat.ui.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.logic.MessageSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel: ViewModel() {
    companion object {
        const val CHAT_LIST_COL_NUM = 1
    }

    lateinit var contact: Contact
    lateinit var messageSender: MessageSender
    lateinit var chatRepo: Chat
    var messages: MutableLiveData<List<Message>> = MutableLiveData()

    fun loadAllMessagesFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            val temp = chatRepo.getAllMessages()

            withContext(Dispatchers.Main) {
                messages.value = temp
            }
        }
    }
}