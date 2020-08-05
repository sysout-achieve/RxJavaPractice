package com.gun.rxjavapractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_hello.setOnClickListener {
            val nextIntent = Intent(this, HelloActivity::class.java)
            startActivity(nextIntent)
            finish()
        }

        btn_loop.setOnClickListener {
            val nextIntent = Intent(this, LoopActivity::class.java)
            startActivity(nextIntent)
            finish()
        }

    }
}
