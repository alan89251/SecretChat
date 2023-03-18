package com.mapd721.secretchat.logic

import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ContactRepository
import com.mapd721.secretchat.data_source.repository.EncryptionKeyRepository

class ContactManager(
    private val contactRepository: ContactRepository,
    private val encryptionKeyRepository: EncryptionKeyRepository
    ) {

    fun addContact(id: String, name: String) {
        val encryptionKey = encryptionKeyRepository.getById(id)
            ?: throw IllegalArgumentException("Contact not exist in remote")
        val contact = Contact()
        contact.id = id
        contact.name = name
        contact.key = encryptionKey.key
        contactRepository.insert(contact)
    }

    fun getAll(): List<Contact> {
        return contactRepository.getAll()
    }
}