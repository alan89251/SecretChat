package com.mapd721.secretchat.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
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

        vm.addContact(contactId, contactId, {
            closeDialog(contactId)
        }, {
            Toast.makeText(requireContext(), "Fail to add contact: $it", Toast.LENGTH_SHORT)
        })
    }

    private fun closeDialog(contactId: String) {
        val bundle = Bundle()
        bundle.putString(ARG_RESULT_CONTACT_ID, contactId)
        setFragmentResult(
            RESULT_LISTENER_KEY,
            bundle
        )
        dismiss()
    }

    companion object {
        const val TAG = "AddContactDialog"
        const val RESULT_LISTENER_KEY = "AddContactDialogResult"
        const val ARG_RESULT_CONTACT_ID = "contactId"
    }
}