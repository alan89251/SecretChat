package com.mapd721.secretchat.ui.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mapd721.secretchat.R
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.databinding.RecyclerViewChatListBinding

class ChatListRecyclerViewAdapter(
    var contactList: List<Contact>,
    var onItemClick: (View, Int, Contact) -> Unit
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
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}