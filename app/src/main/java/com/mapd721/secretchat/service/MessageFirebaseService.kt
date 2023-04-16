package com.mapd721.secretchat.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.mapd721.secretchat.R
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.*
import com.mapd721.secretchat.encryption.EncryptionKeyPairManager
import com.mapd721.secretchat.logic.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MessageFirebaseService : Service() {
    private var isInited = false
    private lateinit var contactManager: ContactManager
    private lateinit var chatFactory: ChatFactory
    private lateinit var messageIOFactory: MessageIOFactory // Should be create in onStartCommand
    val contactList: ArrayList<Contact> = ArrayList()
    val messageReceivers: MutableMap<String, MessageReceiver> = HashMap()
    private lateinit var notificationSender: NotificationSender
    private lateinit var fileRepository: FileRepository

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
        fileRepository = FileRepositoryFactory.getFireStore(
            resources.getString(R.string.cloud_storage_root_folder_name)
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
        else {
            runCommand(intent)
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
        CoroutineScope(Dispatchers.IO).launch {
            when (message.mime) {
                Message.Mime.IMAGE,
                Message.Mime.VIDEO -> downloadFileFromCloud(message)
                else -> {}
            }

            withContext(Dispatchers.Main) {
                sendNotification(message)
                sendMessageBroadcast(message)
            }
        }
    }

    private fun downloadFileFromCloud(message: Message) {
        val bytes = fileRepository.getSync(message.uploadedFilePath)
        val file = File(
            filesDir.path + "/" + resources.getString(R.string.media_storage_root),
            message.oriFileName
        )
        file.createNewFile()
        file.outputStream().use {
            it.write(bytes)
        }
    }

    private fun sendNotification(message: Message) {
        val contentText = when (message.mime) {
            Message.Mime.TEXT -> message.text
            Message.Mime.IMAGE -> "Received image"
            Message.Mime.VIDEO -> "Received video"
            Message.Mime.LOCATION -> message.text
        }
        notificationSender.send(message.senderId, contentText)
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

    private fun runCommand(intent: Intent) {
        val cmd = intent.getStringExtra(MessageServiceCmd.KEY_CMD) ?: return
        when (cmd) {
            MessageServiceCmd.CMD_ADD_CONTACT -> {
                val contactId = intent.getStringExtra(MessageServiceCmd.ARG_CONTACT)!!
                CoroutineScope(Dispatchers.IO).launch {
                    val contact = contactManager.getById(contactId)!!

                    withContext(Dispatchers.Main) {
                        listenToContact(contact)
                    }
                }
            }
            else -> {}
        }
    }

    private fun listenToContact(contact: Contact) {
        contactList.add(contact)
        val messageReceiver = messageIOFactory.getMessageReceiver(contact)
        messageReceiver.setOnMessageListener(::onMessage)
        messageReceivers[contact.id] = messageReceiver
        messageReceiver.listenMessage()
    }
}