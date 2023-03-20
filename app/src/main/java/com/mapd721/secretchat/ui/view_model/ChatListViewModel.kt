package com.mapd721.secretchat.ui.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.logic.ContactManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListViewModel(
    contactManager: ContactManager
): ViewModel() {
    companion object {
        const val CHAT_LIST_COL_NUM = 1
    }

    val contactList: MutableLiveData<MutableList<Contact>> = MutableLiveData()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val list = contactManager.getAll()

            withContext(Dispatchers.Main) {
                contactList.value = ArrayList(list)
            }
        }
    }
}