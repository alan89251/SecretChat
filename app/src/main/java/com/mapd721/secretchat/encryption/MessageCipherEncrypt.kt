package com.mapd721.secretchat.encryption

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

class MessageCipherEncrypt (private val key: Key) {
    companion object {
        const val RSA_PADDING = "RSA/ECB/PKCS1Padding"
        const val AES = "AES/CBC/PKCS5PADDING"
        const val AES_KEY_SIZE = 256
        const val AES_IV_ENCRYPTED_SIZE = 256
    }

    private val cipher: Cipher

    init {
        cipher = Cipher.getInstance(RSA_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, key)
    }

    fun encrypt(msg: String): String {
        return Base64.encodeToString(
            cipher
                .doFinal(
                    msg.toByteArray()
                ),
            Base64.DEFAULT
        )
    }

    fun encrypt(bytes: ByteArray): ByteArray {
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(AES_KEY_SIZE)
        val key = keygen.generateKey()
        val cipherAES = Cipher.getInstance(AES)
        cipherAES.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipherAES.doFinal(bytes)
        val encryptedIv = cipher.doFinal(cipherAES.iv)
        val encryptedKey = cipher.doFinal(key.encoded)
        val outputStream = ByteArrayOutputStream()
        outputStream.write(encryptedKey)
        outputStream.write(encryptedIv)
        outputStream.write(encryptedBytes)
        return outputStream.toByteArray()
    }
}