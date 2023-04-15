package com.mapd721.secretchat.ui.view_holder

import android.view.View
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.databinding.RecyclerViewReceiverDialogBinding

class ReceiverDialogViewHolder(
    itemBinding: RecyclerViewReceiverDialogBinding,
    dialogBodyViewHolder: DialogBodyViewHolder
) : DialogViewHolder(itemBinding.root) {
    val binding: RecyclerViewReceiverDialogBinding = itemBinding
    private val bodyViewHolder: DialogBodyViewHolder = dialogBodyViewHolder

    override fun setMsgTime(msgTime: String) {
        binding.receiverTime.text = msgTime
    }

    override fun setBody(message: Message) {
        bodyViewHolder.setMessage(message)
    }

    override fun setOnDialogClickListener(onDialogClickListener: (View) -> Unit) {
        binding.root.setOnClickListener(onDialogClickListener)
    }
}