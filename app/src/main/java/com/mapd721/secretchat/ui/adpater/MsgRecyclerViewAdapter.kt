package com.mapd721.secretchat.ui.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mapd721.secretchat.R
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.databinding.MessageDialogBodyImageBinding
import com.mapd721.secretchat.databinding.MessageDialogBodyTextBinding
import com.mapd721.secretchat.databinding.MessageDialogBodyVideoBinding
import com.mapd721.secretchat.databinding.RecyclerViewReceiverDialogBinding
import com.mapd721.secretchat.databinding.RecyclerViewSenderDialogBinding
import com.mapd721.secretchat.ui.view_holder.*

class MsgRecyclerViewAdapter(
    var msgList: List<Message>,
    val mediaFolderPath: String,
    val onItemClick: (Message) -> Unit
): RecyclerView.Adapter<DialogViewHolder>() {
    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return when (msg.type) {
            Message.TYPE_SNED -> getItemViewTypeOfSender(msg.mime)
            else -> getItemViewTypeOfReceiver(msg.mime)
        }
    }

    private fun getItemViewTypeOfSender(mime: Message.Mime): Int {
        return when (mime) {
            Message.Mime.TEXT -> SENDER_TEXT
            Message.Mime.IMAGE -> SENDER_IMAGE
            Message.Mime.VIDEO -> SENDER_VIDEO
            Message.Mime.LOCATION -> SENDER_LOCATION
        }
    }

    private fun getItemViewTypeOfReceiver(mime: Message.Mime): Int {
        return when (mime) {
            Message.Mime.TEXT -> RECEIVER_TEXT
            Message.Mime.IMAGE -> RECEIVER_IMAGE
            Message.Mime.VIDEO -> RECEIVER_VIDEO
            Message.Mime.LOCATION -> RECEIVER_LOCATION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
        return when (viewType) {
            SENDER_TEXT,
            SENDER_IMAGE,
            SENDER_VIDEO,
            SENDER_LOCATION -> createSenderDialogViewHolder(parent, viewType)
            RECEIVER_TEXT,
            RECEIVER_IMAGE,
            RECEIVER_VIDEO,
            RECEIVER_LOCATION -> createReceiverDialogViewHolder(parent, viewType)
            else -> throw Exception("Invalid message type")
        }
    }

    private fun createSenderDialogViewHolder(parent: ViewGroup, viewType: Int): SenderDialogViewHolder {
        val binding = RecyclerViewSenderDialogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return when (viewType) {
            SENDER_TEXT -> {
                val bodyBinding = MessageDialogBodyTextBinding.inflate(LayoutInflater.from(parent.context))
                binding.body.addView(bodyBinding.root)
                SenderDialogViewHolder(
                    binding,
                    TextDialogBodyViewHolder(bodyBinding)
                )
            }
            SENDER_IMAGE -> {
                val bodyBinding = MessageDialogBodyImageBinding.inflate(LayoutInflater.from(parent.context))
                binding.body.addView(bodyBinding.root)
                SenderDialogViewHolder(
                    binding,
                    ImageDialogBodyViewHolder(
                        bodyBinding,
                        mediaFolderPath
                    )
                )
            }
            SENDER_VIDEO -> {
                val bodyBinding = MessageDialogBodyVideoBinding.inflate(LayoutInflater.from(parent.context))
                binding.body.addView(bodyBinding.root)
                SenderDialogViewHolder(
                    binding,
                    VideoDialogBodyViewHolder(
                        bodyBinding,
                        mediaFolderPath
                    )
                )
            }
            SENDER_LOCATION -> {
                SenderDialogViewHolder(
                    binding,
                    LocationDialogBodyViewHolder()
                )
            }
            else -> throw Exception("invalid message type")
        }
    }

    private fun createReceiverDialogViewHolder(parent: ViewGroup, viewType: Int): ReceiverDialogViewHolder {
        val binding = RecyclerViewReceiverDialogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return when (viewType) {
            RECEIVER_TEXT -> {
                val bodyBinding = MessageDialogBodyTextBinding.inflate(LayoutInflater.from(parent.context))
                binding.body.addView(bodyBinding.root)
                ReceiverDialogViewHolder(
                    binding,
                    TextDialogBodyViewHolder(bodyBinding)
                )
            }
            RECEIVER_IMAGE -> {
                val bodyBinding = MessageDialogBodyImageBinding.inflate(LayoutInflater.from(parent.context))
                binding.body.addView(bodyBinding.root)
                ReceiverDialogViewHolder(
                    binding,
                    ImageDialogBodyViewHolder(
                        bodyBinding,
                        mediaFolderPath
                    )
                )
            }
            RECEIVER_VIDEO -> {
                val bodyBinding = MessageDialogBodyVideoBinding.inflate(LayoutInflater.from(parent.context))
                binding.body.addView(bodyBinding.root)
                ReceiverDialogViewHolder(
                    binding,
                    VideoDialogBodyViewHolder(
                        bodyBinding,
                        mediaFolderPath
                    )
                )
            }
            RECEIVER_LOCATION -> {
                ReceiverDialogViewHolder(
                    binding,
                    LocationDialogBodyViewHolder()
                )
            }
            else -> throw Exception("invalid message type")
        }
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        val message = msgList[position]
        holder.setMessage(message)
    }

    companion object {
        const val SENDER_TEXT = 0
        const val SENDER_IMAGE = 1
        const val SENDER_VIDEO = 2
        const val SENDER_LOCATION = 3
        const val RECEIVER_TEXT = 4
        const val RECEIVER_IMAGE = 5
        const val RECEIVER_VIDEO = 6
        const val RECEIVER_LOCATION = 7
    }
}
