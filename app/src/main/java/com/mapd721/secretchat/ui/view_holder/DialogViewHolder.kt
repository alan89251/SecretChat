package com.mapd721.secretchat.ui.view_holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mapd721.secretchat.data_model.chat.Message
import java.text.SimpleDateFormat

abstract class DialogViewHolder(itemView: View)
    : RecyclerView.ViewHolder(itemView)
{
    abstract fun setMsgTime(msgTime: String)
    abstract fun setBody(message: Message)
    abstract fun setOnDialogClickListener(onDialogClickListener: (View) -> Unit)
    fun setMessage(message: Message) {
        setBody(message)
        val dataFormat = SimpleDateFormat("hh:mm")
        setMsgTime(dataFormat.format(message.sentDateTime))
    }
}