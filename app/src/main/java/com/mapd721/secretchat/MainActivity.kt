package com.mapd721.secretchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mapd721.secretchat.database.SecretChatDatabase
import com.mapd721.secretchat.logic.SelfEncryptionKeyPairInitializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SelfEncryptionKeyPairInitializer.run(this@MainActivity)
    }
}