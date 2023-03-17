package com.mapd721.secretchat.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mapd721.secretchat.database.room.chat.ChatDaoRoom
import com.mapd721.secretchat.database.room.contact.ContactDaoRoom
import com.mapd721.secretchat.database.room.contact.ContactModel
import com.mapd721.secretchat.database.room.message.MessageDaoRoom
import com.mapd721.secretchat.database.room.message.MessageDaoRoomAdapter
import com.mapd721.secretchat.database.room.message.MessageModel

@Database(
    entities = arrayOf(
        ContactModel::class,
        MessageModel::class
    ),
    version = 6,
    exportSchema = false
)
abstract class SecretChatDatabase: RoomDatabase() {
    abstract fun contactModelDao(): ContactDaoRoom
    abstract fun messageModelDao(): MessageDaoRoom

    fun chatDao(id: String): ChatDaoRoom {
        return ChatDaoRoom(
            id,
            MessageDaoRoomAdapter(messageModelDao())
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: SecretChatDatabase? = null
        const val DATABASE_NAME = "secret_chat_database"

        fun getDatabaseClient(context: Context): SecretChatDatabase {
            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(
                        context,
                        SecretChatDatabase::class.java,
                        DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!
            }
        }
    }
}