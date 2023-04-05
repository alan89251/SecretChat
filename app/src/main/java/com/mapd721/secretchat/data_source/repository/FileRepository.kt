package com.mapd721.secretchat.data_source.repository

interface FileRepository {
    /**
     * @return path of the uploaded file
     */
    fun saveSync(bytes: ByteArray): String
}