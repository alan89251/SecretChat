package com.mapd721.secretchat.data_source.firestore.file

import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.mapd721.secretchat.data_source.repository.FileRepository
import java.util.*

class FileStorageFirestore(
    private val bucket: String
): FileRepository {
    private val root = FirebaseStorage.getInstance().reference

    override fun saveSync(bytes: ByteArray): String {
        val name = UUID.randomUUID().toString()
        return saveSync(bytes, name)
    }

    override fun saveSync(bytes: ByteArray, name: String): String {
        val task = root.child("$bucket/$name")
            .putBytes(bytes)
        val result = Tasks.await(task)
        if (result.error != null) {
            return ""
        }
        return "$bucket/$name"
    }

    override fun getSync(path: String): ByteArray {
        val task = root.child(path)
            .getBytes(MAX_DOWNLOAD_SIZE_BYTES)
        return Tasks.await(task)
    }

    companion object {
        const val MAX_DOWNLOAD_SIZE_BYTES = 4294967296L
    }
}