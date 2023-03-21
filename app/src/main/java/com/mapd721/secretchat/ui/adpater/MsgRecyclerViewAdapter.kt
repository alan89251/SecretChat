package com.mapd721.secretchat.ui.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mapd721.secretchat.R
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.databinding.RecyclerViewReceiverBinding
import com.mapd721.secretchat.databinding.RecyclerViewSenderBinding

class MsgRecyclerViewAdapter(
    var msgList: List<Message>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class SenderViewHolder(itemBinding: RecyclerViewSenderBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
            val binding: RecyclerViewSenderBinding = itemBinding
        }

    class ReceiverViewHolder(itemBinding: RecyclerViewReceiverBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
            val binding: RecyclerViewReceiverBinding = itemBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENDER -> {
                val binding: RecyclerViewSenderBinding = RecyclerViewSenderBinding.inflate(
                    LayoutInflater.from(parent.context))
                SenderViewHolder(binding)
            }
            else -> { // Receiver
                val binding: RecyclerViewReceiverBinding = RecyclerViewReceiverBinding.inflate(
                    LayoutInflater.from(parent.context))
                ReceiverViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = msgList[position]
        when (holder.itemViewType) {
            VIEW_TYPE_SENDER -> {
                val concreteHolder = holder as SenderViewHolder
                concreteHolder.binding.contentText.text = message.text
            }
            VIEW_TYPE_RECEIVER -> {
                val concreteHolder = holder as ReceiverViewHolder
                concreteHolder.binding.contentText.text = message.text
            }
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = msgList[position]
        return when(message.type) {
            Message.TYPE_SNED -> VIEW_TYPE_SENDER
            else -> VIEW_TYPE_RECEIVER
        }
    }

    companion object {
        const val VIEW_TYPE_SENDER = 0
        const val VIEW_TYPE_RECEIVER = 1
    }
}
