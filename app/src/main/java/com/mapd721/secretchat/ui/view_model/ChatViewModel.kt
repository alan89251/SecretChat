package com.mapd721.secretchat.ui.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.logic.MessageReceiver
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
    lateinit var messageReceiver: MessageReceiver
    lateinit var chatRepo: Chat
    var messagesLiveData: MutableLiveData<MutableList<Message>> = MutableLiveData()
    lateinit var messages: ArrayList<Message>

    fun loadAllMessagesFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            messages = ArrayList(chatRepo.getAllMessages())

            withContext(Dispatchers.Main) {
                messagesLiveData.value = messages
            }
        }
    }

    fun sendMessage(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = messageSender.send(text)

            withContext(Dispatchers.Main) {
                messages.add(message)
                messagesLiveData.value = messages
            }
        }
    }

    fun listenMessage() {
        messageReceiver.setOnMessageListener {
            messages.add(it)
            messagesLiveData.value = messages
        }
        messageReceiver.listenMessage()
    }
}