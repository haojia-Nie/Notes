package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.actionBarMessage.observe(this){
            if (viewModel.actionBarMessage.value != null){
                getSupportActionBar()?.setSubtitle(viewModel.actionBarMessage.value);
            }
        }

    }
}