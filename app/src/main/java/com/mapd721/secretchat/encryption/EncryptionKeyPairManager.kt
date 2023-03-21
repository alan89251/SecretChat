package com.mapd721.secretchat.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.*

class EncryptionKeyPairManager {
    companion object {
        const val KEY_STORE_NAME = "AndroidKeyStore"
    }

    private val keyStore: KeyStore

    init {
        keyStore = KeyStore.getInstance(KEY_STORE_NAME)
        keyStore.load(null)
    }

    fun createKeyPair(keyPairName: String) {
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
        generator.generateKeyPair()
    }

    fun getKeyPair(keyPairName: String): KeyPair? {
        val privateKey = keyStore.getKey(keyPairName, null) as PrivateKey?
        val publicKey = keyStore.getCertificate(keyPairName)?.publicKey
        if (privateKey == null
            || publicKey == null
        ) {
            return null
        }
        return KeyPair(publicKey, privateKey)
    }

    fun getKey(name: String): PrivateKey? {
        return keyStore.getKey(name, null) as PrivateKey?
    }

    fun getCertificate(name: String): PublicKey? {
        return keyStore.getCertificate(name)?.publicKey
    }
}