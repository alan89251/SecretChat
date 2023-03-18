package com.mapd721.secretchat.data_source.room.contact

import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_model.contact.ContactDao

class ContactDaoRoomAdapter (
    private val dao: ContactDaoRoom
): ContactDao {

    override fun insert(contact: Contact) {
        dao.insert(
            ContactModel(
                contact
            )
        )
    }

    override fun getAll(): List<Contact> {
        return dao.getAll()
            .map {
                it.toContact()
            }
    }

    override fun getById(id: String): Contact? {
        val result = dao.getById(id)
        return if (result.isEmpty()) {
            null
        } else {
            result[0].toContact()
        }
    }
}