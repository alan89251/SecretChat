package com.mapd721.secretchat.ui.view_model

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.net.toFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.R
import com.mapd721.secretchat.broadcast.MessageBroadcastReceiver
import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.encryption.EncryptionKeyPairManager
import com.mapd721.secretchat.logic.MessageBroadcast
import com.mapd721.secretchat.logic.MessageIOFactory
import com.mapd721.secretchat.logic.MessageSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ChatViewModel(
    val chatFactory: ChatFactory,
    val selfId: String,
    val contact: Contact,
    val selfKeyPairName: String,
    val cloudStorageRootFolderName: String,
    val doRegisterBroadcastReceiver: (BroadcastReceiver, IntentFilter) -> Unit,
    val doSendSelectAttachmentIntent: (Intent, Int) -> Unit // arg1: intent, arg2: request code
): ViewModel() {
    companion object {
        const val CHAT_LIST_COL_NUM = 1
        private const val SELECT_ATTACHMENT_IMAGE = 1
    }

    val messageSender: MessageSender
    private val messageReceiver: MessageBroadcastReceiver
    val chatRepo: Chat
    var messagesLiveData: MutableLiveData<MutableList<Message>> = MutableLiveData()
    val messages: ArrayList<Message> = ArrayList()
    val messageIOFactory: MessageIOFactory
    private var attachmentPopupMenu: PopupMenu? = null
    fun setAttachmentMenu(menu: PopupMenu) {
        menu.menuInflater.inflate(R.menu.attachment_menu, menu.menu)
        menu.setOnMenuItemClickListener(::onAttachmentMenuItemClick)
        attachmentPopupMenu = menu
    }

    init {
        chatRepo = chatFactory.getLocalChat(selfId, contact.id)
        messageIOFactory = MessageIOFactory(
            selfId,
            EncryptionKeyPairManager().getKey(selfKeyPairName)!!,
            chatFactory,
            MessageIOFactory.Mode.UI,
            cloudStorageRootFolderName
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

    fun addAttachment(v: View) {
        attachmentPopupMenu?.show()
    }

    private fun onAttachmentMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_photo -> selectPhotoToSend()
            R.id.action_video -> {}
            R.id.action_location -> {}
        }

        return true
    }

    private fun selectPhotoToSend() {
        val intent = Intent()
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setAction(Intent.ACTION_GET_CONTENT)
        doSendSelectAttachmentIntent(intent, SELECT_ATTACHMENT_IMAGE)
    }

    fun onIntentFinished(requestCode: Int, data: Intent) {
        when (requestCode) {
            SELECT_ATTACHMENT_IMAGE -> onSelectedAttachmentImage(data)
        }
    }

    private fun onSelectedAttachmentImage(data: Intent) {
        val imageUriList = ArrayList<Uri>()
        if (data.clipData != null) {
            val clipData = data.clipData!!
            val itemCount = clipData.itemCount
            for (i in 0 until itemCount) {
                val imageUri = clipData.getItemAt(i).uri
                imageUriList.add(imageUri)
            }
        }
        else {
            val imageUri = data.data!!
            imageUriList.add(imageUri)
        }
        // support sending single image only
        sendImage(imageUriList[0])
    }

    private fun sendImage(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            val file = File(uri.path!!)
            var bytes: ByteArray
            file.inputStream().use {
                bytes = it.readBytes()
            }
            val message = messageSender.sendImage(file.name, bytes)

            withContext(Dispatchers.Main) {
                messages.add(message)
                messagesLiveData.value = messages
            }
        }
    }
}