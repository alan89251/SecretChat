package com.mapd721.secretchat.ui.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.encryption.EncryptionKeyPairManager
import com.mapd721.secretchat.logic.MessageIOFactory
import com.mapd721.secretchat.logic.MessageReceiver
import com.mapd721.secretchat.logic.MessageSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    val chatFactory: ChatFactory,
    val selfId: String,
    val contact: Contact,
    val selfKeyPairName: String
): ViewModel() {
    companion object {
        const val CHAT_LIST_COL_NUM = 1
    }

    val messageSender: MessageSender
    val messageReceiver: MessageReceiver
    val chatRepo: Chat
    var messagesLiveData: MutableLiveData<MutableList<Message>> = MutableLiveData()
    lateinit var messages: ArrayList<Message>
    val messageIOFactory: MessageIOFactory

    init {
        chatRepo = chatFactory.getLocalChat(selfId, contact.id)
        messageIOFactory = MessageIOFactory(
            selfId,
            EncryptionKeyPairManager().getKey(selfKeyPairName)!!,
            chatFactory
        )
        messageSender = messageIOFactory.getMessageSender(contact)
        messageReceiver = messageIOFactory.getMessageReceiver(contact)
    }

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