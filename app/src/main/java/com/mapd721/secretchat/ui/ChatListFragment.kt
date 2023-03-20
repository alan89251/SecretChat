package com.mapd721.secretchat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User
import com.mapd721.secretchat.R
import com.mapd721.secretchat.databinding.FragmentChatListBinding
import com.mapd721.secretchat.ui.view_model.ChatListViewModel

class ChatListFragment : Fragment() {
    private lateinit var binding: FragmentChatListBinding
    private lateinit var vm: ChatListViewModel
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ChatListViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        return binding.root
    }
}