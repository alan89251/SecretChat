package com.mapd721.secretchat.service

import android.app.NotificationManager
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
import com.mapd721.secretchat.logic.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessageFirebaseService : Service() {
    private var isInited = false
    private lateinit var contactManager: ContactManager
    private lateinit var chatFactory: ChatFactory
    private lateinit var messageIOFactory: MessageIOFactory // Should be create in onStartCommand
    val contactList: ArrayList<Contact> = ArrayList()
    val messageReceivers: MutableMap<String, MessageReceiver> = HashMap()
    private lateinit var notificationSender: NotificationSender

    override fun onCreate() {
        super.onCreate()

        contactManager = ContactManager(
            ContactRepositoryFactory(this).getLocalRepository(),
            EncryptionKeyRepositoryFactory().getRemoteRepository()
        )
        chatFactory = ChatFactory(this)
        notificationSender = NotificationSender(
            this,
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        )
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
            MessageIOFactory.Mode.SERVICE,
            resources.getString(R.string.cloud_storage_root_folder_name)
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
        notificationSender.send(message.senderId, message.text)
        sendMessageBroadcast(message)
    }

    private fun sendMessageBroadcast(message: Message) {
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