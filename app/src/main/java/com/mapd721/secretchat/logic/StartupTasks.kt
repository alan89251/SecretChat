package com.mapd721.secretchat.logic

import android.util.Base64
import com.mapd721.secretchat.data_model.encryption_key.EncryptionKey
import com.mapd721.secretchat.encryption.EncryptionKeyPairManager
import com.mapd721.secretchat.data_source.repository.EncryptionKeyRepositoryFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartupTasks {
    companion object {
        const val SELF_KEY_PAIR_NAME = "self_key"

        /*fun run() {
            CoroutineScope(Dispatchers.IO).launch {
                initSelfEncryptionKeyPair()
            }
        }*/
    }
}