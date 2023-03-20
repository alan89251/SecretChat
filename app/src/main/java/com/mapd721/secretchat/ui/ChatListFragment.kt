package com.mapd721.secretchat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ContactRepositoryFactory
import com.mapd721.secretchat.data_source.repository.EncryptionKeyRepositoryFactory
import com.mapd721.secretchat.databinding.FragmentChatListBinding
import com.mapd721.secretchat.logic.ContactManager
import com.mapd721.secretchat.ui.adpater.ChatListRecyclerViewAdapter
import com.mapd721.secretchat.ui.view_model.ChatListViewModel

class ChatListFragment : Fragment() {
    private lateinit var binding: FragmentChatListBinding
    private lateinit var vm: ChatListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ChatListViewModel(
            ContactManager(
                ContactRepositoryFactory(requireContext()).getLocalRepository(),
                EncryptionKeyRepositoryFactory().getRemoteRepository()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        vm.contactList.observe(requireActivity(), ::onLoadedContactList)

        return binding.root
    }

    private fun onLoadedContactList(contactList: MutableList<Contact>) {
        vm.contactList.removeObservers(requireActivity())
        binding.chatList.layoutManager = GridLayoutManager(requireContext(), ChatListViewModel.CHAT_LIST_COL_NUM)
        binding.chatList.adapter = ChatListRecyclerViewAdapter(contactList)
    }
}