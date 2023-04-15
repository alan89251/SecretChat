package com.mapd721.secretchat.ui.view_holder

import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.databinding.RecyclerViewReceiverDialogBinding

class ReceiverDialogViewHolder(
    itemBinding: RecyclerViewReceiverDialogBinding,
    dialogBodyViewHolder: DialogBodyViewHolder
) : DialogViewHolder(itemBinding.root) {
    val binding: RecyclerViewReceiverDialogBinding = itemBinding
    val bodyViewHolder: DialogBodyViewHolder = dialogBodyViewHolder

    override fun setMsgTime(msgTime: String) {
        binding.receiverTime.text = msgTime
    }

    override fun setBody(message: Message) {
        bodyViewHolder.setMessage(message)
    }
}