package com.mapd721.secretchat.ui.view_model

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.broadcast.MessageBroadcastReceiver
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.encryption.EncryptionKeyPairManager
import com.mapd721.secretchat.logic.ContactManager
import com.mapd721.secretchat.logic.MessageBroadcast
import com.mapd721.secretchat.logic.MessageIOFactory
import com.mapd721.secretchat.logic.MessageReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListViewModel(
    val contactManager: ContactManager,
    val chatFactory: ChatFactory,
    val selfId: String,
    val selfKeyPairName: String,
    val doRegisterBroadcastReceiver: (BroadcastReceiver, IntentFilter) -> Unit
): ViewModel() {
    companion object {
        const val CHAT_LIST_COL_NUM = 1
    }

    val contactListLiveData: MutableLiveData<MutableList<Contact>> = MutableLiveData()
    val contactList: ArrayList<Contact> = ArrayList()
    val messageListeners: MutableMap<String, (Message) -> Unit> = HashMap()
    val messageIOFactory: MessageIOFactory
    val messageReceiver: MessageBroadcastReceiver

    init {
        messageIOFactory = MessageIOFactory(
            selfId,
            EncryptionKeyPairManager().getKey(selfKeyPairName)!!,
            chatFactory,
            MessageIOFactory.Mode.UI
        )
        messageReceiver = MessageBroadcastReceiver(::dispatchMessage)
        doRegisterBroadcastReceiver(messageReceiver, IntentFilter(MessageBroadcast.INTENT_FILTER))

        CoroutineScope(Dispatchers.IO).launch {
            val list = contactManager.getAll()

            withContext(Dispatchers.Main) {
                contactList.addAll(list)
                contactListLiveData.value = contactList
            }
        }
    }

    fun initContactItem(contact: Contact, onMessage: (Message) -> Unit) {
        getLatestMessage(contact) {
            if (it != null) {
                onMessage(it)
            }
            listenMessage(contact, onMessage)
        }
    }

    private fun getLatestMessage(contact: Contact, onMessage: (Message?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val chat = chatFactory.getLocalChat(selfId, contact.id)
            val message = chat.getLatestMessage()

            withContext(Dispatchers.Main) {
                onMessage(message)
            }
        }
    }

    private fun listenMessage(contact: Contact, onMessage: (Message) -> Unit) {
        messageListeners[contact.id] = onMessage
    }

    private fun dispatchMessage(message: Message) {
        val contactId = if (message.senderId == selfId) message.receiverId else message.senderId
        messageListeners[contactId]?.invoke(message)
    }
}