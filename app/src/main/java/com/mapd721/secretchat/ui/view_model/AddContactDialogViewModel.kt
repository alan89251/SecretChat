package com.mapd721.secretchat.ui.view_model

import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.logic.ContactManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddContactDialogViewModel(
    private val contactManager: ContactManager
) : ViewModel() {
    fun addContact(contactId: String, name: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                contactManager.addContact(contactId, name)
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }
            catch (e: IllegalArgumentException) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }
}