package com.gun.rxjavapractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nextIntent = Intent(this, HelloActivity::class.java)
        startActivity(nextIntent)
        finish()
    }
}
