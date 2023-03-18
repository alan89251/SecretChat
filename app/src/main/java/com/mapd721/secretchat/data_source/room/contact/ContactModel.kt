package com.mapd721.secretchat.data_source.room.contact

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
    var key: String,

    @PrimaryKey
    @ColumnInfo(name = ContactFields.FIELD_ID)
    var id: String
) {
    constructor(contact: Contact) : this(
        contact.name,
        contact.key,
        contact.id
    )

    @Ignore
    fun toContact(): Contact {
        val contact = Contact()
        contact.id = id
        contact.name = name
        contact.key = key
        return contact
    }
}