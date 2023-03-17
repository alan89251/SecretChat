package com.mapd721.secretchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mapd721.secretchat.encryption.KeyPairManager
import com.mapd721.secretchat.encryption.MessageCipher

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}