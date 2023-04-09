package com.mapd721.secretchat.ui.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.databinding.RecyclerViewChatBinding
import java.text.SimpleDateFormat

class MsgRecyclerViewAdapter(
    var msgList: List<Message>,
    val onItemClick: (Message) -> Unit
): RecyclerView.Adapter<MsgRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemBinding: RecyclerViewChatBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        val binding: RecyclerViewChatBinding = itemBinding
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerViewChatBinding = RecyclerViewChatBinding.inflate(
            LayoutInflater.from(parent.context)
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // init layout
        holder.binding.senderDialog.visibility = View.GONE
        holder.binding.receiverDialog.visibility = View.GONE

        val message = msgList[position]
        val dataFormat = SimpleDateFormat("hh:mm")
        when (message.type) {
            Message.TYPE_SNED -> {
                holder.binding.senderDialog.visibility = View.VISIBLE
                holder.binding.senderText.text = getDisplayText(message)
                holder.binding.senderMsgTime.text = dataFormat.format(message.sentDateTime)
                holder.binding.root.setOnClickListener { onItemClick(message) }
            }
            else -> { // RECEIVE
                holder.binding.receiverDialog.visibility = View.VISIBLE
                holder.binding.receiverText.text = getDisplayText(message)
                holder.binding.receiverTime.text = dataFormat.format(message.sentDateTime)
                holder.binding.root.setOnClickListener { onItemClick(message) }
            }
        }
    }

    private fun getDisplayText(message: Message): String {
        return when (message.mime) {
            Message.Mime.TEXT -> message.text
            Message.Mime.IMAGE -> message.oriFileName
            Message.Mime.VIDEO -> message.oriFileName
            Message.Mime.LOCATION -> message.text
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }
}
