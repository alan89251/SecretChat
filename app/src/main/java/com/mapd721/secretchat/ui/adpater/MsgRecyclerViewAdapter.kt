package com.mapd721.secretchat.ui.adpater

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.databinding.RecyclerViewChatBinding
import java.io.File
import java.text.SimpleDateFormat

class MsgRecyclerViewAdapter(
    var msgList: List<Message>,
    val mediaFolderPath: String,
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
                configSenderMessageLayout(holder, message)
                holder.binding.senderDialog.visibility = View.VISIBLE
                when (message.mime) {
                    Message.Mime.TEXT -> holder.binding.senderText.text = getDisplayText(message)
                    Message.Mime.IMAGE -> setImage(holder.binding.senderImage, message.oriFileName)
                    Message.Mime.VIDEO -> {
                        setVideo(holder.binding.senderVideo, message.oriFileName)
                        holder.binding.root.setOnClickListener { onItemClick(message) }
                    }
                    Message.Mime.LOCATION -> {}
                }
                holder.binding.senderMsgTime.text = dataFormat.format(message.sentDateTime)
            }
            else -> { // RECEIVE
                configReceiverMessageLayout(holder, message)
                holder.binding.receiverDialog.visibility = View.VISIBLE
                when (message.mime) {
                    Message.Mime.TEXT -> holder.binding.receiverText.text = getDisplayText(message)
                    Message.Mime.IMAGE -> setImage(holder.binding.receiverImage, message.oriFileName)
                    Message.Mime.VIDEO -> {
                        setVideo(holder.binding.receiverVideo, message.oriFileName)
                        holder.binding.root.setOnClickListener { onItemClick(message) }
                    }
                    Message.Mime.LOCATION -> {}
                }
                holder.binding.receiverTime.text = dataFormat.format(message.sentDateTime)
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

    private fun configSenderMessageLayout(holder: ViewHolder, message: Message) {
        holder.binding.senderText.visibility = View.GONE
        holder.binding.senderImage.visibility = View.GONE
        holder.binding.senderVideo.visibility = View.GONE

        when (message.mime) {
            Message.Mime.TEXT -> holder.binding.senderText.visibility = View.VISIBLE
            Message.Mime.IMAGE -> holder.binding.senderImage.visibility = View.VISIBLE
            Message.Mime.VIDEO -> holder.binding.senderVideo.visibility = View.VISIBLE
            Message.Mime.LOCATION -> {}
            else -> {}
        }
    }

    private fun configReceiverMessageLayout(holder: ViewHolder, message: Message) {
        holder.binding.receiverText.visibility = View.GONE
        holder.binding.receiverImage.visibility = View.GONE
        holder.binding.receiverVideo.visibility = View.GONE

        when (message.mime) {
            Message.Mime.TEXT -> holder.binding.receiverText.visibility = View.VISIBLE
            Message.Mime.IMAGE -> holder.binding.receiverImage.visibility = View.VISIBLE
            Message.Mime.VIDEO -> holder.binding.receiverVideo.visibility = View.VISIBLE
            Message.Mime.LOCATION -> {}
            else -> {}
        }
    }

    private fun setImage(imageView: ImageView, fileName: String) {
        val image = Drawable.createFromPath(mediaFolderPath + fileName) ?: return
        imageView.setImageDrawable(image)
    }

    private fun setVideo(videoView: VideoView, fileName: String) {
        val file = File(mediaFolderPath + fileName)
        if (!file.exists()) {
            return
        }
        videoView.setVideoPath(mediaFolderPath + fileName)
    }

    override fun getItemCount(): Int {
        return msgList.size
    }
}
