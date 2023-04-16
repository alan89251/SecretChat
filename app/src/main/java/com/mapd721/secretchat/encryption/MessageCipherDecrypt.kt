package com.mapd721.secretchat.encryption

import android.util.Base64
import java.security.Key
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class MessageCipherDecrypt (private val key: Key) {
    companion object {
        const val RSA_PADDING = "RSA/ECB/PKCS1Padding"
        const val AES = "AES/CBC/PKCS5PADDING"
        const val AES_KEY_SIZE = 256
        const val AES_IV_ENCRYPTED_SIZE = 256
    }

    private val cipher: Cipher

    init {
        cipher = Cipher.getInstance(RSA_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, key)
    }

    fun decrypt(msg: String): String {
        return String(
            cipher
                .doFinal(
                    Base64.decode(
                        msg,
                        Base64.DEFAULT
                    )
                )
        )
    }

    fun decrypt(bytes: ByteArray): ByteArray {
        val encryptedKey = bytes.copyOfRange(0, AES_KEY_SIZE)
        val encryptedIv = bytes.copyOfRange(AES_KEY_SIZE, AES_KEY_SIZE + AES_IV_ENCRYPTED_SIZE)
        val encryptedBytes = bytes.copyOfRange(AES_KEY_SIZE + AES_IV_ENCRYPTED_SIZE, bytes.size)
        val decryptedKeyBytes = cipher.doFinal(encryptedKey)
        val decryptedKey = SecretKeySpec(decryptedKeyBytes, AES)
        val decryptedIvBytes = cipher.doFinal(encryptedIv)
        val decryptedIv = IvParameterSpec(decryptedIvBytes)
        val cipherAES = Cipher.getInstance(AES)
        cipherAES.init(Cipher.DECRYPT_MODE, decryptedKey, decryptedIv)
        return cipherAES.doFinal(encryptedBytes)
    }
}