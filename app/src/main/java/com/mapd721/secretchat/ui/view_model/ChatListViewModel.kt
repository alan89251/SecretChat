package com.mapd721.secretchat.ui.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.encryption.EncryptionKeyPairManager
import com.mapd721.secretchat.logic.ContactManager
import com.mapd721.secretchat.logic.MessageIOFactory
import com.mapd721.secretchat.logic.MessageReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListViewModel(
    contactManager: ContactManager,
    val chatFactory: ChatFactory,
    val selfId: String,
    val selfKeyPairName: String
): ViewModel() {
    companion object {
        const val CHAT_LIST_COL_NUM = 1
    }

    val contactListLiveData: MutableLiveData<MutableList<Contact>> = MutableLiveData()
    val contactList: ArrayList<Contact> = ArrayList()
    val messageReceivers: MutableMap<String, MessageReceiver> = HashMap()
    val messageIOFactory: MessageIOFactory

    init {
        messageIOFactory = MessageIOFactory(
            selfId,
            EncryptionKeyPairManager().getKey(selfKeyPairName)!!,
            chatFactory
        )

        CoroutineScope(Dispatchers.IO).launch {
            val list = contactManager.getAll()

            withContext(Dispatchers.Main) {
                contactList.addAll(list)
                createMessageReceivers()
                contactListLiveData.value = contactList
            }
        }
    }

    private fun createMessageReceivers() {
        contactList.forEach {
            val messageReceiver = messageIOFactory.getMessageReceiver(it)
            messageReceivers[it.id] = messageReceiver
        }
    }

    fun listenMessage(contact: Contact, onMessage: (Message) -> Unit ) {
        val messageReceiver = messageReceivers[contact.id]!!
        messageReceiver.setOnMessageListener {
            onMessage(it)
        }
        messageReceiver.listenMessage()
    }
}