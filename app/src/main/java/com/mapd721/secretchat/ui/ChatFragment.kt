package com.mapd721.secretchat.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapd721.secretchat.R
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.ui.adpater.MsgRecyclerViewAdapter
import com.mapd721.secretchat.databinding.FragmentChatBinding
import com.mapd721.secretchat.ui.dialog.AddContactDialogFragment
import com.mapd721.secretchat.ui.view_model.ChatViewModel
import com.mapd721.secretchat.ui.view_model.GlobalViewModel

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var vm: ChatViewModel
    private val globalViewModel: GlobalViewModel by activityViewModels()
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var sendMessageEditText: EditText
    private lateinit var sendMessageButton: FloatingActionButton
    private lateinit var  messageLayoutManager: RecyclerView.LayoutManager
    private lateinit var msgRecyclerViewAdapter: MsgRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        requireArguments().let {
            vm = ChatViewModel(
                ChatFactory(requireContext()),
                globalViewModel.selfId,
                it.getSerializable(ARG_CONTACT) as Contact,
                resources.getString(R.string.self_key_pair_name)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        vm.listenMessage()

        binding.btnSend.setOnClickListener(btnSendOnClickListener)

        binding.msgRecyclerView.layoutManager = GridLayoutManager(requireContext(), ChatViewModel.CHAT_LIST_COL_NUM)
        vm.messagesLiveData.observe(requireActivity(), ::updateMessageRecyclerView)
        vm.loadAllMessagesFromDB()

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.app_bar_btn_add_contact -> {
                showAddContactDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAddContactDialog() {
        AddContactDialogFragment()
            .show(
                childFragmentManager,
                AddContactDialogFragment.TAG
            )
    }

    private fun updateMessageRecyclerView(messages: List<Message>) {
        binding.msgRecyclerView.adapter = MsgRecyclerViewAdapter(messages)
    }

    private val btnSendOnClickListener = View.OnClickListener {
        if (binding.edtMsg.text == null
            || binding.edtMsg.text.toString().trim().isBlank()) {
            return@OnClickListener
        }
        vm.sendMessage(binding.edtMsg.text.toString())
        binding.edtMsg.text!!.clear()
    }

    companion object {
        const val ARG_CONTACT = "contact"

        @JvmStatic
        fun newInstance(contact: Contact) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CONTACT, contact)
                }
            }
    }
}