package com.mapd721.secretchat.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey

class KeyPairManager {
    companion object {
        const val KEY_STORE_NAME = "AndroidKeyStore"
    }

    fun createKeyPair(keyPairName: String): KeyPair {
        val generator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA,
            KEY_STORE_NAME
        )
        generator.initialize(
            KeyGenParameterSpec.Builder(
                keyPairName,
                KeyProperties.PURPOSE_ENCRYPT
                    or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build()
        )
        val keyPair = generator.generateKeyPair()
        //TODO save in room
        return keyPair
    }

    fun getKeyPair(keyPairName: String): KeyPair {
        val keyStore = KeyStore.getInstance(KEY_STORE_NAME)
        keyStore.load(null)
        val privateKey = keyStore.getKey(keyPairName, null) as PrivateKey?
        val publicKey = keyStore.getCertificate(keyPairName)?.publicKey
        return KeyPair(publicKey!!, privateKey!!)
    }


}