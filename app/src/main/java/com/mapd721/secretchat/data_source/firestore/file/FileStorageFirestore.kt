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
        val task = root.child("$bucket/$name")
            .putBytes(bytes)
        val result = Tasks.await(task)
        if (result.error != null) {
            return ""
        }
        return "$bucket/$name"
    }
}