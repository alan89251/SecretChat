package com.mapd721.secretchat.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapd721.secretchat.R
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.databinding.FragmentChatListBinding
import com.mapd721.secretchat.logic.CloudImageDownloader
import com.mapd721.secretchat.logic.MessageBroadcast
import com.mapd721.secretchat.service.MessageFirebaseService
import com.mapd721.secretchat.service.MessageServiceCmd
import com.mapd721.secretchat.ui.adpater.ChatListRecyclerViewAdapter
import com.mapd721.secretchat.ui.dialog.AddContactDialogFragment
import com.mapd721.secretchat.ui.dialog.WeatherDialogFragment
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
            globalViewModel.contactManager,
            ChatFactory(requireContext()),
            globalViewModel.selfId,
            resources.getString(R.string.self_key_pair_name),
            resources.getString(R.string.cloud_storage_root_folder_name),
            { broadcastReceiver, intentFilter ->
                requireActivity()
                    .registerReceiver(broadcastReceiver, intentFilter)
            }
        )

        childFragmentManager.setFragmentResultListener(
            AddContactDialogFragment.RESULT_LISTENER_KEY,
            this) { _, bundle ->
                val contactId = bundle.getString(AddContactDialogFragment.ARG_RESULT_CONTACT_ID)!!
                vm.onAddedContact()
                listenToMsgOfNewContactInService(contactId)
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(vm.messageReceiver)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(vm.messageReceiver, IntentFilter(MessageBroadcast.INTENT_FILTER))
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

        (requireActivity() as AppCompatActivity)
            .supportActionBar!!
            .setDisplayHomeAsUpEnabled(false)

        binding.contactSearchBar.setOnQueryTextListener(vm.contactSearchOnQueryTextListener)
        vm.contactListLiveData.observe(requireActivity(), ::onLoadedContactList)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.app_bar_btn_add_contact -> {
                AddContactDialogFragment()
                    .show(childFragmentManager, AddContactDialogFragment.TAG)
                true
            }
            R.id.app_bar_btn_weather -> {
                WeatherDialogFragment()
                    .show(childFragmentManager, WeatherDialogFragment.TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onLoadedContactList(contactList: MutableList<Contact>) {
        val chatListLayoutManager = LinearLayoutManager(requireContext())
        chatListLayoutManager.reverseLayout = true
        chatListLayoutManager.stackFromEnd = true
        binding.chatList.layoutManager = chatListLayoutManager
        binding.chatList.adapter = ChatListRecyclerViewAdapter(
            contactList,
            resources.getString(R.string.profile_picture_folder_path),
            CloudImageDownloader(requireContext()),
            ::onChatListItemClick,
            { _, _, contact, onMessageUpdate ->
                vm.initContactItem(contact, onMessageUpdate::onMessageUpdate)
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

    private fun listenToMsgOfNewContactInService(contactId: String) {
        val intent = Intent(requireActivity(), MessageFirebaseService::class.java)
        intent.putExtra(MessageServiceCmd.KEY_CMD, MessageServiceCmd.CMD_ADD_CONTACT)
        intent.putExtra(MessageServiceCmd.ARG_CONTACT, contactId)
        requireActivity().startService(intent)
    }
}