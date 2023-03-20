package com.mapd721.secretchat.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import com.mapd721.secretchat.R
import com.mapd721.secretchat.adpater.MessageAdapter
import com.mapd721.secretchat.databinding.FragmentChatBinding
import com.mapd721.secretchat.ui.view_model.ChatViewModel
import java.util.Calendar

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var vm: ChatViewModel
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var sendMessageEditText: EditText
    private lateinit var sendMessageButton: FloatingActionButton
//    private lateinit var fstore: FirebaseFirestore
//    private lateinit var fauth: FirebaseAuth
    private lateinit var  messageLayoutManager: RecyclerView.LayoutManager
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ChatViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
//        fstore = FirebaseFirestore.getInstance()
//        fauth = FirebaseAuth.getInstance()
        messageRecyclerView = view.findViewById(R.id.msg_recyclerView)
        sendMessageButton = view.findViewById(R.id.btn_send)
        sendMessageEditText = view.findViewById(R.id.edt_msg)
        messageLayoutManager = LinearLayoutManager(context)
        sendMessageButton.setOnClickListener{
            sendMessage()
        }
        return binding.root
    }
    private fun sendMessage() {
        val message = sendMessageEditText.text.toString()
        if(TextUtils.isEmpty(message))
        {
            sendMessageEditText.error = "Cannot be Empty"
        } else {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val timeStamp = "$hour: $minute"

//            val messageObject = mutableMapOf<String,String>().also {
//                it["message"] = message
//                it["messageSender"] = FirebaseAuth.getInstance().currentUser?.id.toString()
//                it["messageReceiver"] = ""
//                it["messageTime"] = timeStamp
//            }
        }
    }
}