package com.mapd721.secretchat.logic

import com.mapd721.secretchat.repository.ContactRepository

class ContactManager(
    private val localRepository: ContactRepository,
    private val remoteRepository: ContactRepository
    ) {

    fun addContact(id: Int) {
        val contact = remoteRepository.getById(id)
            ?: throw IllegalArgumentException("Contact not exist in remote")
        localRepository.insert(contact)
    }
}