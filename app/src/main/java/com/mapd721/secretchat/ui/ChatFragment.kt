package com.mapd721.secretchat.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapd721.secretchat.R
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.ui.adpater.MsgRecyclerViewAdapter
import com.mapd721.secretchat.databinding.FragmentChatBinding
import com.mapd721.secretchat.logic.MessageBroadcast
import com.mapd721.secretchat.ui.dialog.AddContactDialogFragment
import com.mapd721.secretchat.ui.fragment_result_listener.CameraResultListener
import com.mapd721.secretchat.ui.view_model.ChatViewModel
import com.mapd721.secretchat.ui.view_model.GlobalViewModel

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var vm: ChatViewModel
    private val globalViewModel: GlobalViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        requireArguments().let {
            vm = ChatViewModel(
                ChatFactory(requireContext()),
                globalViewModel.selfId,
                it.getSerializable(ARG_CONTACT) as Contact,
                resources.getString(R.string.self_key_pair_name),
                resources.getString(R.string.cloud_storage_root_folder_name),
                requireActivity().filesDir.path + "/" +
                        resources.getString(R.string.media_sent_storage_root),
                requireActivity().contentResolver,
                requireActivity()
                    .getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager,
                { broadcastReceiver, intentFilter ->
                    requireActivity()
                        .registerReceiver(broadcastReceiver, intentFilter)
                },
                { intent, requestCode ->
                    startActivityForResult(intent, requestCode)
                },
                ::navigateToViewImageScreen,
                ::navigateToVideoPlaybackScreen,
                ::navigateToViewLocationScreen,
                ::navigateToCameraScreen
            )
        }

        parentFragmentManager.setFragmentResultListener(
            CameraResultListener.RESULT_LISTENER_KEY,
            this,
            CameraResultListener { filePath, fileType ->
                vm.onMediaReadyToSend(
                    filePath,
                    if (fileType == CameraResultListener.TYPE_IMAGE) Message.Mime.IMAGE
                    else Message.Mime.VIDEO
                )
            }
        )

        vm.loadAndListenMessages()
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(vm.messageReceiver)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(vm.messageReceiver, IntentFilter(MessageBroadcast.INTENT_FILTER))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar!!
        supportActionBar.setDisplayHomeAsUpEnabled(true)
        supportActionBar.title = vm.contact.name

        binding.btnSend.setOnClickListener(btnSendOnClickListener)
        val msgRecyclerViewLayoutManager = LinearLayoutManager(requireContext())
        msgRecyclerViewLayoutManager.stackFromEnd = true
        binding.msgRecyclerView.layoutManager = msgRecyclerViewLayoutManager
        vm.setAttachmentMenu(
            PopupMenu(requireContext(), binding.btnAttach)
        )

        vm.messagesLiveData.observe(requireActivity(), ::updateMessageRecyclerView)

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK
            || data == null) {
            return
        }
        vm.onIntentFinished(requestCode, data)
    }

    private fun showAddContactDialog() {
        AddContactDialogFragment()
            .show(
                childFragmentManager,
                AddContactDialogFragment.TAG
            )
    }

    private fun updateMessageRecyclerView(messages: List<Message>) {
        binding.msgRecyclerView.adapter = MsgRecyclerViewAdapter(
            messages,
            requireActivity().filesDir.path + "/" +
                    resources.getString(R.string.media_storage_root) + "/",
            requireActivity().filesDir.path + "/" +
                    resources.getString(R.string.media_sent_storage_root) + "/",
            vm::onChatMsgDialogClick
        )
    }

    private val btnSendOnClickListener = View.OnClickListener {
        if (binding.edtMsg.text == null
            || binding.edtMsg.text.toString().trim().isBlank()) {
            return@OnClickListener
        }
        vm.sendMessage(binding.edtMsg.text.toString())
        binding.edtMsg.text!!.clear()
    }

    private fun navigateToViewImageScreen(fileName: String, msgType: Int) {
        val filePath = if (msgType == Message.TYPE_SNED)
            requireActivity().filesDir.path + "/" +
                    resources.getString(R.string.media_sent_storage_root) + "/" +
                    fileName
        else
            requireActivity().filesDir.path + "/" +
                    resources.getString(R.string.media_storage_root) + "/" +
                    fileName

        findNavController().navigate(
            ChatFragmentDirections
                .actionChatFragmentToViewImageFragment(
                    filePath
                )
        )
    }

    private fun navigateToVideoPlaybackScreen(fileName: String, msgType: Int) {
        val filePath = if (msgType == Message.TYPE_SNED)
            requireActivity().filesDir.path + "/" +
                    resources.getString(R.string.media_sent_storage_root) + "/" +
                    fileName
        else
            requireActivity().filesDir.path + "/" +
                    resources.getString(R.string.media_storage_root) + "/" +
                    fileName

        findNavController().navigate(
            ChatFragmentDirections
                .actionChatFragmentToVideoPlaybackFragment(
                    filePath
                )
        )
    }

    private fun navigateToViewLocationScreen(latLng: String) {
        findNavController().navigate(
            ChatFragmentDirections
                .actionChatFragmentToViewLocationFragment(
                    latLng
                )
        )
    }

    private fun navigateToCameraScreen() {
        findNavController().navigate(
            ChatFragmentDirections
                .actionChatFragmentToCameraFragment()
        )
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