package com.mapd721.secretchat.database.room.contact

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDaoRoom {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: ContactModel)

    @Query("SELECT * FROM ${ContactFields.TABLE}")
    fun getAll(): List<ContactModel>

    @Query("SELECT * FROM ${ContactFields.TABLE} WHERE ${ContactFields.FIELD_ID} = :id")
    fun getById(id: Int): List<ContactModel>
}