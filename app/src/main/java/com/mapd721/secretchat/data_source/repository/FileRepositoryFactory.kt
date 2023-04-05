package com.mapd721.secretchat.data_source.repository

import com.mapd721.secretchat.data_source.firestore.file.FileStorageFirestore

class FileRepositoryFactory {
    companion object {
        fun getFireStore(bucket: String): FileRepository {
            return FileStorageFirestore(bucket)
        }
    }
}