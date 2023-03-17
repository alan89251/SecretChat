package com.mapd721.secretchat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mapd721.secretchat.data_model.encryption_key.EncryptionKeyDao
import com.mapd721.secretchat.data_model.encryption_key.EncryptionKeyModel

@Database(
    entities = arrayOf(
        EncryptionKeyModel::class
    ),
    version = 1,
    exportSchema = false
)
abstract class SecretChatDatabase: RoomDatabase() {
    abstract fun encryptionKeyModelDao(): EncryptionKeyDao

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