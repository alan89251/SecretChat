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
}