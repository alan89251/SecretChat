package com.mapd721.secretchat.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.mapd721.secretchat.databinding.DialogAddContactBinding
import com.mapd721.secretchat.ui.view_model.AddContactDialogViewModel
import com.mapd721.secretchat.ui.view_model.GlobalViewModel

class AddContactDialogFragment : DialogFragment() {
    private val globalViewModel: GlobalViewModel by activityViewModels()
    private lateinit var binding: DialogAddContactBinding
    private lateinit var vm: AddContactDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = AddContactDialogViewModel(globalViewModel.contactManager)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddContactBinding.inflate(LayoutInflater.from(context))
        binding.vm = vm
        binding.lifecycleOwner = this

        binding.btnAdd.setOnClickListener(btnAddOnClickListener)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    private val btnAddOnClickListener = View.OnClickListener {
        val contactId = binding.etUserId.text.toString()
        if (contactId.isBlank()) {
            return@OnClickListener
        }

        vm.addContact(contactId, contactId) {
            closeDialog()
        }
    }

    private fun closeDialog() {
        dismiss()
    }

    companion object {
        const val TAG = "AddContactDialog"
    }
}