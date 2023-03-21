package com.mapd721.secretchat.logic

import android.util.Base64
import com.mapd721.secretchat.encryption.MessageCipherEncrypt
import com.mapd721.secretchat.data_source.repository.ContactRepository
import com.mapd721.secretchat.encryption.MessageCipherDecrypt
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.X509EncodedKeySpec

class MessageCipherFactory(
    private val contactRepository: ContactRepository
) {
    fun getCipherByContactId(contactId: String): MessageCipherEncrypt {
        val contact = contactRepository.getById(contactId)
            ?: throw IllegalArgumentException("Cannot get cipher for the contact")
        val bytes = Base64.decode(
            contact.key.toByteArray(),
            Base64.DEFAULT
        )
        val x509EncodedKey = X509EncodedKeySpec(bytes)
        val publicKey = KeyFactory.getInstance("RSA")
            .generatePublic(x509EncodedKey)
        return MessageCipherEncrypt(publicKey)
    }

    companion object {
        fun getCipherEncryptFromKey(key: String): MessageCipherEncrypt {
            val bytes = Base64.decode(
                key.toByteArray(),
                Base64.DEFAULT
            )
            val x509EncodedKey = X509EncodedKeySpec(bytes)
            val publicKey = KeyFactory.getInstance("RSA")
                .generatePublic(x509EncodedKey)
            return MessageCipherEncrypt(publicKey)
        }

        fun getCipherDecryptFromKey(key: PrivateKey): MessageCipherDecrypt {
            return MessageCipherDecrypt(key)
        }
    }
}