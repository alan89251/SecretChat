package com.mapd721.secretchat.data_model.contact

interface ContactDao {
    fun insert(contact: Contact)
    fun getAll(): List<Contact>
}