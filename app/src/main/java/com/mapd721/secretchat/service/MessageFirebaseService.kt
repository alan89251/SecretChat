package com.mapd721.secretchat.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.mapd721.secretchat.R
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.data_source.repository.ContactRepositoryFactory
import com.mapd721.secretchat.data_source.repository.EncryptionKeyRepositoryFactory
import com.mapd721.secretchat.encryption.EncryptionKeyPairManager
import com.mapd721.secretchat.logic.ContactManager
import com.mapd721.secretchat.logic.MessageBroadcast
import com.mapd721.secretchat.logic.MessageIOFactory
import com.mapd721.secretchat.logic.MessageReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessageFirebaseService : Service() {
    private var isInited = false
    private val contactManager: ContactManager
    private val chatFactory: ChatFactory
    private lateinit var messageIOFactory: MessageIOFactory // Should be create in onStartCommand
    val contactList: ArrayList<Contact> = ArrayList()
    val messageReceivers: MutableMap<String, MessageReceiver> = HashMap()

    init {
        contactManager = ContactManager(
            ContactRepositoryFactory(this).getLocalRepository(),
            EncryptionKeyRepositoryFactory().getRemoteRepository()
        )
        chatFactory = ChatFactory(this)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent == null) {
            return START_STICKY
        }
        if (!isInited) {
            isInited = true
            doStartService()
        }

        return START_STICKY
    }

    private fun doStartService() {
        messageIOFactory = MessageIOFactory(
            getSelfId(),
            EncryptionKeyPairManager().getKey(
                resources.getString(R.string.self_key_pair_name)
            )!!,
            chatFactory,
            MessageIOFactory.Mode.SERVICE
        )
        CoroutineScope(Dispatchers.IO).launch {
            contactList.addAll(
                contactManager.getAll()
            )

            withContext(Dispatchers.Main) {
                createMessageReceivers()
                listenMessages()
            }
        }
    }

    private fun listenMessages() {
        messageReceivers.forEach {
            it.value.setOnMessageListener(::onMessage)
            it.value.listenMessage()
        }
    }

    private fun onMessage(message: Message) {
        val intent = Intent(MessageBroadcast.INTENT_FILTER)
        intent.putExtra(MessageBroadcast.KEY_MESSAGE, message)
        sendBroadcast(intent)
    }

    private fun createMessageReceivers() {
        contactList.forEach {
            val messageReceiver = messageIOFactory.getMessageReceiver(it)
            messageReceivers[it.id] = messageReceiver
        }
    }

    private fun getSelfId(): String {
        return getSharedPreferences(
            resources.getString(R.string.preference_name),
            MODE_PRIVATE
        ).getString(
            resources.getString(R.string.self_id_preference_key),
            ""
        ).toString()
    }
}