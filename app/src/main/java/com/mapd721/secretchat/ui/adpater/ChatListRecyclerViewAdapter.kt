package com.mapd721.secretchat.ui.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mapd721.secretchat.R
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.databinding.RecyclerViewChatListBinding
import com.mapd721.secretchat.logic.CloudImageDownloader
import java.text.SimpleDateFormat

class ChatListRecyclerViewAdapter(
    var contactList: List<Contact>,
    private val profilePictureFolderPath: String,
    private val cloudImageDownloader: CloudImageDownloader,
    private val onItemClick: (View, Int, Contact) -> Unit,
    private val onItemBind: (View, Int, Contact, OnMessageUpdateListener) -> Unit
    ): RecyclerView.Adapter<ChatListRecyclerViewAdapter.ViewHolder>() {
        class ViewHolder(
            itemBinding: RecyclerViewChatListBinding,
            onItemClick: (View, Int) -> Unit
        ): RecyclerView.ViewHolder(itemBinding.root) {
            val binding: RecyclerViewChatListBinding = itemBinding

            init {
                binding.root.setOnClickListener {
                    onItemClick(it, this@ViewHolder.layoutPosition)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerViewChatListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_view_chat_list,
            parent,
            false
        )
        return ViewHolder(binding) { view, position ->
            onItemClick(
                view,
                position,
                contactList[position]
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]
        holder.binding.name.text = contact.name
        val dateFormat = SimpleDateFormat("EEE")
        cloudImageDownloader.loadInto(
            "$profilePictureFolderPath/${contact.name}.jpg",
            holder.binding.profileImage
        )
        onItemBind(
            holder.binding.root,
            position, contact,
            object : OnMessageUpdateListener {
                override fun onMessageUpdate(message: Message) {
                    holder.binding.latestMessageText.text = when (message.mime) {
                        Message.Mime.TEXT -> message.text
                        Message.Mime.IMAGE -> "Received image"
                        Message.Mime.VIDEO -> "Received video"
                        Message.Mime.LOCATION -> message.text
                    }
                    holder.binding.msgTime.text = dateFormat.format(message.sentDateTime)
                }
            }
        )
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    interface OnMessageUpdateListener {
        fun onMessageUpdate(message: Message)
    }
}