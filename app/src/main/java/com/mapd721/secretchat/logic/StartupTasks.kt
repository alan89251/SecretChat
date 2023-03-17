package com.mapd721.secretchat.logic

import android.content.Context
import com.mapd721.secretchat.encryption.SelfEncryptionKeyPairManager

class StartupTasks {
    companion object {
        const val SELF_KEY_PAIR_NAME = "self_key"

        fun run(context: Context) {
            initSelfEncryptionKeyPair()
        }

        /**
         * Generate new self encryption key pair if not exist
         */
        private fun initSelfEncryptionKeyPair() {
            val manager = SelfEncryptionKeyPairManager()
            if (manager.getKeyPair(SELF_KEY_PAIR_NAME) == null) {
                manager.createKeyPair(SELF_KEY_PAIR_NAME)
            }
        }
    }
}