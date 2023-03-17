package com.mapd721.secretchat.encryption

import android.util.Base64
import java.security.KeyPair
import javax.crypto.Cipher

class MessageCipher (
    private val keyPair: KeyPair
) {
    companion object {
        const val RSA_PADDING = "RSA/ECB/PKCS1Padding"
    }

    private val cipherEncrypt: Cipher
    private val cipherDecrypt: Cipher

    init {
        cipherEncrypt = Cipher.getInstance(RSA_PADDING)
        cipherEncrypt.init(Cipher.ENCRYPT_MODE, keyPair.public)

        cipherDecrypt = Cipher.getInstance(RSA_PADDING)
        cipherDecrypt.init(Cipher.DECRYPT_MODE, keyPair.private)
    }

    fun encrypt(msg: String): String {
        return Base64.encodeToString(
            cipherEncrypt
                .doFinal(
                    msg.toByteArray()
                ),
            Base64.DEFAULT
        )
    }

    fun decrypt(msg: String): String {
        return String(
            cipherDecrypt
                .doFinal(
                    Base64.decode(
                        msg,
                        Base64.DEFAULT
                    )
                )
        )
    }
}