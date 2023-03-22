package com.mapd721.secretchat.ui.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.databinding.RecyclerViewChatBinding
import java.text.SimpleDateFormat

class MsgRecyclerViewAdapter(
    var msgList: List<Message>
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
                holder.binding.senderText.text = message.text
                holder.binding.senderMsgTime.text = dataFormat.format(message.sentDateTime)
            }
            else -> { // RECEIVE
                holder.binding.receiverDialog.visibility = View.VISIBLE
                holder.binding.receiverText.text = message.text
                holder.binding.receiverTime.text = dataFormat.format(message.sentDateTime)
            }
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }
}
