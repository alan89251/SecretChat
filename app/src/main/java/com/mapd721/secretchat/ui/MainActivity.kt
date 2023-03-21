package com.mapd721.secretchat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.mapd721.secretchat.R
import com.mapd721.secretchat.ui.view_model.GlobalViewModel

class MainActivity : AppCompatActivity() {
    private val globalViewModel: GlobalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}