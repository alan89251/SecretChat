package com.mapd721.secretchat.logic

import android.content.Context
import com.mapd721.secretchat.encryption.KeyPairManager

class SelfEncryptionKeyPairInitializer {
    companion object {
        const val SELF_KEY_PAIR_NAME = "self_key"

        /**
         * Generate new self encryption key pair if not exist
         */
        fun run(context: Context) {
            val keyPairManager = KeyPairManager()
            if (keyPairManager.getKeyPair(SELF_KEY_PAIR_NAME) == null) {
                keyPairManager.createKeyPair(SELF_KEY_PAIR_NAME)
            }
        }
    }
}