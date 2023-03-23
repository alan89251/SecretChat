package com.mapd721.secretchat.ui.view_model

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.broadcast.MessageBroadcastReceiver
import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.encryption.EncryptionKeyPairManager
import com.mapd721.secretchat.logic.MessageBroadcast
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
    val selfKeyPairName: String,
    val doRegisterBroadcastReceiver: (BroadcastReceiver, IntentFilter) -> Unit
): ViewModel() {
    companion object {
        const val CHAT_LIST_COL_NUM = 1
    }

    val messageSender: MessageSender
    private val messageReceiver: MessageBroadcastReceiver
    val chatRepo: Chat
    var messagesLiveData: MutableLiveData<MutableList<Message>> = MutableLiveData()
    val messages: ArrayList<Message> = ArrayList()
    val messageIOFactory: MessageIOFactory

    init {
        chatRepo = chatFactory.getLocalChat(selfId, contact.id)
        messageIOFactory = MessageIOFactory(
            selfId,
            EncryptionKeyPairManager().getKey(selfKeyPairName)!!,
            chatFactory,
            MessageIOFactory.Mode.UI
        )
        messageSender = messageIOFactory.getMessageSender(contact)
        messageReceiver = MessageBroadcastReceiver()
        doRegisterBroadcastReceiver(messageReceiver, IntentFilter(MessageBroadcast.INTENT_FILTER))
    }

    fun loadAllMessagesFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            val sentMessages = chatRepo.getAllSentMessages()
            val receivedMessage = chatRepo.getAllReceivedMessages()
            messages.addAll(sentMessages)
            messages.addAll(receivedMessage)
            messages.sortBy { it.sentDateTime }

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
    }
}