package com.mapd721.secretchat.repository

import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_model.contact.ContactDao

class ContactRepository (
    private val dao: ContactDao
) {
    fun insert(contact: Contact) {
        dao.insert(contact)
    }

    fun getAll(): List<Contact> {
        return dao.getAll()
    }

    fun getById(id: String): Contact? {
        return dao.getById(id)
    }
}