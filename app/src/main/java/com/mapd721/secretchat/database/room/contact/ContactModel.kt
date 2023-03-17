package com.mapd721.secretchat.database.room.contact

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.mapd721.secretchat.data_model.contact.Contact

@Entity(tableName = ContactFields.TABLE)
data class ContactModel(
    @ColumnInfo(name = ContactFields.FIELD_NAME)
    var name: String,

    @ColumnInfo(name = ContactFields.FIELD_KEY)
    var key: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ContactFields.FIELD_ID)
    var id: Int? = null

    constructor(contact: Contact) : this(contact.name, contact.key) {
        id = contact.id
    }

    @Ignore
    fun toContact(): Contact {
        val contact = Contact()
        contact.id = id!!
        contact.name = name
        contact.key = key
        return contact
    }
}