package com.mapd721.secretchat.ui.view_model

import android.content.SharedPreferences
import android.util.Base64
import androidx.lifecycle.ViewModel
import com.mapd721.secretchat.data_model.encryption_key.EncryptionKey
import com.mapd721.secretchat.data_source.repository.EncryptionKeyRepositoryFactory
import com.mapd721.secretchat.encryption.EncryptionKeyPairManager
import com.mapd721.secretchat.logic.ContactManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GlobalViewModel: ViewModel() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var selfIdPreferenceKey: String
    lateinit var selfKeyPairName: String
    var selfId = ""
    lateinit var contactManager: ContactManager

    fun registerAccount(selfId: String, onRegistered: () -> Unit) {
        saveSelfId(selfId)
        initSelfEncryptionKeyPair()
        CoroutineScope(Dispatchers.IO).launch {
            publishPublicKey()

            withContext(Dispatchers.Main) {
                onRegistered()
            }
        }
    }

    fun saveSelfId(selfId: String) {
        this.selfId = selfId
        val editor = sharedPreferences.edit()
        editor.putString(selfIdPreferenceKey, this.selfId)
        editor.commit()
    }

    private fun initSelfEncryptionKeyPair() {
        val manager = EncryptionKeyPairManager()
        if (manager.getKeyPair(selfKeyPairName) == null) {
            manager.createKeyPair(selfKeyPairName)
        }
    }

    private fun publishPublicKey() {
        val encryptionKey = EncryptionKey()
        encryptionKey.id = selfId
        encryptionKey.key = getSelfPublicKeyStr()
        EncryptionKeyRepositoryFactory()
            .getRemoteRepository()
            .insert(encryptionKey)
    }

    private fun getSelfPublicKeyStr(): String {
        val manager = EncryptionKeyPairManager()
        val keyPair = manager.getKeyPair(selfKeyPairName)!!
        return Base64.encodeToString(
            keyPair.public
                .encoded,
            Base64.DEFAULT
        )
    }
}