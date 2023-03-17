package com.mapd721.secretchat.database.firestore.contact

import com.mapd721.secretchat.data_model.contact.Contact

class ContactModel {
    var id: Int = 0
    var name: String = ""
    var key: String = "'"

    fun toContact(): Contact {
        val contact = Contact()
        contact.id = id
        contact.name = name
        contact.key = key
        return contact
    }
}