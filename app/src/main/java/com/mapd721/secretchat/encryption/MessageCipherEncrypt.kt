package com.mapd721.secretchat.encryption

import android.util.Base64
import java.security.Key
import javax.crypto.Cipher

class MessageCipherEncrypt (private val key: Key) {
    companion object {
        const val RSA_PADDING = "RSA/ECB/PKCS1Padding"
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
}