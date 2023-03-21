package com.mapd721.secretchat.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mapd721.secretchat.R
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.data_source.repository.ContactRepositoryFactory
import com.mapd721.secretchat.data_source.repository.EncryptionKeyRepositoryFactory
import com.mapd721.secretchat.databinding.FragmentChatListBinding
import com.mapd721.secretchat.logic.ContactManager
import com.mapd721.secretchat.ui.adpater.ChatListRecyclerViewAdapter
import com.mapd721.secretchat.ui.view_model.ChatListViewModel
import com.mapd721.secretchat.ui.view_model.GlobalViewModel

class ChatListFragment : Fragment() {
    private val globalViewModel: GlobalViewModel by activityViewModels()
    private lateinit var binding: FragmentChatListBinding
    private lateinit var vm: ChatListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        vm = ChatListViewModel(
            ContactManager(
                ContactRepositoryFactory(requireContext()).getLocalRepository(),
                EncryptionKeyRepositoryFactory().getRemoteRepository()
            ),
            ChatFactory(requireContext()),
            globalViewModel.selfId,
            resources.getString(R.string.self_key_pair_name)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_menu, menu)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        vm.contactListLiveData.observe(requireActivity(), ::onLoadedContactList)

        return binding.root
    }

    private fun onLoadedContactList(contactList: MutableList<Contact>) {
        vm.contactListLiveData.removeObservers(requireActivity())
        binding.chatList.layoutManager = GridLayoutManager(requireContext(), ChatListViewModel.CHAT_LIST_COL_NUM)
        binding.chatList.adapter = ChatListRecyclerViewAdapter(
            contactList,
            ::onChatListItemClick,
            { _, _, contact, onMessageUpdate ->
                vm.listenMessage(contact)
            }
        )
    }

    private fun onChatListItemClick(view: View, position: Int, contact: Contact) {
        navToChatFragment(contact)
    }

    private fun navToChatFragment(contact: Contact) {
        val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(
                contact
            )
        findNavController().navigate(action)
    }
}