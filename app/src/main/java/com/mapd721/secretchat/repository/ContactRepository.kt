package com.mapd721.secretchat.repository

import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_model.contact.ContactDao

class ContactRepository (
    private val localDao: ContactDao
) {
    fun insert(contact: Contact) {
        localDao.insert(contact)
    }

    fun getAll(): List<Contact> {
        return localDao.getAll()
    }
}