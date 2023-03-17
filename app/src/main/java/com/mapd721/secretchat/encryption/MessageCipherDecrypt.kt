package com.mapd721.secretchat.encryption

import android.util.Base64
import java.security.Key
import javax.crypto.Cipher

class MessageCipherDecrypt (private val key: Key) {
    companion object {
        const val RSA_PADDING = "RSA/ECB/PKCS1Padding"
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
}