package com.mapd721.secretchat.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.mapd721.secretchat.service.MessageFirebaseService
import com.mapd721.secretchat.ui.view_model.GlobalViewModel
import java.io.File

class HomeFragment : Fragment() {
    private val globalViewModel: GlobalViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private var profilePictureUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        globalViewModel.initViewModel(
            requireActivity().getSharedPreferences(resources.getString(R.string.preference_name), AppCompatActivity.MODE_PRIVATE),
            resources.getString(R.string.self_id_preference_key),
            resources.getString(R.string.self_key_pair_name),
            resources.getString(R.string.profile_picture_folder_path),
            ContactManager(
                ContactRepositoryFactory(requireActivity()).getLocalRepository(),
                EncryptionKeyRepositoryFactory().getRemoteRepository()
            ),
            requireActivity().contentResolver
        )

        if (globalViewModel.selfId != "") {
            runStartupTasks()
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
        binding.profilePicture.setOnClickListener(onProfilePictureImageViewClick)

        return binding.root
    }

    private fun runStartupTasks() {
        createMediaFolder()
        createMediaSentFolder()
        startMessageFirebaseService()
    }

    private fun createMediaFolder() {
        val folder = File(requireActivity().filesDir.path + "/" + resources.getString(R.string.media_storage_root))
        if (!folder.exists()) {
            folder.mkdir()
        }
    }

    private fun createMediaSentFolder() {
        val folder = File(requireActivity().filesDir.path + "/" + resources.getString(R.string.media_sent_storage_root))
        if (!folder.exists()) {
            folder.mkdir()
        }
    }

    private fun startMessageFirebaseService() {
        requireActivity()
            .startService(
                Intent(requireActivity(), MessageFirebaseService::class.java)
            )
    }

    private val btnRegisterOnClickListener = View.OnClickListener {
        if (binding.etUserId.text.toString() == "") {
            return@OnClickListener
        }
        globalViewModel.saveSelfId(binding.etUserId.text.toString())
        globalViewModel.registerAccount(binding.etUserId.text.toString(), profilePictureUri) {
            runStartupTasks()
            navToChatListFragment()
        }
    }

    private val onProfilePictureImageViewClick = View.OnClickListener {
        val intent = Intent()
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, SELECT_PROFILE_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK
            || data == null) {
            return
        }

        if (requestCode != SELECT_PROFILE_PICTURE) {
            return
        }

        val imageUriList = ArrayList<Uri>()
        if (data.clipData != null) {
            val clipData = data.clipData!!
            val itemCount = clipData.itemCount
            for (i in 0 until itemCount) {
                val imageUri = clipData.getItemAt(i).uri
                imageUriList.add(imageUri)
            }
        }
        else {
            val imageUri = data.data!!
            imageUriList.add(imageUri)
        }
        profilePictureUri = imageUriList[0]

        binding.profilePicture.setImageURI(profilePictureUri)
    }

    private fun navToChatListFragment() {
        findNavController().navigate(
            HomeFragmentDirections
                .actionHomeFragmentToChatListFragment()
        )
    }

    companion object {
        const val SELECT_PROFILE_PICTURE = 1
    }
}