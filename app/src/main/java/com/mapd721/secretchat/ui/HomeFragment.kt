package com.mapd721.secretchat.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mapd721.secretchat.R
import com.mapd721.secretchat.data_source.repository.ContactRepositoryFactory
import com.mapd721.secretchat.data_source.repository.EncryptionKeyRepositoryFactory
import com.mapd721.secretchat.databinding.FragmentHomeBinding
import com.mapd721.secretchat.logic.ContactManager
import com.mapd721.secretchat.ui.view_model.GlobalViewModel

class HomeFragment : Fragment() {
    private val globalViewModel: GlobalViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init globalViewModel
        globalViewModel.selfIdPreferenceKey = resources.getString(R.string.self_id_preference_key)
        globalViewModel.selfKeyPairName = resources.getString(R.string.self_key_pair_name)
        globalViewModel.sharedPreferences = requireActivity().getSharedPreferences(resources.getString(R.string.preference_name),
            AppCompatActivity.MODE_PRIVATE
        )
        globalViewModel.contactManager = ContactManager(
            ContactRepositoryFactory(requireActivity()).getLocalRepository(),
            EncryptionKeyRepositoryFactory().getRemoteRepository()
        )

        globalViewModel.selfId = globalViewModel.sharedPreferences.getString(
            resources.getString(R.string.self_id_preference_key),
            ""
        ).toString()
        if (globalViewModel.selfId != "") {
            navToChatListFragment()
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btnRegister.setOnClickListener(btnRegisterOnClickListener)

        return binding.root
    }

    private val btnRegisterOnClickListener = View.OnClickListener {
        if (binding.etUserId.text.toString() == "") {
            return@OnClickListener
        }
        globalViewModel.saveSelfId(binding.etUserId.text.toString())
        globalViewModel.registerAccount(binding.etUserId.text.toString()) {
            navToChatListFragment()
        }
    }

    private fun navToChatListFragment() {
        findNavController().navigate(
            HomeFragmentDirections
                .actionHomeFragmentToChatListFragment()
        )
    }
}