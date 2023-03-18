package com.mapd721.secretchat.logic

import android.util.Base64
import com.mapd721.secretchat.data_model.encryption_key.EncryptionKey
import com.mapd721.secretchat.encryption.SelfEncryptionKeyPairManager
import com.mapd721.secretchat.data_source.repository.EncryptionKeyRepositoryFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartupTasks {
    companion object {
        const val SELF_KEY_PAIR_NAME = "self_key"

        fun run() {
            CoroutineScope(Dispatchers.IO).launch {
                initSelfEncryptionKeyPair()
            }
        }

        /**
         * Generate new self encryption key pair if not exist
         */
        private fun initSelfEncryptionKeyPair() {
            val manager = SelfEncryptionKeyPairManager()
            if (manager.getKeyPair(SELF_KEY_PAIR_NAME) == null) {
                manager.createKeyPair(SELF_KEY_PAIR_NAME)
            }

            publishPublicKey()
        }

        private fun publishPublicKey() {
            val encryptionKey = EncryptionKey()
            encryptionKey.id = getSelfId()
            encryptionKey.key = getSelfPublicKeyStr()
            EncryptionKeyRepositoryFactory()
                .getRemoteRepository()
                .insert(encryptionKey)
        }

        private fun getSelfPublicKeyStr(): String {
            val manager = SelfEncryptionKeyPairManager()
            val keyPair = manager.getKeyPair(SELF_KEY_PAIR_NAME)!!
            return Base64.encodeToString(
                keyPair.public
                    .encoded,
                Base64.DEFAULT
            )
        }

        private fun getSelfId(): String {
            return "Alan"
        }
    }
}